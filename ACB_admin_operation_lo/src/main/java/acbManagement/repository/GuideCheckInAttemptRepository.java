package acbManagement.repository;

import acbManagement.model.guide.GuideCheckInAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuideCheckInAttemptRepository extends JpaRepository<GuideCheckInAttempt, Long> {
    List<GuideCheckInAttempt> findAllByTripGuideAttendance_TripGuideIdOrderByAttemptedAtDesc(
            Long tripGuideId);

    Optional<GuideCheckInAttempt> findFirstByTripGuideAttendance_TripGuideIdAndAcceptedTrueOrderByAttemptedAtAsc(
            Long tripGuideId);

    boolean existsBySourceAndSourceEventId(String source, String sourceEventId);
}
