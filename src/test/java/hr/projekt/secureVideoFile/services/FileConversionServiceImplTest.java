package hr.projekt.secureVideoFile.services;

import hr.projekt.secureVideoFile.exceptions.FileEncryptionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileConversionServiceImplTest {

    @InjectMocks
    private FileConversionServiceImpl fileConversionService;
    @Mock
    private MultipartFile multipartFile;

    public FileConversionServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertFileToImageList() {

        byte[] byteArray = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        MultipartFile mockMultipartFile = new MockMultipartFile("file", byteArray);

        fileConversionService.convertFileToImageList(mockMultipartFile,"test_password", "test_name");

        Assertions.assertNotNull(fileConversionService.convertFileToImageList(mockMultipartFile,"test_password", "test_name"));

    }

    @Test
    public void testConvertFileToImageListException() {

        assertThrows(NullPointerException.class, () -> {
            fileConversionService.convertFileToImageList(null, null ,null);
        });

    }

    @Test
    public void testConvertImageListToFileDecryptionException() {

        List<BufferedImage> bufferedImageList = new ArrayList<>();
        bufferedImageList.add(new BufferedImage(123, 123, 5));

        assertThrows(FileEncryptionException.class, () -> {
            fileConversionService.convertImageListToFile(bufferedImageList, "test_password", "test-14000.mp4");
        });

    }

    @Test
    public void testConvertImageListToFileNullException() {

        assertThrows(NullPointerException.class, () -> {
            fileConversionService.convertImageListToFile(null, "test_password", "test-14000.mp4");
        });

    }

}
