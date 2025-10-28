package ma.jaouad.keynoteservice.mapper;

import ma.jaouad.keynoteservice.dto.KeynoteDTO;
import ma.jaouad.keynoteservice.entity.Keynote;
import org.springframework.stereotype.Component;

@Component
public class KeynoteMapper {
    
    public KeynoteDTO toDTO(Keynote keynote) {
        if (keynote == null) {
            return null;
        }
        return KeynoteDTO.builder()
                .id(keynote.getId())
                .nom(keynote.getNom())
                .prenom(keynote.getPrenom())
                .email(keynote.getEmail())
                .fonction(keynote.getFonction())
                .build();
    }
    
    public Keynote toEntity(KeynoteDTO dto) {
        if (dto == null) {
            return null;
        }
        return Keynote.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .fonction(dto.getFonction())
                .build();
    }
}
