package acbManagement.model.student;

import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.ser.impl.UnknownSerializer;

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
    @JoinColumn(name = "guardian_id", referencedColumnName = "user_id")
    private User guardian;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false)
    private Relationship relationship;

    @Column(name = "is_primary", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean isPrimary = false;
}
