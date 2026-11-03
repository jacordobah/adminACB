package acbManagement.repository;

import acbManagement.model.meetingPoint.MeetingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingPointRepository extends JpaRepository<MeetingPoint, Long> {

    List<MeetingPoint> findByRoute_RouteId(Long routeId);

    // Puntos de una ruta ordenados por distancia al colegio
    List<MeetingPoint> findByRoute_RouteIdOrderByDistanceAsc(Long routeId);

    // La pareja codigo + ruta es unica en la base
    Optional<MeetingPoint> findByRoute_RouteIdAndMeetingPointCode(Long routeId, String meetingPointCode);

    boolean existsByRoute_RouteIdAndMeetingPointCode(Long routeId, String meetingPointCode);
}
