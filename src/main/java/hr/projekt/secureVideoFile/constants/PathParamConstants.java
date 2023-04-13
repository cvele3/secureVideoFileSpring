package hr.projekt.secureVideoFile.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathParamConstants {

    public static final String UPLOAD_FILE = "/upload";

    public static final String RETRIEVE_FILE = "/retrieve";

    public static final String HELLO_TEST = "/hello";

    public static final String UPLOAD_FILE_AND_GET_URL = "/uploadAndGetURL";

    public static final String UPLOAD_FILE_AND_GET_URL_AND_VIDEO_NAME = "/uploadFile";

    public static final String RETRIEVE_FILE_FROM_URL_AND_VIDEO_NAME = "/retrieveFile";

    public static final String RETRIEVE_FILE_FROM_URL = "/retrieveUsingURL";

}
