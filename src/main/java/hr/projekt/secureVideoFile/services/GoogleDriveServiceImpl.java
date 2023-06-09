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
import hr.projekt.secureVideoFile.exceptions.*;
import hr.projekt.secureVideoFile.utils.VideoUtil;
import javafx.util.Pair;
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

        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();


        log.info("\t \t Converting encrypted buffered images to video..");

        try {
            VideoUtil.imagesToVideo(imageList, videoName + VideoConstants.VIDEO_FORMAT);
        } catch (Exception e) {
            throw new ImageConversionException(StatusCode.IMAGE_CONVERSION_ERROR,e.getMessage());
        }


        log.info("\t \t Converting video to file..");


        File file = new File(VideoConstants.TMP_STORAGE + videoName + VideoConstants.VIDEO_FORMAT);
        FileContent mediaContent = new FileContent(VideoConstants.MIME_TYPE, file);

        // Create a file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(videoName + VideoConstants.VIDEO_FORMAT);


        log.info("\t \t Uploading file to Google Drive..");

        com.google.api.services.drive.model.File uploadedFile = null;
        try {
            uploadedFile = service.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, size")
                    .execute();
        } catch (IOException e) {
            throw new GoogleDriveUploadException(StatusCode.GOOGLE_DRIVE_UPLOAD_ERROR, e.getMessage());
        }


        log.info("\t \t Cleaning up temp file saved locally..");

        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        return videoName + VideoConstants.VIDEO_FORMAT;
    }

    @Override
    public List<BufferedImage> videoFromGoogleDriveToImageList(String videoName) {

        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();


        log.info("\t \t Downloading video from Google Drive..");

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


        log.info("\t \t Converting video to images..");

        List<BufferedImage> imageList = null;
        try {
            imageList = VideoUtil.videoToImages(outputFilePath);
        } catch (Exception e) {
            throw new VideoConversionException(StatusCode.VIDEO_CONVERSION_ERROR, e.getMessage());
        }

        log.info("\t \t Cleaning up temp file saved locally..");

        File file = new File(outputFilePath);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        return imageList;
    }

    @Override
    public String imageListToVideoAndToGoogleDriveURL(List<BufferedImage> imageList, String videoName) {

        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();

        log.info("\t \t Converting images to video..");


        try {
            VideoUtil.imagesToVideo(imageList, videoName + VideoConstants.VIDEO_FORMAT);
        } catch (Exception e) {
            throw new ImageConversionException(StatusCode.IMAGE_CONVERSION_ERROR,e.getMessage());
        }

        File file = new File(VideoConstants.TMP_STORAGE + videoName + VideoConstants.VIDEO_FORMAT);
        FileContent mediaContent = new FileContent(VideoConstants.MIME_TYPE, file);

        // Create a file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(videoName + VideoConstants.VIDEO_FORMAT);


        log.info("\t \t Uploading video to Google Drive using Drive URL..");

        com.google.api.services.drive.model.File uploadedFile = null;
        try {
            uploadedFile = service.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, size, webViewLink")
                    .execute();
        } catch (IOException e) {
            throw new GoogleDriveUploadException(StatusCode.GOOGLE_DRIVE_UPLOAD_ERROR, e.getMessage());
        }


        log.info("\t \t Cleaning up temp file saved locally..");

        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        // Return the webViewLink field, which contains the URL of the uploaded file in Google Drive
        return uploadedFile.getWebViewLink();
    }

    @Override
    public Pair<String, String> imageListToVideoAndToGoogleDriveURLAndVideoName(List<BufferedImage> imageList, String videoName) {

        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();

        log.info("\t \t Converting images to video..");

        try {
            VideoUtil.imagesToVideo(imageList, videoName + VideoConstants.VIDEO_FORMAT);
        } catch (Exception e) {
            throw new ImageConversionException(StatusCode.IMAGE_CONVERSION_ERROR,e.getMessage());
        }

        File file = new File(VideoConstants.TMP_STORAGE + videoName + VideoConstants.VIDEO_FORMAT);
        FileContent mediaContent = new FileContent(VideoConstants.MIME_TYPE, file);

        // Create a file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(videoName + VideoConstants.VIDEO_FORMAT);

        log.info("\t \t Uploading video to Google Drive using Drive URL and video name..");

        com.google.api.services.drive.model.File uploadedFile = null;
        try {
            uploadedFile = service.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, size, webViewLink")
                    .execute();
        } catch (IOException e) {
            throw new GoogleDriveUploadException(StatusCode.GOOGLE_DRIVE_UPLOAD_ERROR, e.getMessage());
        }

        log.info("\t \t Cleaning up temp file saved locally..");

        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        // Return the video name and webViewLink field, which contains the URL of the uploaded file in Google Drive
        return new Pair<>(videoName + VideoConstants.VIDEO_FORMAT, uploadedFile.getWebViewLink());
    }

    @Override
    public List<BufferedImage> videoFromGoogleDriveToImageListUsingURLAndVideoName(String URL, String videoName) {
        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();


        log.info("\t \t Downloading video from Google Drive..");

        String outputFilePath = VideoConstants.TMP_STORAGE + videoName;
        try {
            String fileId = extractFileIdFromUrl(URL);

            OutputStream outputStream = new FileOutputStream(outputFilePath);
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            outputStream.close();

        } catch (IOException e) {
            log.error("Error while getting video from drive");
            throw new GoogleDriveVideoDownloadException(StatusCode.GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR, e.getMessage());
        }


        log.info("\t \t Converting video to images..");

        List<BufferedImage> imageList = null;
        try {
            imageList = VideoUtil.videoToImages(outputFilePath);
        } catch (Exception e) {
            throw new VideoConversionException(StatusCode.VIDEO_CONVERSION_ERROR, e.getMessage());
        }

        log.info("\t \t Cleaning up temp file saved locally..");

        File file = new File(outputFilePath);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        return imageList;
    }

    @Override
    public Pair<List<BufferedImage>, String> videoFromGoogleDriveToImageListUsingURL(String URL) {
        log.info("\t \t Initializing drive..");

        Drive service = initializeDrive();


        log.info("\t \t Downloading video from Google Drive..");

        String videoName = getVideoFileNameFromUrl(URL);

        String outputFilePath = VideoConstants.TMP_STORAGE + videoName;
        try {
            String fileId = extractFileIdFromUrl(URL);

            OutputStream outputStream = new FileOutputStream(outputFilePath);
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            outputStream.close();

        } catch (IOException e) {
            log.error("Error while getting video from drive");
            throw new GoogleDriveVideoDownloadException(StatusCode.GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR, e.getMessage());
        }


        log.info("\t \t Converting video to images..");

        List<BufferedImage> imageList = null;
        try {
            imageList = VideoUtil.videoToImages(outputFilePath);
        } catch (Exception e) {
            throw new VideoConversionException(StatusCode.VIDEO_CONVERSION_ERROR, e.getMessage());
        }

        log.info("\t \t Cleaning up temp file saved locally..");

        File file = new File(outputFilePath);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new FileManipulationException(StatusCode.FILE_DELETION_ERROR, "Error while deleting file");
        }

        return new Pair<>(imageList, videoName);
    }

    @Override
    public boolean deleteUserVideos(List<String> videoUrls) {
        log.info("\t \t Initializing drive..");
        Drive service = initializeDrive();

        for (String videoUrl : videoUrls) {
            String fileId = extractFileIdFromUrl(videoUrl);
            if (fileId == null) {
                log.error("Failed to extract file ID from URL: " + videoUrl);
                continue;
            }

            try {
                log.info("\t \t Deleting video from Google Drive: " + videoUrl);
                service.files().delete(fileId).execute();
                log.info("\t \t Successfully deleted video from Google Drive: " + videoUrl);
            } catch (IOException e) {
                log.error("Error while deleting video from Google Drive: " + videoUrl, e);
                throw new GoogleDriveDeleteException(StatusCode.GOOGLE_DRIVE_DELETE_ERROR, e.getMessage());
            }
        }
        return true;
    }

    private String extractFileIdFromUrl(String url) {
        int start = url.indexOf("d/") + 2;
        int end = url.indexOf("/view");

        if (start != -1 && end != -1) {
            return url.substring(start, end);
        } else {
            throw new IllegalArgumentException("Invalid Google Drive video URL");
        }
    }


    private String getVideoFileNameFromUrl(String videoUrl) {
        String fileId = extractFileIdFromUrl(videoUrl);

        Drive service = initializeDrive();
        try {
            com.google.api.services.drive.model.File file = service.files().get(fileId).setFields("name").execute();
            return file.getName();
        } catch (IOException e) {
            log.error("Error while getting video file name from drive");
            throw new GoogleDriveVideoDownloadException(StatusCode.GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR, e.getMessage());
        }
    }


    private Drive initializeDrive(){
        JsonFactory jsonFactory = new GsonFactory();

        NetHttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new HttpTransportException(StatusCode.HTTP_TRANSPORT_ERROR, e.getMessage());
        }


        File file = new File("client_secret.json");
        GoogleClientSecrets clientSecrets = null;
        try {
            FileInputStream in = new FileInputStream(file);
            clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        } catch (IOException e) {
            throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
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
            throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
        }

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = null;
        try {
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
        }


        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("my-comparison-application")
                .build();

    }
}
