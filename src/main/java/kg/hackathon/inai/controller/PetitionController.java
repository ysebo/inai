package kg.hackathon.inai.controller;

import jakarta.servlet.http.HttpSession;
import kg.hackathon.inai.entity.Petition;
import kg.hackathon.inai.exception.NotFoundException;
import kg.hackathon.inai.repository.PetitionRepository;
import kg.hackathon.inai.service.OpenApiService;
import kg.hackathon.inai.service.PetitionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class PetitionController {
    @Autowired
    private final PetitionService petitionService;
    private final PetitionRepository petitionRepository;
        private final OpenApiService aiApiService;
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
        if(Objects.equals(selectedOption, "Create a petition from scratch"))
            return "redirect:/step-three-2";
        else return "redirect:/step-three-1";
    }
    @GetMapping("/step-three-2")
    public String stepThree2(){return "step-three-2";}
    @PostMapping("/step-three-2")
    public String stepThree2(@RequestParam("additionalInfo") String additionalInfo,@RequestParam("name") String name, HttpSession session){
        petitionService.add_three(additionalInfo, name, (Long) session.getAttribute("id"));
        return "redirect:/step-four";}

    @GetMapping("/step-three-1")
    public String stepThree(){
        return "step-three-1";
    }
    @PostMapping("/step-three-1")
    public String stepThree(@RequestParam("additionalInfo") String additionalInfo,
                            @RequestParam("name") String name,
                            HttpSession session) {
        petitionService.add_three(additionalInfo, name, (Long) session.getAttribute("id"));
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
        model.addAttribute("name", petition.get().getName());
        model.addAttribute("description", petition.get().getDescription());
        String ans = aiApiService.getResponse("Проверь данную петицию по этим критериям оценивания: " +
                "Ясность цели: Петиция должна чётко формулировать свою цель или проблему, которую она решает.\n" +
                "Законодательная обоснованность: Петиция должна соответствовать действующему законодательству и не нарушать права других лиц.\n" +
                "Поддержка сообщества: Количество подписей и активность сообщества в поддержку петиции.\n" +
                "Доказательства и аргументация: Наличие обоснований, фактов и доказательств, подтверждающих заявленные в петиции аргументы.\n" +
                "Возможность реализации: Оценка реальности и практичности предложенных в петиции решений.\n" +
                "Использование ресурсов: Анализ, требуют ли предложенные меры значительных финансовых, временных или других ресурсов.\n" +
                "Оригинальность: Уникальность предложения по сравнению с уже существующими инициативами.\n" +
                "Социальное воздействие: Влияние реализации петиции на общество, включая потенциальные положительные и отрицательные последствия.\n" +
                "Прозрачность и открытость: Наличие чёткого понимания того, кто стоит за петицией и как будет использоваться собранная информация.\n" +
                "Качество изложения: Грамотность, логика изложения и структура текста петиции.\n" +
                "Эмоциональное воздействие: Способность петиции вызывать эмоциональный отклик у читателей, что может способствовать её распространению.\n" +
                "Потенциал для масштабирования: Возможность расширения предложенных решений на более широкие группы или регионы.\n" +
                "Инклюзивность: Учёт интересов различных групп населения, включая меньшинства и уязвимые слои.\n" +
                "Этичность: Соответствие этическим нормам и принципам.\n" +
                "Техническая осуществимость: Оценка возможностей реализации петиции с технической точки зрения, включая необходимые изменения в инфраструктуре или технологиях." +
                "и если она подходит под некоторые из этих критериев, напиши, что петиция \"Хорошая\", в противном случае, \"Плохая\". Повторяю еще раз, ты должен написать просто одно слово" +
                "Вот данная петиция: " + petition.get().getDescription());
        System.out.println(ans);
        if(Objects.equals(ans, "Хорошая"))
            return "step-four";
        else return "step-one";
    }

    @PostMapping("/likeToPetition/{id}")
    public void likeToPetition(@RequestHeader("Authorization") String token, @PathVariable Long id ){
        petitionService.likeToPetition(token, id);
    }
}
