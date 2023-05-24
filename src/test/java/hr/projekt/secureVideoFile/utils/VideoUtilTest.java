package hr.projekt.secureVideoFile.utils;

import hr.projekt.secureVideoFile.exceptions.FileManipulationException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class VideoUtilTest {

    private String path = "path/that/doesn't/exists";

    @Test
    void imagesToVideo() {

        assertThrows(NullPointerException.class, () -> VideoUtil.imagesToVideo(null, null));
    }

    @Test
    void videoToImages() {

        assertThrows(FileNotFoundException.class, () -> VideoUtil.videoToImages(path));
    }
}