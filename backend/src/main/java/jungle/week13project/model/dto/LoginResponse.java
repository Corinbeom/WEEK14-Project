package jungle.week13project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String userName;
    private String userEmail;
    private String userId;

    public LoginResponse(String token) {
    }
}
