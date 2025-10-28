package ma.jaouad.conferenceservice.dto;

import lombok.*;
import ma.jaouad.conferenceservice.entity.TypeConference;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceDTO {
    private Long id;
    private String titre;
    private TypeConference type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
    private Long keynoteId;
    private KeynoteDTO keynote;
    private List<ReviewDTO> reviews;
}
