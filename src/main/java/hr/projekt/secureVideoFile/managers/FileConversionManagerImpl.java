package hr.projekt.secureVideoFile.managers;

import hr.projekt.secureVideoFile.services.FileConversionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Log4j2
@Service
public class FileConversionManagerImpl implements FileConversionManager{


   private final FileConversionService fileConversionService;


   @Autowired
    public FileConversionManagerImpl(FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }


    @Override
    public byte[] convertFileToSignedVideo(MultipartFile file, String password) {

       log.info("Conversion of file to signed video.");

       log.info("\t1) Reading file into bytes.");
       log.info("\t2) Encrypting bytes.");

        return new byte[0];
    }

    @Override
    public File convertSignedVideoToFile(String videoGoogleDiskUrl, String password) {
        log.info("Getting file from uploaded video.");

        log.info("\t1) Downloading video from google disk..");

        log.info("\t2) Converting video to bytes..");

        log.info("\t3) Decrypting bytes..");

        log.info("\t4) Saving decrypted bytes to file..");


        return null;
    }
}
