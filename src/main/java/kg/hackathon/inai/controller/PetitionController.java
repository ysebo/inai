package kg.hackathon.inai.controller;

import jakarta.servlet.http.HttpSession;
import kg.hackathon.inai.dto.petition.PetitionAdd1;
import kg.hackathon.inai.entity.Petition;
import kg.hackathon.inai.repository.PetitionRepository;
import kg.hackathon.inai.service.PetitionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/petition")
@AllArgsConstructor
public class PetitionController {
    @Autowired
    private final PetitionService petitionService;
    private final PetitionRepository petitionRepository;
    @GetMapping("")
    public String stepOne(){
        return "step-one";
    }

    @PostMapping("")
    public String stepOne(@RequestParam("country") String country,
                          @RequestParam("region") String region,
                          @RequestParam("city") String city,
                          HttpSession session) {
        session.setAttribute("country", country);
        session.setAttribute("region", region);
        session.setAttribute("city", city);
        return "redirect:/step-two";
    }

    @GetMapping("/step-two")
    public String stepTwo(){
        return "step-two";
    }
    @PostMapping("/step-two")
    public String handleStepTwoForm(@RequestParam("selectedOption") String selectedOption,
                                    HttpSession session) {
        // Save the selected option to the session
        session.setAttribute("selectedOption", selectedOption);

        // Redirect to Step Three
        return "redirect:/step-three";
    }


    @GetMapping("/step-three")
    public String stepThree(){
        return "step-three";
    }
    @PostMapping("/step-three")
    public String stepThree(@RequestParam("additionalInfo") String additionalInfo,
                            HttpSession session) {
        session.setAttribute("additionalInfo", additionalInfo);
        return "redirect:/step-four";
    }

    @GetMapping("/step-four")
    public String showConfirmationPage(HttpSession session, Model model) {
        String country = (String) session.getAttribute("country");
        String region = (String) session.getAttribute("region");
        String city = (String) session.getAttribute("city");
        String selectedOption = (String) session.getAttribute("selectedOption");
        String additionalInfo = (String) session.getAttribute("additionalInfo");


        Petition petition = new Petition();
        petition.setCountry(country);
        petition.setRegion(region);
        petition.setCity(city);
        petition.setSelectedOption(selectedOption);
        petition.setAdditionalInfo(additionalInfo);
        petitionRepository.save(petition);

        model.addAttribute("country", country);
        model.addAttribute("region", region);
        model.addAttribute("city", city);
        model.addAttribute("selectedOption", selectedOption);
        model.addAttribute("additionalInfo", additionalInfo);

        return "step-four";
    }

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
