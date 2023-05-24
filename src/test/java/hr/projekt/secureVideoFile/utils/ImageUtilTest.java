package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {

    private String binaryString = "01111111";
    byte[] byteArray = BinaryUtil.fromBinaryString(binaryString);
    private String path = "path/that/doesn't/exists";
    private int width = 2;
    private int height = 4;

    @Test
    void getImageBinaryString() {

        assertThrows(FileManipulationException.class, () -> ImageUtil.getImageBinaryString(path));
    }

    @Test
    void getBinaryStringFromImageList() {

        assertThrows(NullPointerException.class, () -> ImageUtil.getBinaryStringFromImageList(null));
    }

    @Test
    void getBinaryStringFromByteArray() {

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ImageUtil.getBinaryStringFromByteArray(byteArray, width, height));
    }

    @Test
    void calculateMultiplication() {
        int[] result = ImageUtil.calculateMultiplication(binaryString.length(), width, height);

        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
    }

    @Test
    void addRandomize() {

        assertThrows(NullPointerException.class, () -> ImageUtil.addRandomize(null, 2));
    }
}