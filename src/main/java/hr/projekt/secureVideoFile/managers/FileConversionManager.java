package hr.projekt.secureVideoFile.managers;

import org.springframework.web.multipart.MultipartFile;

public interface FileConversionManager {

    /**
     * This method is used for converting and encrypting user file
     *
     * @param file
     * @param password
     * @return byte[] of video that was made from file conversion
     */
    byte[] convertFileToImage(MultipartFile file, String password);
}
