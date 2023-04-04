package hr.projekt.secureVideoFile.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileEncryptionUtil {

    //Arbitrarily selected 8-byte salt sequence:
    private static final byte[] salt = {
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
    };

    private static Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException {

        //Use a KeyFactory to derive the corresponding key from the passphrase:
        PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        //Create parameters from the salt and an arbitrary number of iterations:
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);

        //Set up the cipher:
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

        //Set the cipher mode to decryption or encryption:
        if (decryptMode) {
            cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
        }

        return cipher;
    }


    public static byte[] encryptFile(byte[] fileData, String pass) throws GeneralSecurityException {
        byte[] decData;
        byte[] encData;

        // Generate the cipher using pass
        Cipher cipher = makeCipher(pass, true);

        int blockSize = 8;
        // Figure out how many bytes are padded
        int paddedCount = blockSize - (fileData.length % blockSize);

        // Figure out full size including padding
        int padded = fileData.length + paddedCount;

        decData = new byte[padded];

        System.arraycopy(fileData, 0, decData, 0, fileData.length);

        // Write out padding bytes as per PKCS5 algorithm
        for (int i = fileData.length; i < padded; ++i) {
            decData[i] = (byte) paddedCount;
        }

        // Encrypt the file data
        encData = cipher.doFinal(decData);

        return encData;
    }


    /**
     * Decrypts one file to a second file using a key derived from a passphrase:
     **/
    public static byte[] decryptFile(byte[] encryptedData, String pass) throws GeneralSecurityException, IOException {
        byte[] encData = encryptedData.clone();
        byte[] decData;

        // Generate the cipher using pass
        Cipher cipher = makeCipher(pass, false);

        // Decrypt the file data
        decData = cipher.doFinal(encData);

        // Figure out how much padding to remove
        int padCount = (int) decData[decData.length - 1];


        if (padCount >= 1 && padCount <= 8) {
            decData = Arrays.copyOfRange(decData, 0, decData.length - padCount);
        }

        return decData;
    }
}
