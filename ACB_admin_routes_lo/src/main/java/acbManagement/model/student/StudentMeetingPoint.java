package acbManagement.model.student;

import acbManagement.model.meetingPoint.MeetingPoint;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students_meeting_points")
@Getter
@Setter
@NoArgsConstructor
public class StudentMeetingPoint {
    @EmbeddedId
    private StudentMeetingPointId id;

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_point_id", nullable = false)
    private MeetingPoint meetingPoint;
}