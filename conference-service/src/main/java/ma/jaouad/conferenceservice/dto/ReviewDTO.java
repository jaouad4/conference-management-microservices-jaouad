package ma.jaouad.conferenceservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private LocalDate date;
    private String texte;
    private Integer stars;
    private Long conferenceId;
}
