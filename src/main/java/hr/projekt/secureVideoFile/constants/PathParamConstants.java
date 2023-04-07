package hr.projekt.secureVideoFile.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathParamConstants {

    public static final String UPLOAD_FILE = "/upload";

    public static final String RETRIEVE_FILE = "/retrieve";
}
