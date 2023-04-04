package hr.projekt.secureVideoFile.services;

import java.awt.image.BufferedImage;
import java.util.List;

public interface GoogleDriveService {

    /**
     * This method is used for merging images to video and storing it on Google Drive
     *
     * @param imageList
     * @return path where video is located after storing it on Google Drive
     */
    String imageListToVideoAndToGoogleDrive(List<BufferedImage> imageList);

    /**
     * This method is used for fetching video from Google Drive and converting it to image list
     *
     * @param pathToVideoOnGoogleDrive
     * @return image list used later on for conversion to file
     */
    List<BufferedImage> videoFromGoogleDriveToImageList(String pathToVideoOnGoogleDrive);

}
