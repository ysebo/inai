package kg.hackathon.inai.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "The password is required")
    private String password;
}
