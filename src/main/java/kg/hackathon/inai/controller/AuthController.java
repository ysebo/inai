package kg.hackathon.inai.controller;

import kg.hackathon.inai.dto.auth.AuthenticationRequest;
import kg.hackathon.inai.dto.auth.AuthenticationResponse;
import kg.hackathon.inai.dto.user.UserRequest;
import kg.hackathon.inai.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthenticationResponse userRegister(@RequestBody AuthenticationRequest request) {
        return authService.register(request);
    }

    //авторизация
    @PostMapping("/login")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authService.login(request);
    }
    @PostMapping("/register/admin")
    public AuthenticationResponse adminRegister(@RequestBody AuthenticationRequest request) {
        return authService.registerAdmin(request);
    }


}
