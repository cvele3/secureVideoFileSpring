package hr.projekt.secureVideoFile.services;

import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.util.List;

public interface GoogleDriveService {

    /**
     * This method is used for merging images to video and storing it on Google Drive
     *
     * @param imageList
     * @param videoName
     * @return path where video is located after storing it on Google Drive
     */
    String imageListToVideoAndToGoogleDrive(List<BufferedImage> imageList, String videoName);

    /**
     * This method is used for fetching video from Google Drive and converting it to image list
     *
     * @param videoName
     * @return image list used later on for conversion to file
     */
    List<BufferedImage> videoFromGoogleDriveToImageList(String videoName);


    /**
     * This method is used for merging images to video and storing it on Google Drive
     *
     * @param imageList
     * @param videoName
     * @return url of video on Google Drive
     */
    String imageListToVideoAndToGoogleDriveURL(List<BufferedImage> imageList, String videoName);

    /**
     * This method is used for merging images to video and storing it on Google Drive
     *
     * @param imageList
     * @param videoName
     * @return url and video name of video on Google Drive
     */
    Pair<String, String> imageListToVideoAndToGoogleDriveURLAndVideoName(List<BufferedImage> imageList, String videoName);

    /**
     * This method is used for fetching video from Google Drive and converting it to image list
     *
     * @param URL
     * @param videoName
     * @return image list used later on for conversion to file
     */
    List<BufferedImage> videoFromGoogleDriveToImageListUsingURLAndVideoName(String URL, String videoName);


    /**
     * This method is used for fetching video from Google Drive and converting it to image list
     *
     * @param URL
     * @return image list used later on for conversion to file
     */
    Pair<List<BufferedImage>, String> videoFromGoogleDriveToImageListUsingURL(String URL);
}
