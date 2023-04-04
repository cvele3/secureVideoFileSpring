package hr.projekt.secureVideoFile.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

        return fileContent;
    }
}
