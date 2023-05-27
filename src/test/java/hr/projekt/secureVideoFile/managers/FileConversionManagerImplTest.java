package hr.projekt.secureVideoFile.managers;

import static org.junit.jupiter.api.Assertions.*;

import hr.projekt.secureVideoFile.request.UserInfoRequest;
import hr.projekt.secureVideoFile.services.FileConversionService;
import hr.projekt.secureVideoFile.services.GoogleDriveService;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileConversionManagerImplTest {

    @Mock
    private FileConversionService fileConversionService;

    @Mock
    private GoogleDriveService googleDriveService;

    @InjectMocks
    private FileConversionManagerImpl fileConversionManager;

    public FileConversionManagerImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertFileToSignedVideo() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        String password = "sample_password";
        String name = "sample_name";

        // Mock the necessary methods
        when(fileConversionService.convertFileToImageList(any(MockMultipartFile.class), any(String.class), any(String.class)))
                .thenReturn(new Pair<>(null, null));
        when(googleDriveService.imageListToVideoAndToGoogleDrive(anyList(), any(String.class)))
                .thenReturn("sample_video.mp4");

        // Call the method to test
        fileConversionManager.convertFileToSignedVideo(file, password, name);

        // Verify the method invocations
        verify(fileConversionService, times(1)).convertFileToImageList(file, password, name);
        verify(googleDriveService, times(1)).imageListToVideoAndToGoogleDrive(null, null);
    }

    @Test
    public void testConvertFileToSignedVideoAndGetURL() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        String password = "sample_password";
        String name = "sample_name";
        List<BufferedImage> imageList = new ArrayList<>();
        String videoURL = "https://example.com/sample_video.mp4";

        // Mock the necessary methods
        when(fileConversionService.convertFileToImageList(any(MockMultipartFile.class), any(String.class), any(String.class)))
                .thenReturn(new Pair<>(imageList, null));
        when(googleDriveService.imageListToVideoAndToGoogleDriveURL(imageList, null)).thenReturn(videoURL);

        // Call the method to test
        fileConversionManager.convertFileToSignedVideoAndGetURL(file, password, name);

        // Verify the method invocations
        verify(fileConversionService, times(1)).convertFileToImageList(file, password, name);
        verify(googleDriveService, times(1)).imageListToVideoAndToGoogleDriveURL(imageList, null);
    }

    @Test
    public void testConvertFileToSignedVideoAndGetURLAndVideoName() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        String password = "sample_password";
        String name = "sample_name";
        List<BufferedImage> imageList = new ArrayList<>();
        String videoURL = "https://example.com/sample_video.mp4";
        String videoName = "sample_video.mp4";
        Pair<String, String> urlAndNamePair = new Pair<>(videoURL, videoName);

        // Mock the necessary methods
        when(fileConversionService.convertFileToImageList(any(MockMultipartFile.class), any(String.class), any(String.class)))
                .thenReturn(new Pair<>(imageList, null));
        when(googleDriveService.imageListToVideoAndToGoogleDriveURLAndVideoName(imageList, null))
                .thenReturn(urlAndNamePair);

        // Call the method to test
        fileConversionManager.convertFileToSignedVideoAndGetURLAndVideoName(file, password, name);

        // Verify the method invocations
        verify(fileConversionService, times(1)).convertFileToImageList(file, password, name);
        verify(googleDriveService, times(1)).imageListToVideoAndToGoogleDriveURLAndVideoName(imageList, null);
    }

    @Test
    public void testConvertSignedVideoToFileUsingURLAndVideoName() {
        String videoName = "sample_video.mp4";
        String password = "sample_password";
        String URL = "https://example.com/sample_video.mp4";
        List<BufferedImage> imageList = new ArrayList<>();

        // Mock the necessary methods
        when(googleDriveService.videoFromGoogleDriveToImageListUsingURLAndVideoName(URL, videoName))
                .thenReturn(imageList);
        when(fileConversionService.convertImageListToFile(imageList, password, videoName)).thenReturn(new byte[0]);

        // Call the method to test
        fileConversionManager.convertSignedVideoToFileUsingURLAndVideoName(videoName, password, URL);

        // Verify the method invocations
        verify(googleDriveService, times(1)).videoFromGoogleDriveToImageListUsingURLAndVideoName(URL, videoName);
        verify(fileConversionService, times(1)).convertImageListToFile(imageList, password, videoName);
    }

    @Test
    public void testConvertSignedVideoToFileUsingURL() {
        String password = "sample_password";
        String URL = "https://example.com/sample_video.mp4";
        List<BufferedImage> imageList = new ArrayList<>();
        String videoName = "sample_video.mp4";

        // Mock the necessary methods
        when(googleDriveService.videoFromGoogleDriveToImageListUsingURL(URL))
                .thenReturn(new Pair<>(imageList, videoName));
        when(fileConversionService.convertImageListToFile(imageList, password, videoName)).thenReturn(new byte[0]);

        // Call the method to test
        fileConversionManager.convertSignedVideoToFileUsingURL(password, URL);

        // Verify the method invocations
        verify(googleDriveService, times(1)).videoFromGoogleDriveToImageListUsingURL(URL);
        verify(fileConversionService, times(1)).convertImageListToFile(imageList, password, videoName);
    }

    @Test
    public void testDeleteUserVideos() {
        List<UserInfoRequest> userInfoRequests = new ArrayList<>();
        UserInfoRequest userInfoRequest1 = new UserInfoRequest();
        userInfoRequest1.setOwner("sample_user");
        userInfoRequest1.setAccessCode("sample_video");
        userInfoRequest1.setUrl("https://example.com/video1.mp4");

        UserInfoRequest userInfoRequest2 = new UserInfoRequest();
        userInfoRequest2.setOwner("sample_user");
        userInfoRequest2.setAccessCode("sample_video");
        userInfoRequest2.setUrl("https://example.com/video2.mp4");

        userInfoRequests.add(userInfoRequest1);
        userInfoRequests.add(userInfoRequest2);

        List<String> urls = new ArrayList<>();
        urls.add("https://example.com/video1.mp4");
        urls.add("https://example.com/video2.mp4");

        // Mock the necessary methods
        when(googleDriveService.deleteUserVideos(urls)).thenReturn(true);

        // Call the method to test
        fileConversionManager.deleteUserVideos(userInfoRequests);

        // Verify the method invocations
        verify(googleDriveService, times(1)).deleteUserVideos(urls);
    }

}