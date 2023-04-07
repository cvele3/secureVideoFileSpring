package hr.projekt.secureVideoFile.managers;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileConversionManager {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file
     * @param password
     * @return byte[] of video that was made from file conversion
     */
    String convertFileToSignedVideo(MultipartFile file, String password, String name);
    byte[] convertSignedVideoToFile(String videoName, String password);

}
