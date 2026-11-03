package acbManagement.model.trip;

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

import java.time.OffsetDateTime;

@Entity
@Table(name = "trip_guide_attendance", uniqueConstraints = @UniqueConstraint(
        name = "uq_trip_guide_role", columnNames = {"trip_id", "assignment_role"}))
@Getter
@Setter
@NoArgsConstructor
public class TripGuideAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_guide_id")
    private Long tripGuideId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "assignment_role", nullable = false, length = 10)
    private String assignmentRole; // MAIN o SUPPORT

    // IDs externos de Users.
    @Column(name = "planned_guide_id", nullable = false)
    private Long plannedGuideId;

    @Column(name = "planned_guide_name_snapshot", nullable = false, length = 150)
    private String plannedGuideNameSnapshot;

    @Column(name = "actual_guide_id")
    private Long actualGuideId;

    @Column(name = "actual_guide_name_snapshot", length = 150)
    private String actualGuideNameSnapshot;

    @Column(name = "arrived_at")
    private OffsetDateTime arrivedAt;

    @Column(name = "substitution_reason")
    private String substitutionReason;

    @Column(name = "replacement_recorded_by_user_id")
    private Long replacementRecordedByUserId;

    @Column(name = "replacement_recorded_at")
    private OffsetDateTime replacementRecordedAt;
}
