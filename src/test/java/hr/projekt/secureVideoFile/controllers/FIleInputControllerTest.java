package hr.projekt.secureVideoFile.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import hr.projekt.secureVideoFile.constants.PathParamConstants;
import hr.projekt.secureVideoFile.managers.FileConversionManager;
import hr.projekt.secureVideoFile.request.UserInfoRequest;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class FIleInputControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileConversionManager fileConversionManager;

    @InjectMocks
    private FIleInputController fileInputController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileInputController).build();
    }

    @Test
    public void testUploadFile() throws Exception {
        // Mock the conversion result
        String expectedVideoName = "sample_video.mp4";
        when(fileConversionManager.convertFileToSignedVideo(any(MultipartFile.class), anyString(), anyString()))
                .thenReturn(expectedVideoName);

        // Create a mock file to upload
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(multipart(PathParamConstants.UPLOAD_FILE)
                .file(file)
                .param("password", "sample_password")
                .param("name", "sample_name"));

        // Verify the response
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(expectedVideoName));

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertFileToSignedVideo(eq(file), eq("sample_password"), eq("sample_name"));
    }

    @Test
    public void testRetrieveFile() throws Exception {
        // Mock the conversion result
        String expectedContent = "Sample file content";
        when(fileConversionManager.convertSignedVideoToFile(anyString(), anyString()))
                .thenReturn(expectedContent.getBytes());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(post(PathParamConstants.RETRIEVE_FILE)
                .param("password", "sample_password")
                .param("name", "sample_name"));

        // Verify the response
        resultActions.andExpect(status().isOk());

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertSignedVideoToFile(eq("sample_name"), eq("sample_password"));
    }


    @Test
    public void testSayHello() throws Exception {
        // Perform the request
        ResultActions resultActions = mockMvc.perform(get(PathParamConstants.HELLO_TEST));

        // Verify the response
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testUploadFileAndGetURL() throws Exception {
        // Mock the conversion result
        String expectedURL = "https://example.com/sample-video.mp4";
        when(fileConversionManager.convertFileToSignedVideoAndGetURL(any(MultipartFile.class), anyString(), anyString()))
                .thenReturn(expectedURL);

        // Create a mock file to upload
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(
                multipart(PathParamConstants.UPLOAD_FILE_AND_GET_URL)
                        .file(file)
                        .param("password", "sample_password")
                        .param("name", "sample_name")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NDdhNGM5MDM2MzA1MWE0OWJmM2Y3ODEiLCJtYWlsIjoic2VjdXJldmlkZW9maWxlQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoibGV2cm9uZSIsInN0YXR1cyI6IkFjdGl2ZSIsImlhdCI6MTY4NTc3NzQ4MywiZXhwIjoxNjg4MzY5NDgzfQ.dhP4W2ub02pXMcvDOC9IYTcD55_3oL3Am8xEpmK6Z3U")
        );

        // Verify the response
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(expectedURL));

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertFileToSignedVideoAndGetURL(eq(file), eq("sample_password"), eq("sample_name"));
    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testUploadFileAndGetURLUnauthorized() throws Exception {

        // Create a mock file to upload
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(
                multipart(PathParamConstants.UPLOAD_FILE_AND_GET_URL)
                        .file(file)
                        .param("password", "sample_password")
                        .param("name", "sample_name")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer test")
        );

        // Verify the response
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unsuccessful authorization"));

    }


    @Test
    public void testUploadFileAndGetURLAndVideoName() throws Exception {
        // Mock the conversion result
        String expectedURL = "https://example.com/sample-video.mp4";
        String expectedVideoName = "sample-video.mp4";
        Pair<String, String> expectedPair = new Pair<>(expectedURL, expectedVideoName);
        when(fileConversionManager.convertFileToSignedVideoAndGetURLAndVideoName(any(MultipartFile.class), anyString(), anyString()))
                .thenReturn(expectedPair);

        // Create a mock file to upload
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());

        // Perform the request
        ResultActions resultActions = mockMvc.perform(multipart(PathParamConstants.UPLOAD_FILE_AND_GET_URL_AND_VIDEO_NAME)
                .file(file)
                .param("password", "sample_password")
                .param("name", "sample_name"));

        // Verify the response
        resultActions.andExpect(status().isOk());

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertFileToSignedVideoAndGetURLAndVideoName(eq(file), eq("sample_password"), eq("sample_name"));
    }



    @Test
    public void testRetrieveFileFromURLAndVideoName() throws Exception {
        // Mock the conversion result
        String expectedContent = "Sample file content";
        byte[] expectedBytes = expectedContent.getBytes();
        when(fileConversionManager.convertSignedVideoToFileUsingURLAndVideoName(anyString(), anyString(), anyString()))
                .thenReturn(expectedBytes);

        // Perform the request
        ResultActions resultActions = mockMvc.perform(post(PathParamConstants.RETRIEVE_FILE_FROM_URL_AND_VIDEO_NAME)
                .param("password", "sample_password")
                .param("name", "sample_name")
                .param("URL", "https://example.com/sample-video.mp4"));

        // Verify the response
        resultActions.andExpect(status().isOk());

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertSignedVideoToFileUsingURLAndVideoName(eq("sample_name"), eq("sample_password"), eq("https://example.com/sample-video.mp4"));
    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testRetrieveFileFromURL() throws Exception {
        // Mock the conversion result
        String expectedContent = "Sample file content";
        byte[] expectedBytes = expectedContent.getBytes();
        when(fileConversionManager.convertSignedVideoToFileUsingURL(anyString(), anyString()))
                .thenReturn(expectedBytes);

        // Perform the request
        ResultActions resultActions = mockMvc.perform(post(PathParamConstants.RETRIEVE_FILE_FROM_URL)
                .param("password", "sample_password")
                .param("URL", "https://example.com/sample-video.mp4")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NDdhNGM5MDM2MzA1MWE0OWJmM2Y3ODEiLCJtYWlsIjoic2VjdXJldmlkZW9maWxlQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoibGV2cm9uZSIsInN0YXR1cyI6IkFjdGl2ZSIsImlhdCI6MTY4NTc3NzQ4MywiZXhwIjoxNjg4MzY5NDgzfQ.dhP4W2ub02pXMcvDOC9IYTcD55_3oL3Am8xEpmK6Z3U"));


        // Verify the response
        resultActions.andExpect(status().isOk());

        // Verify the conversion method invocation
        verify(fileConversionManager, times(1)).convertSignedVideoToFileUsingURL(eq("sample_password"), eq("https://example.com/sample-video.mp4"));
    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testRetrieveFileFromURLUnathorized() throws Exception {

        // Perform the request
        ResultActions resultActions = mockMvc.perform(post(PathParamConstants.RETRIEVE_FILE_FROM_URL)
                .param("password", "sample_password")
                .param("URL", "https://example.com/sample-video.mp4")
                .header(HttpHeaders.AUTHORIZATION, "Bearer test"));


        // Verify the response
        resultActions.andExpect(status().isUnauthorized());

    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testDeleteUserVideos() throws Exception {
        // Prepare the request body
        List<UserInfoRequest> userInfoRequests = new ArrayList<>();
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setOwner("sample_user");
        userInfoRequest.setAccessCode("sample_video");
        userInfoRequests.add(userInfoRequest);

        // Mock the deletion result
        boolean deletionCompleted = true;
        when(fileConversionManager.deleteUserVideos(anyList()))
                .thenReturn(deletionCompleted);

        // Perform the request
        ResultActions resultActions = mockMvc.perform(delete(PathParamConstants.DELETE_USER_VIDEOS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(userInfoRequests))
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NDdhNGM5MDM2MzA1MWE0OWJmM2Y3ODEiLCJtYWlsIjoic2VjdXJldmlkZW9maWxlQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoibGV2cm9uZSIsInN0YXR1cyI6IkFjdGl2ZSIsImlhdCI6MTY4NTc3NzQ4MywiZXhwIjoxNjg4MzY5NDgzfQ.dhP4W2ub02pXMcvDOC9IYTcD55_3oL3Am8xEpmK6Z3U"));

        // Verify the response
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Successful deletion"));

        // Verify the deletion method invocation
        verify(fileConversionManager, times(1)).deleteUserVideos(eq(userInfoRequests));
    }

    @DisabledIf("#{systemProperties['CI'] == true}")
    @Test
    public void testDeleteUserVideosUnauthorized() throws Exception {
        // Prepare the request body
        List<UserInfoRequest> userInfoRequests = new ArrayList<>();
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setOwner("sample_user");
        userInfoRequest.setAccessCode("sample_video");
        userInfoRequests.add(userInfoRequest);


        // Perform the request
        ResultActions resultActions = mockMvc.perform(delete(PathParamConstants.DELETE_USER_VIDEOS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJson(userInfoRequests))
                .header(HttpHeaders.AUTHORIZATION, "Bearer test"));

        // Verify the response
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(content().string("Unsuccessful authorization"));

    }

    private String convertObjectToJson(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        objectMapper.writeValue(mockHttpOutputMessage.getBody(), object);
        return mockHttpOutputMessage.getBodyAsString();
    }

}