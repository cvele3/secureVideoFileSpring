package hr.projekt.secureVideoFile.services;

import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

public interface FileConversionService {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file input file
     * @param password user's secret key which is used for encryption
     * @return Pair of image list and video name
     */
    Pair<List<BufferedImage>, String> convertFileToImageList(MultipartFile file, String password, String name);

    /**
     * This method is used for converting and decrypting image list to file
     *
     * @param imageList images containing enrypted byte data, retreived from a video from Google drive storage
     * @param password user's secret key which is used for decryption
     * @return byte[] of file that was made from conversion
     */
    byte[] convertImageListToFile(List<BufferedImage> imageList, String password, String videoName);

}
