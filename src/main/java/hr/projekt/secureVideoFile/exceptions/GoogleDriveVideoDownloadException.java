package hr.projekt.secureVideoFile.exceptions;

import hr.projekt.secureVideoFile.enums.StatusCode;
import lombok.Getter;

@Getter
public class GoogleDriveVideoDownloadException  extends RuntimeException{

    private final StatusCode statusCode;
    private final String debugMessage;

    public GoogleDriveVideoDownloadException(StatusCode statusCode, String debugMessage) {
        this.statusCode = statusCode;
        this.debugMessage = debugMessage;
    }
}
