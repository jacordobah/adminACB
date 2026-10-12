package acbManagement.model.guide;

import acbManagement.model.route.Route;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guide_check_ins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuideCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_in_id")
    private Long checkInId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "route_id", referencedColumnName = "route_id", nullable = false),
            @JoinColumn(name = "shift_code", referencedColumnName = "shift_code", nullable = false)
    })
    private Route route;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;


    @Column(name = "address", nullable = false, length = 250)
    private String address; // Dirección física del punto de encuentro de inicio

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid;
}
