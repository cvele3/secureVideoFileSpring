package hr.projekt.secureVideoFile.managers;

import hr.projekt.secureVideoFile.services.FileConversionService;
import hr.projekt.secureVideoFile.services.GoogleDriveService;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Log4j2
@Service
public class FileConversionManagerImpl implements FileConversionManager{


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
        Pair<List<BufferedImage>, String> imagesAndName = fileConversionService.convertFileToImageList(file, password, name);

       log.info("1. Reading file into bytes.");
       String videoName = googleDriveService.imageListToVideoAndToGoogleDrive(imagesAndName.getKey(), imagesAndName.getValue());

       log.info("2. Encrypting bytes.");

        return videoName;
    }

    @Override
    public byte[] convertSignedVideoToFile(String videoName, String password) {
        log.info("Getting file from uploaded video.");
        List<BufferedImage> imageList = googleDriveService.videoFromGoogleDriveToImageList(videoName);

        log.info("1. Downloading video from google disk..");
        byte[] fileContent = fileConversionService.convertImageListToFile(imageList, password, videoName);

        log.info("2. Converting video to bytes..");

        log.info("3. Decrypting bytes..");

        log.info("4. Saving decrypted bytes to file..");


        return fileContent;
    }
}
