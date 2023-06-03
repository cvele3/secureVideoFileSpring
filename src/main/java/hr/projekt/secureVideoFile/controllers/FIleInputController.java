package hr.projekt.secureVideoFile.controllers;

import hr.projekt.secureVideoFile.constants.PathParamConstants;
import hr.projekt.secureVideoFile.managers.FileConversionManager;
import hr.projekt.secureVideoFile.request.UserInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@Validated
public class FIleInputController {

    private final FileConversionManager fileConversionManager;

    @Autowired
    public FIleInputController(FileConversionManager fileConversionManager) {
        this.fileConversionManager = fileConversionManager;
    }


    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @PostMapping(path = PathParamConstants.UPLOAD_FILE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("uploadFile endpoint entered");
        // Process the uploaded file here and convert it to an mp4 video

        String videoName = fileConversionManager.convertFileToSignedVideo(file, password, name);


        return ResponseEntity.ok(videoName);
    }

    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE)
    public ResponseEntity<String> retrieveFile(@RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent= fileConversionManager.convertSignedVideoToFile(name, password);


        return ResponseEntity.ok(fileContent.toString());
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @GetMapping(path = PathParamConstants.HELLO_TEST)
    public ResponseEntity<String> sayHello() {
        log.info("sayHello endpoint entered");


        return ResponseEntity.ok(new String("Hello world"));
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @PostMapping(path = PathParamConstants.UPLOAD_FILE_AND_GET_URL)
    public ResponseEntity<String> uploadFileAndGetURL(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws IOException {
        log.info("uploadFileAndGetURL endpoint entered");

        System.out.println(authorizationHeader);


        String url = "http://localhost:3000/api/auth/get/me";
        String token = authorizationHeader.replace("Bearer ", "");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
                String URL = fileConversionManager.convertFileToSignedVideoAndGetURL(file, password, name);
                return ResponseEntity.ok(URL);
            }
            else if (response.getStatusLine().getStatusCode() == 401) {
                return new ResponseEntity<String>("Unsuccessful authorization", HttpStatus.UNAUTHORIZED);
            }
            else {
                return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @PostMapping(path = PathParamConstants.UPLOAD_FILE_AND_GET_URL_AND_VIDEO_NAME)
    public ResponseEntity<Pair<String, String>> uploadFileAndGetURLAndVideoName(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("uploadFileAndGetURL endpoint entered");

        Pair<String, String> urlAndVideoName = fileConversionManager.convertFileToSignedVideoAndGetURLAndVideoName(file, password, name);


        return ResponseEntity.ok(urlAndVideoName);
    }

    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE_FROM_URL_AND_VIDEO_NAME)
    public ResponseEntity<String> retrieveFileFromURLAndVideoName(@RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("URL") String URL) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent= fileConversionManager.convertSignedVideoToFileUsingURLAndVideoName(name, password, URL);


        return ResponseEntity.ok(fileContent.toString());
    }


    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE_FROM_URL)
    public ResponseEntity<StreamingResponseBody> retrieveFileFromURL(@RequestParam("password") String password, @RequestParam("URL") String URL) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent = fileConversionManager.convertSignedVideoToFileUsingURL(password, URL);

        StreamingResponseBody responseBody = outputStream -> {
            outputStream.write(fileContent);
            outputStream.flush();
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.bin\"")
                .body(responseBody);
    }


    @Operation(summary = "Delete user videos", description = "Delete user videos using user info")
    @DeleteMapping(path = PathParamConstants.DELETE_USER_VIDEOS)
    public ResponseEntity<Boolean> deleteUserVideos(@RequestBody @Valid List<UserInfoRequest> userInfoRequests) {
        log.info("retrieveFile endpoint entered");

        Boolean deletionCompleted = fileConversionManager.deleteUserVideos(userInfoRequests);

        return ResponseEntity.ok(deletionCompleted);
    }
}
