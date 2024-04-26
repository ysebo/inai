package kg.hackathon.inai.repository;

import kg.hackathon.inai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
