package acbManagement.repository;

import acbManagement.model.student.StudentMeetingPoint;
import acbManagement.model.student.StudentMeetingPointId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentMeetingPointRepository
        extends JpaRepository<StudentMeetingPoint, StudentMeetingPointId> {

    List<StudentMeetingPoint> findById_StudentId(Long studentId);

    Optional<StudentMeetingPoint> findById_StudentIdAndId_ShiftCode(Long studentId, String shiftCode);

    // Quienes se suben en un punto, para el llamado a lista del guia
    List<StudentMeetingPoint> findByMeetingPoint_MeetingPointId(Long meetingPointId);

    List<StudentMeetingPoint> findByMeetingPoint_MeetingPointIdAndId_ShiftCode(
            Long meetingPointId, String shiftCode);

    List<StudentMeetingPoint> findByMeetingPoint_Route_RouteIdAndId_ShiftCode(
            Long routeId, String shiftCode);
}
