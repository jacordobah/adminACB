package acbManagement.model.school;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "school_code", nullable = false, unique = true, length = 45)
    private String schoolCode;

    @Column(nullable = false, length = 45)
    private String phone;

    @Column(name = "active", nullable = false)
    private Boolean active;
}
