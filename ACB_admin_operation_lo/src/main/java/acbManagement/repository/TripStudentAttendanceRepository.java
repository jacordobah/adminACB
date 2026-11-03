package acbManagement.repository;

import acbManagement.model.trip.TripStudentAttendance;
import acbManagement.model.trip.TripStudentAttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripStudentAttendanceRepository
        extends JpaRepository<TripStudentAttendance, TripStudentAttendanceId> {

    List<TripStudentAttendance> findAllByTrip_TripIdOrderByStudentNameSnapshot(Long tripId);

    List<TripStudentAttendance> findAllById_StudentIdOrderByTrip_TripDateDesc(Long studentId);

    Optional<TripStudentAttendance> findByTrip_TripIdAndId_StudentId(Long tripId, Long studentId);

    long countByTrip_TripIdAndStatusCode(Long tripId, String statusCode);
}
