package hr.projekt.secureVideoFile.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.sun.tools.javac.Main;
import hr.projekt.secureVideoFile.constants.VideoConstants;
import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.GoogleDriveVideoDownloadException;
import hr.projekt.secureVideoFile.utils.VideoUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {


    @Override
    public String imageListToVideoAndToGoogleDrive(List<BufferedImage> imageList, String videoName) {

        Drive service = initializeDrive();

        try {
            VideoUtil.imagesToVideo(imageList, videoName + VideoConstants.VIDEO_FORMAT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        File file = new File(VideoConstants.TMP_STORAGE + videoName + VideoConstants.VIDEO_FORMAT);
        FileContent mediaContent = new FileContent(VideoConstants.MIME_TYPE, file);

        // Create a file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(videoName + VideoConstants.VIDEO_FORMAT);

        // Upload the file to Google Drive
        com.google.api.services.drive.model.File uploadedFile = null;
        try {
            uploadedFile = service.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, size")
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Delete the file after uploading to Google Drive
        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException("Failed to delete file: " + file.getAbsolutePath());
        }

        return videoName + VideoConstants.VIDEO_FORMAT;
    }

    @Override
    public List<BufferedImage> videoFromGoogleDriveToImageList(String videoName) {
        Drive service = initializeDrive();

        String outputFilePath = VideoConstants.TMP_STORAGE + videoName;
        try {
            String query = "mimeType='video/mp4' and trashed = false and name='" + videoName + "'";
            FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();

            com.google.api.services.drive.model.File videoFile = result.getFiles().get(0);
            String fileId = videoFile.getId();

            OutputStream outputStream = new FileOutputStream(outputFilePath);
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            outputStream.close();

        } catch (IOException e) {
            log.error("Error while getting video from drive");
            throw  new GoogleDriveVideoDownloadException(StatusCode.GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR, e.getMessage());
        }

        List<BufferedImage> imageList = null;
        try {
            imageList = VideoUtil.videoToImages(outputFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        File file = new File(outputFilePath);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException("Failed to delete file: " + file.getAbsolutePath());
        }

        return imageList;
    }


    private Drive initializeDrive(){
        JsonFactory jsonFactory = new GsonFactory();

        NetHttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        File file = new File("client_secret.json");
        GoogleClientSecrets clientSecrets = null;
        try {
            FileInputStream in = new FileInputStream(file);
            clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GoogleAuthorizationCodeFlow flow = null;
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    jsonFactory,
                    clientSecrets,
                    Collections.singletonList(DriveScopes.DRIVE)
            )
                    .setDataStoreFactory(new FileDataStoreFactory(new File("driveCredentials")))
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = null;
        try {
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("my-comparison-application")
                .build();

    }
}
