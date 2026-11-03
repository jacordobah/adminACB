package acbManagement.model.meetingPoint;


import acbManagement.model.route.Route;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "meeting_points",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_meeting_point_routes",
                columnNames = {"meeting_point_code", "route_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class MeetingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_point_id")
    private Long meetingPointId;

    @Column(name = "meeting_point_code", nullable = false, length = 4)
    private String meetingPointCode;

    @Column(nullable = false, length = 45)
    private String address;

    private Integer distance;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
}