package ma.jaouad.conferenceservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.jaouad.conferenceservice.client.KeynoteRestClient;
import ma.jaouad.conferenceservice.dto.ConferenceDTO;
import ma.jaouad.conferenceservice.dto.KeynoteDTO;
import ma.jaouad.conferenceservice.entity.Conference;
import ma.jaouad.conferenceservice.entity.TypeConference;
import ma.jaouad.conferenceservice.mapper.ConferenceMapper;
import ma.jaouad.conferenceservice.repository.ConferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConferenceService {
    
    private final ConferenceRepository conferenceRepository;
    private final ConferenceMapper conferenceMapper;
    private final KeynoteRestClient keynoteRestClient;
    
    public ConferenceDTO createConference(ConferenceDTO conferenceDTO) {
        // Vérifier que le keynote existe
        try {
            KeynoteDTO keynote = keynoteRestClient.getKeynoteById(conferenceDTO.getKeynoteId());
            if (keynote == null) {
                throw new RuntimeException("Keynote non trouvé avec l'id: " + conferenceDTO.getKeynoteId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible de vérifier le keynote: " + e.getMessage());
        }
        
        Conference conference = conferenceMapper.toEntity(conferenceDTO);
        Conference savedConference = conferenceRepository.save(conference);
        ConferenceDTO result = conferenceMapper.toDTO(savedConference);
        
        // Enrichir avec les détails du keynote
        result.setKeynote(keynoteRestClient.getKeynoteById(savedConference.getKeynoteId()));
        return result;
    }
    
    public ConferenceDTO getConferenceById(Long id) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée avec l'id: " + id));
        ConferenceDTO dto = conferenceMapper.toDTO(conference);
        
        // Enrichir avec les détails du keynote
        dto.setKeynote(keynoteRestClient.getKeynoteById(conference.getKeynoteId()));
        return dto;
    }
    
    public List<ConferenceDTO> getAllConferences() {
        return conferenceRepository.findAll()
                .stream()
                .map(conference -> {
                    ConferenceDTO dto = conferenceMapper.toDTO(conference);
                    try {
                        log.debug("Fetching keynote with ID: {}", conference.getKeynoteId());
                        KeynoteDTO keynote = keynoteRestClient.getKeynoteById(conference.getKeynoteId());
                        log.debug("Keynote fetched successfully: {}", keynote);
                        dto.setKeynote(keynote);
                    } catch (Exception e) {
                        log.error("Failed to fetch keynote with ID {}: {}", conference.getKeynoteId(), e.getMessage(), e);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public List<ConferenceDTO> getConferencesByType(TypeConference type) {
        return conferenceRepository.findByType(type)
                .stream()
                .map(conference -> {
                    ConferenceDTO dto = conferenceMapper.toDTO(conference);
                    dto.setKeynote(keynoteRestClient.getKeynoteById(conference.getKeynoteId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public List<ConferenceDTO> getConferencesByKeynoteId(Long keynoteId) {
        return conferenceRepository.findByKeynoteId(keynoteId)
                .stream()
                .map(conference -> {
                    ConferenceDTO dto = conferenceMapper.toDTO(conference);
                    dto.setKeynote(keynoteRestClient.getKeynoteById(conference.getKeynoteId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public ConferenceDTO updateConference(Long id, ConferenceDTO conferenceDTO) {
        Conference existingConference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée avec l'id: " + id));
        
        // Vérifier que le keynote existe si changé
        if (!existingConference.getKeynoteId().equals(conferenceDTO.getKeynoteId())) {
            try {
                KeynoteDTO keynote = keynoteRestClient.getKeynoteById(conferenceDTO.getKeynoteId());
                if (keynote == null) {
                    throw new RuntimeException("Keynote non trouvé avec l'id: " + conferenceDTO.getKeynoteId());
                }
            } catch (Exception e) {
                throw new RuntimeException("Impossible de vérifier le keynote: " + e.getMessage());
            }
        }
        
        existingConference.setTitre(conferenceDTO.getTitre());
        existingConference.setType(conferenceDTO.getType());
        existingConference.setDate(conferenceDTO.getDate());
        existingConference.setDuree(conferenceDTO.getDuree());
        existingConference.setNombreInscrits(conferenceDTO.getNombreInscrits());
        existingConference.setScore(conferenceDTO.getScore());
        existingConference.setKeynoteId(conferenceDTO.getKeynoteId());
        
        Conference updatedConference = conferenceRepository.save(existingConference);
        ConferenceDTO result = conferenceMapper.toDTO(updatedConference);
        
        // Enrichir avec les détails du keynote
        result.setKeynote(keynoteRestClient.getKeynoteById(updatedConference.getKeynoteId()));
        return result;
    }
    
    public void deleteConference(Long id) {
        if (!conferenceRepository.existsById(id)) {
            throw new RuntimeException("Conférence non trouvée avec l'id: " + id);
        }
        conferenceRepository.deleteById(id);
    }
}
