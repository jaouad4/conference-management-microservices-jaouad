package ma.jaouad.keynoteservice.service;

import lombok.RequiredArgsConstructor;
import ma.jaouad.keynoteservice.dto.KeynoteDTO;
import ma.jaouad.keynoteservice.entity.Keynote;
import ma.jaouad.keynoteservice.mapper.KeynoteMapper;
import ma.jaouad.keynoteservice.repository.KeynoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KeynoteService {
    
    private final KeynoteRepository keynoteRepository;
    private final KeynoteMapper keynoteMapper;
    
    public KeynoteDTO createKeynote(KeynoteDTO keynoteDTO) {
        if (keynoteRepository.existsByEmail(keynoteDTO.getEmail())) {
            throw new RuntimeException("Email déjà existant: " + keynoteDTO.getEmail());
        }
        Keynote keynote = keynoteMapper.toEntity(keynoteDTO);
        Keynote savedKeynote = keynoteRepository.save(keynote);
        return keynoteMapper.toDTO(savedKeynote);
    }
    
    public KeynoteDTO getKeynoteById(Long id) {
        Keynote keynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keynote non trouvé avec l'id: " + id));
        return keynoteMapper.toDTO(keynote);
    }
    
    public List<KeynoteDTO> getAllKeynotes() {
        return keynoteRepository.findAll()
                .stream()
                .map(keynoteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public KeynoteDTO updateKeynote(Long id, KeynoteDTO keynoteDTO) {
        Keynote existingKeynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keynote non trouvé avec l'id: " + id));
        
        // Vérifier si l'email est déjà utilisé par un autre keynote
        if (!existingKeynote.getEmail().equals(keynoteDTO.getEmail()) 
                && keynoteRepository.existsByEmail(keynoteDTO.getEmail())) {
            throw new RuntimeException("Email déjà existant: " + keynoteDTO.getEmail());
        }
        
        existingKeynote.setNom(keynoteDTO.getNom());
        existingKeynote.setPrenom(keynoteDTO.getPrenom());
        existingKeynote.setEmail(keynoteDTO.getEmail());
        existingKeynote.setFonction(keynoteDTO.getFonction());
        
        Keynote updatedKeynote = keynoteRepository.save(existingKeynote);
        return keynoteMapper.toDTO(updatedKeynote);
    }
    
    public void deleteKeynote(Long id) {
        if (!keynoteRepository.existsById(id)) {
            throw new RuntimeException("Keynote non trouvé avec l'id: " + id);
        }
        keynoteRepository.deleteById(id);
    }
}
