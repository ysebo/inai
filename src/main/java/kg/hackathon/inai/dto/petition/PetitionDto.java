package kg.hackathon.inai.dto.petition;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetitionDto {
    @NotEmpty(message = "Author is required")
    private String author;
    @NotEmpty(message = "The name is required")
    private String name;
    @NotEmpty(message = "The country is required")
    private String country;
    @NotEmpty(message = "The region is required")
    private String region;
    @NotEmpty(message = "The city is required")
    private String city;
    private String description;
}
