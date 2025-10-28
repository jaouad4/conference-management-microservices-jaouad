package ma.jaouad.conferenceservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeynoteDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
