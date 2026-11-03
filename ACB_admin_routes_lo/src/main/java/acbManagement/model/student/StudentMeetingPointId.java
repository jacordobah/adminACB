package acbManagement.model.student;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentMeetingPointId implements Serializable {
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "shift_code", length = 3)
    private String shiftCode;
}