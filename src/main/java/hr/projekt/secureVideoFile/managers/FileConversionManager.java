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

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param videoName
     * @param password
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFile(String videoName, String password);

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file
     * @param password
     * @param name
     * @return URL string of video that was made from file conversion
     */
    String convertFileToSignedVideoAndGetURL(MultipartFile file, String password, String name);

}
