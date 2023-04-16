package hr.projekt.secureVideoFile.managers;

import hr.projekt.secureVideoFile.request.UserInfoRequest;
import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileConversionManager {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file input file from the user
     * @param password user's secret key which is used for encryption
     * @param name name of the file
     * @return byte[] of video that was made from file conversion
     */
    String convertFileToSignedVideo(MultipartFile file, String password, String name);

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param videoName name of the uploaded video in storage (Google Drive)
     * @param password key used to decyrpt data
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFile(String videoName, String password);

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file input file for upload to storage
     * @param password key used to encrypt data
     * @param name name of the file
     * @return URL string of video that was made from file conversion
     */
    String convertFileToSignedVideoAndGetURL(MultipartFile file, String password, String name);

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file input file for upload to storage
     * @param password key used to encrypt data
     * @param name name of the file
     * @return URL and video name strings of video that was made from file conversion
     */
    Pair<String,String> convertFileToSignedVideoAndGetURLAndVideoName(MultipartFile file, String password, String name);

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param videoName name of the uploaded video
     * @param password key used to decrypt data
     * @param URL url of the video on Google Drive
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFileUsingURLAndVideoName(String videoName, String password, String URL);

    /**
     * This method is used to convert video from Google Drive to original file
     *
     * @param password key used to decrypt data
     * @param URL url of the video on Google Drive
     * @return byte[] of original file
     */
    byte[] convertSignedVideoToFileUsingURL( String password, String URL);

    /**
     * This method is used for deleting user videos
     *
     * @param userInfoRequests list of user info request
     * @return boolean depending on if deletion was successful
     */
    boolean deleteUserVideos(List<UserInfoRequest> userInfoRequests);

}
