package hr.projekt.secureVideoFile.controllers;

import hr.projekt.secureVideoFile.constants.PathParamConstants;
import hr.projekt.secureVideoFile.managers.FileConversionManager;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) throws IOException {
        log.info("uploadFile endpoint entered");
        // Process the uploaded file here and convert it to an mp4 video
        System.out.println(password);
        System.out.println(file.getContentType());


        return ResponseEntity.ok("SIUUU");
    }

    @Operation(summary = "Convert input file", description = "Convert input file to video using password based encryption")
    @GetMapping(path = "/hello")
    public ResponseEntity<String> sayHello() {
        log.info("sayHello endpoint entered");


        return ResponseEntity.ok(new String("Hello world"));
    }

}
