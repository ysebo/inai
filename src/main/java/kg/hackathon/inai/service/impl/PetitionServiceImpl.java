package kg.hackathon.inai.service.impl;

import com.amazonaws.services.apigateway.model.Op;
import kg.hackathon.inai.dto.petition.PetitionAdd1;
import kg.hackathon.inai.entity.Petition;
import kg.hackathon.inai.entity.User;
import kg.hackathon.inai.exception.BadCredentialsException;
import kg.hackathon.inai.exception.BadRequestException;
import kg.hackathon.inai.exception.NotFoundException;
import kg.hackathon.inai.repository.PetitionRepository;
import kg.hackathon.inai.service.AuthService;
import kg.hackathon.inai.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {

    private final PetitionRepository petitionRepository;
    private final AuthService authService;

    @Override
    public void add1(String token, PetitionAdd1 request) {
        User user = authService.getUserFromToken(token);
        if(user == null)
            throw new BadCredentialsException("User not found!!!");
        Petition petition = new Petition();
        petition.setAuthor(user.getEmail());
        petition.setCreatedDate(LocalDateTime.now());
        petition.setSignCount(0);
        if(request.getField().isEmpty())
            throw new BadRequestException("Field shouldn't be null!");
        petition.setRegion(request.getField());
        petitionRepository.save(petition);
    }

    @Override
    public void add2(PetitionAdd1 request) {
        Petition petition = petitionRepository.findAll().get(petitionRepository.findAll().size() - 1);
        petition.setName(request.getField());
        petitionRepository.save(petition);
//        System.out.println(petition.getId());
    }

    @Override
    public void add3(PetitionAdd1 request) {
        Petition petition = petitionRepository.findAll().get(petitionRepository.findAll().size() - 1);
        petition.setDescription(request.getField());
        petitionRepository.save(petition);
//        System.out.println(petition.getId());
    }

    @Override
    public void likeToPetition(String token, Long id) {
        User user = authService.getUserFromToken(token);
        if(user == null)
            throw new BadCredentialsException("User not found!!!");
        Optional<Petition> petition = petitionRepository.findById(id);
        if(petition.isEmpty())
            throw new NotFoundException("Petition not found!!", HttpStatus.NOT_FOUND);
        if (!petition.get().getSignedPersons().contains(user)){
            petition.get().getSignedPersons().add(user);
            petition.get().setSignCount(petition.get().getSignCount() + 1);
            petitionRepository.save(petition.get());
        }
        else {
            petition.get().getSignedPersons().remove(user);
            petition.get().setSignCount(petition.get().getSignCount() - 1);
            petitionRepository.save(petition.get());
        }
    }

    @Override
    public Long add_one(String country, String region, String city, String email) {
        Petition petition = new Petition();
        petition.setAuthor(email);
        petition.setRegion(region);
        petition.setCity(city);
        petition.setCountry(country);
        petition.setCreatedDate(LocalDateTime.now());
        petition.setSignCount(0);
        return petitionRepository.saveAndFlush(petition).getId();
    }

    @Override
    public void add_three(String additionalInfo, Long id) {
        Optional<Petition> petition = petitionRepository.findById(id);
        if(petition.isEmpty())
            throw new NotFoundException("Petition not found", HttpStatus.NOT_FOUND);
        petition.get().setDescription(additionalInfo);
        petitionRepository.save(petition.get());
    }

}
