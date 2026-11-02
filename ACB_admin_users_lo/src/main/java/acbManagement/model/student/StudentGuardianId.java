package acbManagement.model.student;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentGuardianId implements Serializable {
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "guardian_id")
    private Long guardianId;
}
