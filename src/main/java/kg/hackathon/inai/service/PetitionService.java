package kg.hackathon.inai.service;

import kg.hackathon.inai.dto.petition.PetitionAdd1;
import org.springframework.ui.Model;

public interface PetitionService {
    void add1(String token, PetitionAdd1 request);

    void add2(PetitionAdd1 request);

    void add3(PetitionAdd1 request);

    void likeToPetition(String token, Long id);

    Long add_one(String country, String region, String city, String email);

    void add_three(String additionalInfo, String name, Long id);

}
