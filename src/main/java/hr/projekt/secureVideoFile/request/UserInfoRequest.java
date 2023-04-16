package hr.projekt.secureVideoFile.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequest {

    private String name;
    private String url;
    private String accessCode;
    private Object owner;
}
