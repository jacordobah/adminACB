package acbManagement.model.trip;

import acbManagement.model.route.Route;
import acbManagement.model.route.ShiftCode;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "route_id", referencedColumnName = "route_id", nullable = false),
            @JoinColumn(name = "shift_code", referencedColumnName = "shift_code", nullable = false)
    })
    private Route route;


    @Enumerated(EnumType.STRING)
    @Column(name = "trip_code", nullable = false)
    private ShiftCode tripCode;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Operation> operations;


    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;
}

