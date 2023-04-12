package hr.projekt.secureVideoFile.managers;

import hr.projekt.secureVideoFile.services.FileConversionService;
import hr.projekt.secureVideoFile.services.GoogleDriveService;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

@Log4j2
@Service
public class FileConversionManagerImpl implements FileConversionManager {


    private final FileConversionService fileConversionService;
    private final GoogleDriveService googleDriveService;


    @Autowired
    public FileConversionManagerImpl(FileConversionService fileConversionService, GoogleDriveService googleDriveService) {
        this.fileConversionService = fileConversionService;
        this.googleDriveService = googleDriveService;
    }


    @Override
    public String convertFileToSignedVideo(MultipartFile file, String password, String name) {
        log.info("Conversion of file to signed video.");

        log.info("\t 1.Converting and encrypting file to binary images..");
        Pair<List<BufferedImage>, String> imagesAndName = fileConversionService.convertFileToImageList(file, password, name);

        log.info("\t 2. Converting images to video and uploading to Google Drive..");
        String videoName = googleDriveService.imageListToVideoAndToGoogleDrive(imagesAndName.getKey(), imagesAndName.getValue());

        log.info("\t    Conversion finished!");

        return videoName;
    }

    @Override
    public byte[] convertSignedVideoToFile(String videoName, String password) {
        log.info("Getting file from signed uploaded video.");

        log.info("\t 1. Downloading video from google disk and converting to images..");
        List<BufferedImage> imageList = googleDriveService.videoFromGoogleDriveToImageList(videoName);

        log.info("\t 2. Decrypting images and converting them to a file..");
        byte[] fileContent = fileConversionService.convertImageListToFile(imageList, password, videoName);

        log.info("\t    Retrieving file from Google Drive finished!");


        return fileContent;
    }
}
