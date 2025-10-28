package ma.jaouad.conferenceservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.jaouad.conferenceservice.dto.ConferenceDTO;
import ma.jaouad.conferenceservice.entity.TypeConference;
import ma.jaouad.conferenceservice.service.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
@RequiredArgsConstructor
@Tag(name = "Conference Management", description = "APIs pour gérer les conférences")
public class ConferenceRestController {
    
    private final ConferenceService conferenceService;
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle conférence")
    public ResponseEntity<ConferenceDTO> createConference(@RequestBody ConferenceDTO conferenceDTO) {
        try {
            ConferenceDTO created = conferenceService.createConference(conferenceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une conférence par son ID")
    public ResponseEntity<ConferenceDTO> getConferenceById(@PathVariable Long id) {
        try {
            ConferenceDTO conference = conferenceService.getConferenceById(id);
            return ResponseEntity.ok(conference);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Obtenir toutes les conférences")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        List<ConferenceDTO> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Obtenir les conférences par type")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByType(@PathVariable TypeConference type) {
        List<ConferenceDTO> conferences = conferenceService.getConferencesByType(type);
        return ResponseEntity.ok(conferences);
    }
    
    @GetMapping("/keynote/{keynoteId}")
    @Operation(summary = "Obtenir les conférences d'un keynote")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByKeynoteId(@PathVariable Long keynoteId) {
        List<ConferenceDTO> conferences = conferenceService.getConferencesByKeynoteId(keynoteId);
        return ResponseEntity.ok(conferences);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une conférence")
    public ResponseEntity<ConferenceDTO> updateConference(@PathVariable Long id, @RequestBody ConferenceDTO conferenceDTO) {
        try {
            ConferenceDTO updated = conferenceService.updateConference(id, conferenceDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une conférence")
    public ResponseEntity<Void> deleteConference(@PathVariable Long id) {
        try {
            conferenceService.deleteConference(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
