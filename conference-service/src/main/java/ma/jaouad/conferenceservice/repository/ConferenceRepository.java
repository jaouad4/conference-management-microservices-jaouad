package ma.jaouad.conferenceservice.repository;

import ma.jaouad.conferenceservice.entity.Conference;
import ma.jaouad.conferenceservice.entity.TypeConference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByType(TypeConference type);
    List<Conference> findByKeynoteId(Long keynoteId);
    List<Conference> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
