package hr.projekt.secureVideoFile.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

@Log4j2
@Service
public class FileConversionServiceImpl implements FileConversionService{

    @Override
    public List<BufferedImage> convertFileToImageList(MultipartFile file, String password) {
        return null;
    }

    @Override
    public byte[] convertImageListToFile(List<BufferedImage> listOfImages, String password) {
        return new byte[0];
    }
}
