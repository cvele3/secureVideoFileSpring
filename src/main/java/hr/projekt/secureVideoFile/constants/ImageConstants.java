package hr.projekt.secureVideoFile.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageConstants {
    public static final Integer TARGET_HEIGHT = 1440;
    public static final Integer TARGET_WIDTH = 2560;
    public static final Integer PIXELS_PER_IMAGE = TARGET_HEIGHT * TARGET_WIDTH;


}
