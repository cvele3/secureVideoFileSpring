package hr.projekt.secureVideoFile.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoConstants {
    public static final String TMP_STORAGE = "tmpVideoStorage/";

    public static final String MIME_TYPE = "video/mp4";

    public static final String VIDEO_FORMAT = ".mp4";
}
