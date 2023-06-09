package hr.projekt.secureVideoFile.controllers;

import hr.projekt.secureVideoFile.constants.PathParamConstants;
import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.*;
import hr.projekt.secureVideoFile.managers.FileConversionManager;
import hr.projekt.secureVideoFile.request.UserInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.ArrayList;
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
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("uploadFile endpoint entered");
        // Process the uploaded file here and convert it to an mp4 video

        String videoName = fileConversionManager.convertFileToSignedVideo(file, password, name);


        return ResponseEntity.ok(videoName);
    }

    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> retrieveFile(@RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent= fileConversionManager.convertSignedVideoToFile(name, password);


        return ResponseEntity.ok(fileContent.toString());
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @GetMapping(path = PathParamConstants.HELLO_TEST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> sayHello() {
        log.info("sayHello endpoint entered");


        return ResponseEntity.ok(new String("Hello world"));
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @PostMapping(path = PathParamConstants.UPLOAD_FILE_AND_GET_URL)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFileAndGetURL(@RequestPart("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws IOException {
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
                String saveVideoEndpoint = "http://localhost:3000/api/file/create";
                String URL = fileConversionManager.convertFileToSignedVideoAndGetURL(file, password, name);
                HttpPost httpPost = new HttpPost(saveVideoEndpoint);
                httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("url", URL));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("accessCode", authorizationHeader));

                httpPost.setEntity(new UrlEncodedFormEntity(params));
                httpClient.execute(httpPost);

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
    @CrossOrigin(origins = "*")
    public ResponseEntity<Pair<String, String>> uploadFileAndGetURLAndVideoName(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("uploadFileAndGetURL endpoint entered");

        Pair<String, String> urlAndVideoName = fileConversionManager.convertFileToSignedVideoAndGetURLAndVideoName(file, password, name);


        return ResponseEntity.ok(urlAndVideoName);
    }

    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE_FROM_URL_AND_VIDEO_NAME)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> retrieveFileFromURLAndVideoName(@RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("URL") String URL) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent= fileConversionManager.convertSignedVideoToFileUsingURLAndVideoName(name, password, URL);


        return ResponseEntity.ok(fileContent.toString());
    }


    @Operation(summary = "Convert video to file", description = "Convert video from drive to original file")
    @PostMapping(path = PathParamConstants.RETRIEVE_FILE_FROM_URL)
    @CrossOrigin(origins = "*")
    public ResponseEntity<StreamingResponseBody> retrieveFileFromURL(@RequestParam("password") String password, @RequestParam("URL") String URL, @RequestParam("name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws IOException {
        log.info("retrieveFile endpoint entered");

        String url = "http://localhost:3000/api/auth/get/me";
        String token = authorizationHeader.replace("Bearer ", "");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
                byte[] fileContent = fileConversionManager.convertSignedVideoToFileUsingURL(password, URL);

                StreamingResponseBody responseBody = outputStream -> {
                    outputStream.write(fileContent);
                    outputStream.flush();
                };

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + ".zip\"")
                        .body(responseBody);
            }
            else if (response.getStatusLine().getStatusCode() == 401) {
                return new ResponseEntity<StreamingResponseBody>(outputStream -> {}, HttpStatus.UNAUTHORIZED);
            }
            else {
                return new ResponseEntity<StreamingResponseBody>(outputStream -> {}, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Operation(summary = "Delete user videos", description = "Delete user videos using user info")
    @DeleteMapping(path = PathParamConstants.DELETE_USER_VIDEOS)
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> deleteUserVideos(@RequestBody @Valid List<UserInfoRequest> userInfoRequests, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws IOException {
        log.info("retrieveFile endpoint entered");

        System.out.println(authorizationHeader);


        String url = "http://localhost:3000/api/auth/get/me";
        String token = authorizationHeader.replace("Bearer ", "");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
                Boolean deletionCompleted = fileConversionManager.deleteUserVideos(userInfoRequests);
                if (deletionCompleted) {
                    return new ResponseEntity<String>("Successful deletion", HttpStatus.OK);
                }
                return new ResponseEntity<String>("Unsuccessful deletion", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else if (response.getStatusLine().getStatusCode() == 401) {
                return new ResponseEntity<String>("Unsuccessful authorization", HttpStatus.UNAUTHORIZED);            }
            else {
                return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);            }
        }
    }

    @ExceptionHandler(FileEncryptionException.class)
    public ResponseEntity<String> handleFileEncryptionException(FileEncryptionException ex) {
        String errorMessage = "Invalid file password";
        StatusCode statusCode = ex.getStatusCode();

        HttpStatus httpStatus;

        switch (statusCode) {
            case FILE_DECRYPTION_ERROR, FILE_ENCRYPTION_ERROR:
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return ResponseEntity.status(httpStatus).body(errorMessage);
    }

    @ExceptionHandler(GoogleDriveInitializationException.class)
    public ResponseEntity<String> handleGoogleDriveInitializationException(GoogleDriveInitializationException ex) {
        String errorMessage = "Google Drive initialization failed, check your credentials";
        StatusCode statusCode = ex.getStatusCode();

        HttpStatus httpStatus;

        switch (statusCode) {
            case GOOGLE_DRIVE_INITIALIZATION_ERROR:
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return ResponseEntity.status(httpStatus).body(errorMessage);
    }

    @ExceptionHandler(GoogleDriveUploadException.class)
    public ResponseEntity<String> handleGoogleDriveUploadException(GoogleDriveUploadException ex) {
        String errorMessage = "Failed to upload to Google Drive";
        StatusCode statusCode = ex.getStatusCode();

        HttpStatus httpStatus;

        switch (statusCode) {
            case GOOGLE_DRIVE_UPLOAD_ERROR:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return ResponseEntity.status(httpStatus).body(errorMessage);
    }

    @ExceptionHandler(GoogleDriveVideoDownloadException.class)
    public ResponseEntity<String> handleGoogleDriveVideoDownloadException(GoogleDriveVideoDownloadException ex) {
        String errorMessage = "Failed to download from Google drive, check URL and name of video";
        StatusCode statusCode = ex.getStatusCode();

        HttpStatus httpStatus;

        switch (statusCode) {
            case GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return ResponseEntity.status(httpStatus).body(errorMessage);
    }

    @ExceptionHandler(HttpHostConnectException.class)
    public ResponseEntity<String> handleHttpHostConnectException(HttpHostConnectException ex) {
        String errorMessage = "Connection to service was refused, check if service is up and running";


        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
    }

}
