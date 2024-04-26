package kg.hackathon.inai.controller;
import kg.hackathon.inai.dto.UserForm;
import kg.hackathon.inai.entity.User;
import kg.hackathon.inai.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import kg.hackathon.inai.dto.auth.AuthenticationRequest;
import kg.hackathon.inai.dto.auth.AuthenticationResponse;
import kg.hackathon.inai.dto.user.UserRequest;
import kg.hackathon.inai.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository repository;
    @GetMapping("" )
    public String showProducts(Model model){
        List<User> users = repository.findAll(Sort.by(Sort.Direction.DESC , "id"));
        model.addAttribute("user", users);
        return "loginForm";
    }


    @GetMapping("/login")
        public String loginForm(Model model) {
            UserForm loginForm  = new UserForm();
            model.addAttribute("loginForm" , loginForm);
            return "loginForm";
    }



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
