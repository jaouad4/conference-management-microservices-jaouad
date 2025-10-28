package ma.jaouad.conferenceservice.repository;

import ma.jaouad.conferenceservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByConferenceId(Long conferenceId);
}
