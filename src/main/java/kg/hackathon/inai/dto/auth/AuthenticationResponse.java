package kg.hackathon.inai.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import kg.hackathon.inai.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
//    private UserResponse user;
    @JsonProperty("access_token")
    private String token;
//    @JsonProperty("refresh_token")
//    private String refreshToken;
}
