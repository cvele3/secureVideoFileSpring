package hr.projekt.secureVideoFile.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;


@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtil {

    public static String getImageBinaryString(String filePath) {
        BufferedImage image = null;
        try {
            image = javax.imageio.ImageIO.read(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int pixelValue = (pixel & 0xff) > 128 ? 1 : 0;
                sb.append(pixelValue);
            }
        }
        return sb.toString();
    }

    public static String getBinaryStringFromImageList(List<BufferedImage> list) {
        StringBuilder sb = new StringBuilder();

        for(BufferedImage image : list){
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = image.getRGB(x, y);
                    int pixelValue = (pixel & 0xff) > 128 ? 1 : 0;
                    sb.append(pixelValue);
                }
            }
        }
        return sb.toString();
    }

    public static String getBinaryStringFromByteArray(byte[] data, int width, int height) {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelIndex = y * width + x;
                int pixelValue = (data[pixelIndex] & 0xff) > 128 ? 1 : 0;
                sb.append(pixelValue);
            }
        }
        return sb.toString();
    }

    public static int[] calculateMultiplication(int input, int num1, int num2) {
        int multiplication = num1 * num2;
        int count = input / multiplication;
        int missing = multiplication - (input % multiplication);
        if (missing == multiplication) {
            missing = 0;
        }
        int[] result = {count, missing};
        return result;
    }
    public static String addRandomize(String binary, int missing) {
        StringBuilder sb = new StringBuilder(binary);
        Random rand = new Random();
        for (int i = 0; i < missing; i++) {
            sb.append(rand.nextInt(2));
        }
        return sb.toString();
    }

}
