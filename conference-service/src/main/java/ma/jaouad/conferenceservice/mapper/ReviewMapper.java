package ma.jaouad.conferenceservice.mapper;

import ma.jaouad.conferenceservice.dto.ReviewDTO;
import ma.jaouad.conferenceservice.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    
    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }
        return ReviewDTO.builder()
                .id(review.getId())
                .date(review.getDate())
                .texte(review.getTexte())
                .stars(review.getStars())
                .conferenceId(review.getConference() != null ? review.getConference().getId() : null)
                .build();
    }
    
    public Review toEntity(ReviewDTO dto) {
        if (dto == null) {
            return null;
        }
        return Review.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .texte(dto.getTexte())
                .stars(dto.getStars())
                .build();
    }
}
