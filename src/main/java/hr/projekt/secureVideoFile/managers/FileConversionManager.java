package hr.projekt.secureVideoFile.managers;

import org.springframework.web.multipart.MultipartFile;


public interface FileConversionManager {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file - input file from the user
     * @param password - user's secret key which is used for encryption
     * @return byte[] of video that was made from file conversion
     */
    String convertFileToSignedVideo(MultipartFile file, String password, String name);
    byte[] convertSignedVideoToFile(String videoName, String password);

}
