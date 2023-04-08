package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BinaryUtil {

    public static byte[] fromBinaryString(String binaryString) {
        int len = binaryString.length();
        byte[] data = new byte[len / 8];
        for (int i = 0; i < len; i += 8) {
            data[i / 8] = (byte) Integer.parseInt(binaryString.substring(i, i + 8), 2);
        }
        return data;
    }

    public static void saveImage(String binaryString, int width, int height, String filePath)  {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = (binaryString.charAt(index++) == '0') ? 0 : 255;
                image.setRGB(x, y, (pixelValue << 16) | (pixelValue << 8) | pixelValue);
            }
        }
        File output = new File(filePath);
        try {
            javax.imageio.ImageIO.write(image, "png", output);
        } catch (IOException e) {
            throw new FileManipulationException(StatusCode.ERROR_WHILE_SAVING_FILE, e.getMessage());
        }
    }

    public static BufferedImage createImage(String binaryString, int width, int height)  {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = (binaryString.charAt(index++) == '0') ? 0 : 255;
                image.setRGB(x, y, (pixelValue << 16) | (pixelValue << 8) | pixelValue);
            }
        }

        return image;
    }
}
