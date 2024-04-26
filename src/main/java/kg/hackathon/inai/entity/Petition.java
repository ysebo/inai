package kg.hackathon.inai.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Petition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> signedPersons;

}
