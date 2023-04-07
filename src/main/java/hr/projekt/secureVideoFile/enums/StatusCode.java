package hr.projekt.secureVideoFile.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {

    NO_ERROR(0),
    GOOGLE_DRIVE_VIDEO_DOWNLOAD_ERROR(100);

    private final int code;
}
