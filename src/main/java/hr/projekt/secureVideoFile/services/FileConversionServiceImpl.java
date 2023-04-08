package hr.projekt.secureVideoFile.services;

import hr.projekt.secureVideoFile.constants.ImageConstants;
import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.FileEncryptionException;
import hr.projekt.secureVideoFile.utils.BinaryUtil;
import hr.projekt.secureVideoFile.utils.ByteArrayUtil;
import hr.projekt.secureVideoFile.utils.FileEncryptionUtil;
import hr.projekt.secureVideoFile.utils.ImageUtil;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class FileConversionServiceImpl implements FileConversionService{

    @Override
    public Pair<List<BufferedImage>, String> convertFileToImageList(MultipartFile file, String password, String name) {

        // Get byte[] from MultipartFile
        byte[] fileContent = ByteArrayUtil.readMultipartFileToByteArray(file);

        // Encrypt byte[]
        try {
            fileContent = FileEncryptionUtil.encryptFile(fileContent, password);
        } catch (GeneralSecurityException e) {
            throw new FileEncryptionException(StatusCode.FILE_ENCRYPTION_ERROR,e.getMessage());
        }

        // Convert encrypted byte[] to binary string
        String binaryString = ByteArrayUtil.toBinaryString(fileContent);
        int totalPixels = binaryString.length();
        String videoName = name + "-" + totalPixels;

        // Adjusting images
        int[] imageData = ImageUtil.calculateMultiplication(totalPixels, ImageConstants.TARGET_WIDTH, ImageConstants.TARGET_HEIGHT);
        String conversionBinaryString = ImageUtil.addRandomize(binaryString, imageData[1]);
        imageData = ImageUtil.calculateMultiplication(conversionBinaryString.length(), ImageConstants.TARGET_WIDTH, ImageConstants.TARGET_HEIGHT);

        // Split the binary string into smaller chunks
        List<String> binaryChunks = new ArrayList<>();
        for (int i = 0; i < imageData[0]; i++) {
            int startIndex = i * ImageConstants.PIXELS_PER_IMAGE;
            int endIndex = startIndex + ImageConstants.PIXELS_PER_IMAGE;
            String chunk = conversionBinaryString.substring(startIndex, endIndex);
            binaryChunks.add(chunk);
        }

        // Create Buffered images
        List<BufferedImage> imageList = new ArrayList<>();
        for (int i = 0; i < binaryChunks.size(); i++) {
            imageList.add(BinaryUtil.createImage(binaryChunks.get(i), ImageConstants.TARGET_WIDTH, ImageConstants.TARGET_HEIGHT));
        }

        return new Pair<>(imageList, videoName);
    }


    @Override
    public byte[] convertImageListToFile(List<BufferedImage> listOfImages, String password, String videoName) {
        // Read each image and convert to binary string
        StringBuilder sb = new StringBuilder();
        int totalPixels = extractNumber(videoName);

        sb.append(ImageUtil.getBinaryStringFromImageList(listOfImages));

        String binaryString2 = sb.toString();
        binaryString2 = binaryString2.substring(0, totalPixels);

        // Convert binary string to byte array
        byte[] fileContent = BinaryUtil.fromBinaryString(binaryString2);

        // Decrypt File byte[]
        try {
            fileContent = FileEncryptionUtil.decryptFile(fileContent, password);
        } catch (Exception e) {
            throw new FileEncryptionException(StatusCode.FILE_DECRYPTION_ERROR, e.getMessage());
        }


        return fileContent;
    }

    private Integer extractNumber(String input) {
        int startIdx = input.indexOf("-") + 1;
        int endIdx = input.indexOf(".mp4");

        return Integer.parseInt(input.substring(startIdx, endIdx));
    }

}
