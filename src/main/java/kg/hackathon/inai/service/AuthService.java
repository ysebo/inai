package kg.hackathon.inai.service;

import jakarta.transaction.Transactional;
import kg.hackathon.inai.dto.auth.AuthenticationRequest;
import kg.hackathon.inai.dto.auth.AuthenticationResponse;
import kg.hackathon.inai.dto.user.UserRequest;
import kg.hackathon.inai.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthenticationResponse register(AuthenticationRequest authLoginRequest);

    AuthenticationResponse registerAdmin(AuthenticationRequest authLoginRequest);

    AuthenticationResponse login(AuthenticationRequest authLoginRequest);

    User getUserFromToken(String token);
//    @Transactional
//    ResponseEntity<?> userRegister(UserRequest request);
//
//    AuthenticationResponse authenticate(AuthenticationRequest request);
//
//    ResponseEntity<?> adminRegister(UserRequest request);
}
