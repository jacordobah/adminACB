package acbManagement.model.school;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "school_code", nullable = false, unique = true, length = 50)
    private String schoolCode;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "active", nullable = false)
    private boolean isActive = true; // Flag para el borrado lógico del colegio
}
