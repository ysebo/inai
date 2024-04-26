package kg.hackathon.inai.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Petition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String country;
    private String region;
    private String city;
    @Column(columnDefinition = "text")
    private String description;
    private LocalDateTime createdDate;
    private Integer signCount;


    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> signedPersons;

}
