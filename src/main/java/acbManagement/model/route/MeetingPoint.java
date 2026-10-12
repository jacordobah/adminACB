package acbManagement.model.route;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meeting_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_point_id")
    private Long meetingPointId;

    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @Column(name = "distance", nullable = false)
    private int distance;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "route_id", referencedColumnName = "route_id", nullable = false),
            @JoinColumn(name = "shift_code", referencedColumnName = "shift_code", nullable = false)
    })
    private Route route;
}
