package acbManagement.model.student;

import acbManagement.model.route.Route;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @Column(name = "student_id")
    private Long studentId; // ID componente de Users; no se genera en componente Rutas.

    @Column(name = "name", length = 150)
    private String name;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
}