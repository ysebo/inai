package kg.hackathon.inai.repository;

import kg.hackathon.inai.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}
