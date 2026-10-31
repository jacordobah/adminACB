package acbManagement.model.student;

import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @Id
    private Long studentId;

    @Column(nullable = false, length = 20)
    private String grade;

    @Column(name = "school_shift", nullable = false, length = 50)
    private String schoolShift; // Jornada

    @Column(nullable = false)
    private String section;

    @Column(name = "school_id")
    private Long schoolId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentGuardian> guardians = new ArrayList<>();
}
