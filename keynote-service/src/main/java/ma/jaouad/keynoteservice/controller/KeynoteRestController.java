package ma.jaouad.keynoteservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.jaouad.keynoteservice.dto.KeynoteDTO;
import ma.jaouad.keynoteservice.service.KeynoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keynotes")
@RequiredArgsConstructor
@Tag(name = "Keynote Management", description = "APIs pour gérer les keynotes")
public class KeynoteRestController {
    
    private final KeynoteService keynoteService;
    
    @PostMapping
    @Operation(summary = "Créer un nouveau keynote")
    public ResponseEntity<KeynoteDTO> createKeynote(@RequestBody KeynoteDTO keynoteDTO) {
        try {
            KeynoteDTO created = keynoteService.createKeynote(keynoteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un keynote par son ID")
    public ResponseEntity<KeynoteDTO> getKeynoteById(@PathVariable Long id) {
        try {
            KeynoteDTO keynote = keynoteService.getKeynoteById(id);
            return ResponseEntity.ok(keynote);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Obtenir tous les keynotes")
    public ResponseEntity<List<KeynoteDTO>> getAllKeynotes() {
        List<KeynoteDTO> keynotes = keynoteService.getAllKeynotes();
        return ResponseEntity.ok(keynotes);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un keynote")
    public ResponseEntity<KeynoteDTO> updateKeynote(@PathVariable Long id, @RequestBody KeynoteDTO keynoteDTO) {
        try {
            KeynoteDTO updated = keynoteService.updateKeynote(id, keynoteDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un keynote")
    public ResponseEntity<Void> deleteKeynote(@PathVariable Long id) {
        try {
            keynoteService.deleteKeynote(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
