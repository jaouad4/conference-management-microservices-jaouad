package ma.jaouad.conferenceservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.jaouad.conferenceservice.dto.ReviewDTO;
import ma.jaouad.conferenceservice.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences/{conferenceId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "APIs pour gérer les reviews des conférences")
public class ReviewRestController {
    
    private final ReviewService reviewService;
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle review pour une conférence")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long conferenceId, @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO created = reviewService.createReview(conferenceId, reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Obtenir toutes les reviews d'une conférence")
    public ResponseEntity<List<ReviewDTO>> getReviewsByConferenceId(@PathVariable Long conferenceId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByConferenceId(conferenceId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une review par son ID")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long conferenceId, @PathVariable Long id) {
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une review")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long conferenceId, @PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long conferenceId, @PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
