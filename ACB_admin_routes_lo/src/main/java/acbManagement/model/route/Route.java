package acbManagement.model.route;
import acbManagement.model.school.School;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(
        name = "routes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_route_shift_code",
                columnNames = {"shift_code", "route_code"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "shift_code", nullable = false, length = 3)
    private String shiftCode; // "541" o "542"

    @Column(name = "route_code", nullable = false, length = 4)
    private String routeCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "scheduled_start_time", nullable = false)
    private LocalTime scheduledStartTime;

    @Column(name = "guide_main_id", nullable = false)
    private Long guideMainId; // ID componente de Users

    @Column(name = "guide_main_name", length = 150)
    private String guideMainName;

    @Column(name = "guide_support_id")
    private Long guideSupportId; // ID componente de Users

    @Column(name = "guide_support_name", length = 150)
    private String guideSupportName;

    private Boolean active;
}
