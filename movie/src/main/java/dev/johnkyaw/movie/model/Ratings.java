package dev.johnkyaw.movie.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ratings {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_sequence", allocationSize = 1)
    private Long id;
    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;
}
