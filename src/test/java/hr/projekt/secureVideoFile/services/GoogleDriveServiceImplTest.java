package hr.projekt.secureVideoFile.services;

import hr.projekt.secureVideoFile.exceptions.GoogleDriveDeleteException;
import hr.projekt.secureVideoFile.exceptions.GoogleDriveUploadException;
import hr.projekt.secureVideoFile.exceptions.GoogleDriveVideoDownloadException;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;


import static org.junit.jupiter.api.Assertions.assertThrows;

public class GoogleDriveServiceImplTest {

     @InjectMocks
     private GoogleDriveServiceImpl googleDriveService;

    public GoogleDriveServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testImageListToVideoAndToGoogleDrive() {

        List<BufferedImage> bufferedImageList = new ArrayList<>();
        bufferedImageList.add(new BufferedImage(1920, 1080, 5));

        Assertions.assertNotNull(googleDriveService.imageListToVideoAndToGoogleDrive(bufferedImageList, "test"));
    }

    @Test
    public void testVideoFromGoogleDriveToImageList() {

        Assertions.assertNotNull(googleDriveService.videoFromGoogleDriveToImageList("testTest-14993920.mp4"));


    }

    @Test
    public void testImageListToVideoAndToGoogleDriveURL() {

        List<BufferedImage> bufferedImageList = new ArrayList<>();
        bufferedImageList.add(new BufferedImage(1920, 1080, 5));

        Assertions.assertNotNull(googleDriveService.imageListToVideoAndToGoogleDriveURL(bufferedImageList, "test"));

    }

    @Test
    public void testImageListToVideoAndToGoogleDriveURLAndVideoName() {

        List<BufferedImage> bufferedImageList = new ArrayList<>();
        bufferedImageList.add(new BufferedImage(1920, 1080, 5));

        Assertions.assertNotNull(googleDriveService.imageListToVideoAndToGoogleDriveURLAndVideoName(bufferedImageList, "test"));

    }

    @Test
    public void testVideoFromGoogleDriveToImageListUsingURLAndVideoName() {

        List<BufferedImage> bufferedImageList = new ArrayList<>();

        Assertions.assertNotNull(googleDriveService.videoFromGoogleDriveToImageListUsingURLAndVideoName("https://drive.google.com/file/d/1QNjQFN0qWReBOShtIhI_av0320KzRi7o/view?usp=drivesdk", "testTest-14993920.mp4"));
        Assertions.assertEquals(googleDriveService.videoFromGoogleDriveToImageListUsingURLAndVideoName("https://drive.google.com/file/d/1QNjQFN0qWReBOShtIhI_av0320KzRi7o/view?usp=drivesdk", "testTest-14993920.mp4").getClass(), bufferedImageList.getClass());

    }

    @Test
    public void testVideoFromGoogleDriveToImageListUsingURL() {

        Pair<List<BufferedImage>, String> listStringPair = new Pair<>(new ArrayList<>(), "");

        Assertions.assertNotNull(googleDriveService.videoFromGoogleDriveToImageListUsingURL("https://drive.google.com/file/d/1QNjQFN0qWReBOShtIhI_av0320KzRi7o/view?usp=drivesdk"));
        Assertions.assertEquals(googleDriveService.videoFromGoogleDriveToImageListUsingURL("https://drive.google.com/file/d/1QNjQFN0qWReBOShtIhI_av0320KzRi7o/view?usp=drivesdk").getClass(), listStringPair.getClass());

    }

    @Test
    public void testDeleteUserVideos() {

        List<String> stringList = new ArrayList<>();
        stringList.add("https://drive.google.com/file/d/1QNj0320KzRi7o/view?usp=drivesdk");

        assertThrows(GoogleDriveDeleteException.class, () -> {
            googleDriveService.deleteUserVideos(stringList);
        });
    }
}
