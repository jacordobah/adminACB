package acbManagement.model.student;

import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @Column(name = "grade", nullable = false, length = 20)
    private String grade;

    @Column(name = "school_shift", nullable = false, length = 50)
    private String schoolShift; // Jornada 

    // Aquí se mapeará la relación con el MeetingPoint de la ruta.
    // Usamos el ID primitivo por ahora para mantener el compilador limpio hasta crear la otra entidad.
    @Column(name = "meeting_point_id", nullable = false)
    private Long meetingPointId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentGuardian> guardians;
}
