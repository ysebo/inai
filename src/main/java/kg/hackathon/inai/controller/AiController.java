package kg.hackathon.inai.controller;

import kg.hackathon.inai.dto.ChatGPT.ChatGPTRequest;
import kg.hackathon.inai.dto.ChatGPT.ChatGPTResponse;
import kg.hackathon.inai.service.OpenApiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ai")

@AllArgsConstructor
public class AiController {

    private final OpenApiService aiApiService;
//    private final RestTemplate template = new RestTemplate();

    @GetMapping("/question")
    public String startOpenApi() {
        return aiApiService.getResponse("привет");
    }

}
