package hr.projekt.secureVideoFile.managers;

import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;


public interface FileConversionManager {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file - input file from the user
     * @param password - user's secret key which is used for encryption
     * @param name
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

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file
     * @param password
     * @param name
     * @return URL and video name strings of video that was made from file conversion
     */
    Pair<String,String> convertFileToSignedVideoAndGetURLAndVideoName(MultipartFile file, String password, String name);

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param videoName
     * @param password
     * @param URL
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFileUsingURLAndVideoName(String videoName, String password, String URL);

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param password
     * @param URL
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFileUsingURL( String password, String URL);

}
