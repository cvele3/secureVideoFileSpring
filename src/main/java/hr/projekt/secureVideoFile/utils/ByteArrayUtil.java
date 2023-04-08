package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteArrayUtil {

    public static void writeByteArrayToFile(byte[] byteArray, String filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(byteArray);
            fos.close();
        } catch (Exception e) {
            throw new FileManipulationException(StatusCode.ERROR_WHILE_SAVING_FILE, e.getMessage());
        }
    }

    public static String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

    public static byte[] readFileToByteArray(String filePath) {
        File file = new File(filePath);
        byte[] fileContent = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(fileContent);
            fis.close();
        } catch (Exception e) {
            throw new FileManipulationException(StatusCode.ERROR_WHILE_SAVING_FILE, e.getMessage());
        }

        return fileContent;
    }

    public static byte[] readMultipartFileToByteArray(MultipartFile file) {
        byte[] fileContent;
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new FileManipulationException(StatusCode.ERROR_WHILE_READING_FILE, e.getMessage());
        }

        return fileContent;
    }

}
