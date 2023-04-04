package hr.projekt.secureVideoFile.services;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

public interface FileConversionService {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file
     * @param password
     * @return image list that was made from file conversion
     */
    List<BufferedImage> convertFileToImageList(MultipartFile file, String password);

    /**
     * This method is used for converting and decrypting image list to file
     *
     * @param imageList
     * @param password
     * @return byte[] of file that was made from conversion
     */
    byte[] convertImageListToFile(List<BufferedImage> imageList, String password);

}
