package hr.projekt.secureVideoFile.controllers;

import hr.projekt.secureVideoFile.constants.PathParamConstants;
import hr.projekt.secureVideoFile.managers.FileConversionManager;
import io.swagger.v3.oas.annotations.Operation;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    public ResponseEntity<String> uploadFileAndGetURL(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("name") String name) throws IOException {
        log.info("uploadFileAndGetURL endpoint entered");

        String URL = fileConversionManager.convertFileToSignedVideoAndGetURL(file, password, name);


        return ResponseEntity.ok(URL);
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
    public ResponseEntity<String> retrieveFileFromURL(@RequestParam("password") String password, @RequestParam("URL") String URL) throws IOException {
        log.info("retrieveFile endpoint entered");

        byte[] fileContent= fileConversionManager.convertSignedVideoToFileUsingURL(password, URL);


        return ResponseEntity.ok(fileContent.toString());
    }
}
