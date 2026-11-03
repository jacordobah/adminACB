package acbManagement.repository;

import acbManagement.model.trip.TripEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripEventRepository extends JpaRepository<TripEvent, Long> {
    List<TripEvent> findAllByTrip_TripIdOrderByOccurredAtDesc(Long tripId);

    List<TripEvent> findAllByReportedByUserIdOrderByOccurredAtDesc(Long userId);

    Optional<TripEvent> findBySourceAndSourceEventId(String source, String sourceEventId);
}
