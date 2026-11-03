package acbManagement.repository;

import acbManagement.model.trip.TripGuideAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripGuideAttendanceRepository extends JpaRepository<TripGuideAttendance, Long> {
    List<TripGuideAttendance> findAllByTrip_TripIdOrderByAssignmentRole(Long tripId);

    Optional<TripGuideAttendance> findByTrip_TripIdAndAssignmentRole(Long tripId, String assignmentRole);

    List<TripGuideAttendance> findAllByPlannedGuideIdOrderByTrip_TripDateDesc(Long guideId);

    List<TripGuideAttendance> findAllByActualGuideIdOrderByTrip_TripDateDesc(Long guideId);
}
