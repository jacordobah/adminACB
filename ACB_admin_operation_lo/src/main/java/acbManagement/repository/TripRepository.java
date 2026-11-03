package acbManagement.repository;

import acbManagement.model.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByRouteIdAndTripDate(Long routeId, LocalDate tripDate);

    List<Trip> findAllByTripDateOrderByRouteCodeSnapshot(LocalDate tripDate);

    List<Trip> findAllByRouteIdOrderByTripDateDesc(Long routeId);
}
