package kg.hackathon.inai.controller;

import jakarta.servlet.http.HttpSession;
import kg.hackathon.inai.entity.Petition;
import kg.hackathon.inai.exception.NotFoundException;
import kg.hackathon.inai.repository.PetitionRepository;
import kg.hackathon.inai.service.PetitionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
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
    public String stepOnePost() {
        return "redirect:/step-two";
    }

    @GetMapping("/step-two")
    public String stepTwo(@RequestParam("country") String country,
                          @RequestParam("region") String region,
                          @RequestParam("city") String city,
                          @RequestParam("email") String email,
                          HttpSession session){
        Long id = petitionService.add_one(country, region, city, email);
        session.setAttribute("id", id);
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
        petitionService.add_three(additionalInfo, (Long) session.getAttribute("id"));
        return "redirect:/step-four";
    }


    @GetMapping("/step-four")
    public String showConfirmationPage(HttpSession session, Model model) {
        Optional<Petition> petition = petitionRepository.findById((Long) session.getAttribute("id"));
        if(petition.isEmpty())
            throw new NotFoundException("Petition not found", HttpStatus.NOT_FOUND);
        model.addAttribute("country", petition.get().getCountry());
        model.addAttribute("region", petition.get().getRegion());
        model.addAttribute("city", petition.get().getCity());
        model.addAttribute("description", petition.get().getDescription());


        return "step-four";
    }

    @PostMapping("/likeToPetition/{id}")
    public void likeToPetition(@RequestHeader("Authorization") String token, @PathVariable Long id ){
        petitionService.likeToPetition(token, id);
    }
}
