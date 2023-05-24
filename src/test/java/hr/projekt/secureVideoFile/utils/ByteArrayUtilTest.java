package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayUtilTest {

    private String binaryString = "01111111";
    private String path = "path/that/doesn't/exists";

    @Test
    void writeByteArrayToFile() {
        byte[] byteArray = BinaryUtil.fromBinaryString(binaryString);

        assertThrows(FileManipulationException.class, () -> ByteArrayUtil.writeByteArrayToFile(byteArray, path));
    }

    @Test
    void toBinaryString() {

        assertNotEquals("10010011", ByteArrayUtil.toBinaryString(BinaryUtil.fromBinaryString(binaryString)));
    }

    @Test
    void readFileToByteArray() {

        assertThrows(FileManipulationException.class, () -> ByteArrayUtil.readFileToByteArray(path));
    }

    @Test
    void readMultipartFileToByteArray() {

        assertThrows(NullPointerException.class, () -> ByteArrayUtil.readMultipartFileToByteArray(null));
    }
}