package acbManagement.model.student;

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
    private Long studentId;
    private Long guardianId;
}
