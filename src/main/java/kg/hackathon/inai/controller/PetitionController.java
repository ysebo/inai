package kg.hackathon.inai.controller;

import kg.hackathon.inai.dto.petition.PetitionAdd1;
import kg.hackathon.inai.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/petition")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    @PostMapping("/add/1")
    public void add1(@RequestHeader("Authorization") String token, @RequestBody PetitionAdd1 request){
        petitionService.add1(token, request);
    }

    @PostMapping("/add/2")
    public void add2(@RequestBody PetitionAdd1 request){
        petitionService.add2(request);
    }

    @PostMapping("/add/3")
    public void add3(@RequestBody PetitionAdd1 request){
        petitionService.add3(request);
    }

    @PostMapping("/likeToPetition/{id}")
    public void likeToPetition(@RequestHeader("Authorization") String token, @PathVariable Long id ){
        petitionService.likeToPetition(token, id);
    }
}
