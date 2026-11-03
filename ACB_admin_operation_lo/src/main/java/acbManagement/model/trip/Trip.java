package acbManagement.model.trip;

import acbManagement.model.event.ActionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "trip", uniqueConstraints = @UniqueConstraint(
        name = "uq_trip_route_date", columnNames = {"route_id", "trip_date"}))
@Getter
@Setter
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    // ID externo de Routes: no es una asociación JPA.
    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "route_code_snapshot", nullable = false, length = 4)
    private String routeCodeSnapshot;

    @Column(name = "shift_code_snapshot", nullable = false, length = 3)
    private String shiftCodeSnapshot;

    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    @Column(name = "school_name_snapshot", nullable = false, length = 150)
    private String schoolNameSnapshot;

    @Column(name = "scheduled_start_snapshot", nullable = false)
    private LocalTime scheduledStartSnapshot;

    @Column(name = "actual_start_at")
    private OffsetDateTime actualStartAt;

    @Column(name = "actual_end_at")
    private OffsetDateTime actualEndAt;

    // ID externo del punto inicial (punto 01) de Routes.
    @Column(name = "start_point_id", nullable = false)
    private Long startPointId;

    @Column(name = "start_point_code_snapshot", nullable = false, length = 4)
    private String startPointCodeSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completion_code", referencedColumnName = "code")
    private ActionCode completionCode;
}
