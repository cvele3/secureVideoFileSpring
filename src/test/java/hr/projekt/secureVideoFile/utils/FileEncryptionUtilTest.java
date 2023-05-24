package hr.projekt.secureVideoFile.utils;

import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class FileEncryptionUtilTest {

    private String binaryString = "01111111";
    private String password = "passwordBasedEncryption";
    byte[] byteArray = BinaryUtil.fromBinaryString(binaryString);

    @Test
    void encryptFile() throws GeneralSecurityException {

        assertNotEquals(byteArray, FileEncryptionUtil.encryptFile(byteArray, password));
    }

    @Test
    void decryptFile() throws GeneralSecurityException {
        byte[] encryptedByteArray = FileEncryptionUtil.encryptFile(byteArray, password);

        assertThrows(GeneralSecurityException.class, () -> FileEncryptionUtil.decryptFile(encryptedByteArray, "incorrectPassword"));
    }
}