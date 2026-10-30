package acbManagement.model.student;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students_guardians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGuardian {

    @EmbeddedId
    private StudentGuardianId id = new StudentGuardianId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId") // Mapea el studentId de la llave compuesta
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guardianId") // Mapea el guardianId de la llave compuesta
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false)
    private Relationship relationship;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;
}
