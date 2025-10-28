package ma.jaouad.conferenceservice.service;

import lombok.RequiredArgsConstructor;
import ma.jaouad.conferenceservice.dto.ReviewDTO;
import ma.jaouad.conferenceservice.entity.Conference;
import ma.jaouad.conferenceservice.entity.Review;
import ma.jaouad.conferenceservice.mapper.ReviewMapper;
import ma.jaouad.conferenceservice.repository.ConferenceRepository;
import ma.jaouad.conferenceservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final ConferenceRepository conferenceRepository;
    private final ReviewMapper reviewMapper;
    
    public ReviewDTO createReview(Long conferenceId, ReviewDTO reviewDTO) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée avec l'id: " + conferenceId));
        
        if (reviewDTO.getStars() < 1 || reviewDTO.getStars() > 5) {
            throw new RuntimeException("Le nombre d'étoiles doit être entre 1 et 5");
        }
        
        Review review = reviewMapper.toEntity(reviewDTO);
        review.setConference(conference);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }
    
    public List<ReviewDTO> getReviewsByConferenceId(Long conferenceId) {
        return reviewRepository.findByConferenceId(conferenceId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review non trouvée avec l'id: " + id));
        return reviewMapper.toDTO(review);
    }
    
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review non trouvée avec l'id: " + id));
        
        if (reviewDTO.getStars() < 1 || reviewDTO.getStars() > 5) {
            throw new RuntimeException("Le nombre d'étoiles doit être entre 1 et 5");
        }
        
        existingReview.setDate(reviewDTO.getDate());
        existingReview.setTexte(reviewDTO.getTexte());
        existingReview.setStars(reviewDTO.getStars());
        
        Review updatedReview = reviewRepository.save(existingReview);
        return reviewMapper.toDTO(updatedReview);
    }
    
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review non trouvée avec l'id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
