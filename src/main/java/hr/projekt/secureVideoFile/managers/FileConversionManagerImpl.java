package hr.projekt.secureVideoFile.managers;

import hr.projekt.secureVideoFile.services.FileConversionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class FileConversionManagerImpl implements FileConversionManager{


   private final FileConversionService fileConversionService;


   @Autowired
    public FileConversionManagerImpl(FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }

    @Override
    public byte[] convertFileToImage(MultipartFile file, String password) {
       log.info("1. Starting conversion of user file");


       byte[] userVideo = null;



        return userVideo;
    }
}
