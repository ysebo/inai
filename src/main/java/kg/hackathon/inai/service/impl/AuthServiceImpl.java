package kg.hackathon.inai.service.impl;

import kg.hackathon.inai.config.JwtService;
import kg.hackathon.inai.dto.auth.AuthenticationRequest;
import kg.hackathon.inai.dto.auth.AuthenticationResponse;
import kg.hackathon.inai.entity.User;
import kg.hackathon.inai.enums.Role;
import kg.hackathon.inai.exception.BadCredentialsException;
import kg.hackathon.inai.exception.NotFoundException;
import kg.hackathon.inai.repository.UserRepository;
import kg.hackathon.inai.service.AuthService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
//    private final EmailSenderService emailSenderService;

    @Override
    public AuthenticationResponse register(AuthenticationRequest authLoginRequest) {
        if(userRepository.findByEmail(authLoginRequest.getEmail()).isPresent()) {
            throw new BadCredentialsException("User with email: " + authLoginRequest.getEmail() + " is already exist");
        }
        User user = new User();
        user.setEmail(authLoginRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(authLoginRequest.getPassword()));
        // todo change here and role
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        System.out.println("Marlen " + token);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    @Override
    public AuthenticationResponse registerAdmin(AuthenticationRequest authLoginRequest) {
        if(userRepository.findByEmail(authLoginRequest.getEmail()).isPresent()) {
            throw new BadCredentialsException("User with email: " + authLoginRequest.getEmail() + " is already exist");
        }
        User user = new User();
        user.setEmail(authLoginRequest.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(authLoginRequest.getPassword()));
        // todo change here and role
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest authLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginRequest.getEmail(),
                        authLoginRequest.getPassword()
                )
        );
        Optional<User> user = userRepository.findByEmail(authLoginRequest.getEmail());
        if(user.isEmpty()) {
            throw new NotFoundException("User with email \"" + authLoginRequest.getEmail() + "\" not found", HttpStatus.NOT_FOUND);
        }
        String token = jwtService.generateToken(user.get());
        AuthenticationResponse authLoginResponse = new AuthenticationResponse();
        authLoginResponse.setToken(token);
        return authLoginResponse;
    }
    @Override
    public User getUserFromToken(String token){

        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        if (chunks.length != 3)
            throw new BadCredentialsException("Wrong token!");
        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            byte[] decodedBytes = decoder.decode(chunks[1]);
            object = (JSONObject) jsonParser.parse(decodedBytes);
        } catch (ParseException e) {
            throw new BadCredentialsException("Wrong token!!");
        }
        return userRepository.findByEmail(String.valueOf(object.get("sub"))).orElseThrow(() ->
                new BadCredentialsException("Wrong token!!!"));
    }
}
