package acbManagement.model.student;

import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "guardians")
@PrimaryKeyJoinColumn(name = "guardian_id") // Une la PK de Guardian con la de User
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guardian extends User {

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentGuardian> students;
}

