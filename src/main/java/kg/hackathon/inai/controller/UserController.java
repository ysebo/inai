package kg.hackathon.inai.controller;

import kg.hackathon.inai.service.OpenApiService;
import kg.hackathon.inai.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")

@AllArgsConstructor
public class UserController {
    private final OpenApiService aiApiService;
    @GetMapping("/openStart")
    public String startOpenApi() {
        return aiApiService.getResponse("Напиши мне петицию : Домогательство, на меня домогались в маршрутке и я не могла ничего сделать ?");
    }

}
