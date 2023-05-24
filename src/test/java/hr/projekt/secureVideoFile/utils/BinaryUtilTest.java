package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryUtilTest {

    private String binaryString = "01111111";
    private String path = "path/that/doesn't/exists";
    private int width = 2;
    private int height = 4;

    @Test
    void fromBinaryString() {
        byte[] byteArray = BinaryUtil.fromBinaryString(binaryString);

        assertEquals(1, byteArray.length);
    }

    @Test
    void saveImage() {

        assertThrows(FileManipulationException.class, () -> BinaryUtil.saveImage(binaryString, width, height, path));
    }

    @Test
    void createImage() {

        assertNotNull(BinaryUtil.createImage(binaryString, width, height));
    }
}