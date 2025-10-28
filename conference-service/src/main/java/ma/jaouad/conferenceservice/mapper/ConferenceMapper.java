package ma.jaouad.conferenceservice.mapper;

import lombok.RequiredArgsConstructor;
import ma.jaouad.conferenceservice.dto.ConferenceDTO;
import ma.jaouad.conferenceservice.entity.Conference;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConferenceMapper {
    
    private final ReviewMapper reviewMapper;
    
    public ConferenceDTO toDTO(Conference conference) {
        if (conference == null) {
            return null;
        }
        return ConferenceDTO.builder()
                .id(conference.getId())
                .titre(conference.getTitre())
                .type(conference.getType())
                .date(conference.getDate())
                .duree(conference.getDuree())
                .nombreInscrits(conference.getNombreInscrits())
                .score(conference.getScore())
                .keynoteId(conference.getKeynoteId())
                .reviews(conference.getReviews() != null ? 
                        conference.getReviews().stream()
                                .map(reviewMapper::toDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }
    
    public Conference toEntity(ConferenceDTO dto) {
        if (dto == null) {
            return null;
        }
        return Conference.builder()
                .id(dto.getId())
                .titre(dto.getTitre())
                .type(dto.getType())
                .date(dto.getDate())
                .duree(dto.getDuree())
                .nombreInscrits(dto.getNombreInscrits())
                .score(dto.getScore())
                .keynoteId(dto.getKeynoteId())
                .build();
    }
}
