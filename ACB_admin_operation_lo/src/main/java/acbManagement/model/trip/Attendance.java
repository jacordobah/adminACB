package acbManagement.model.trip;

import acbManagement.model.student.Student;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @EmbeddedId
    private AttendanceId id = new AttendanceId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tripId")
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "is_present", nullable = false)
    private boolean isPresent = false;
    
    @Column(name = "roll_call", nullable = false)
    private LocalDateTime rollCall = LocalDateTime.now();
}

