package acbManagement.model.guide;

import acbManagement.model.event.ActionCode;
import acbManagement.model.trip.TripGuideAttendance;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "guide_check_in_attempt", uniqueConstraints = @UniqueConstraint(
        name = "uq_check_in_source_event", columnNames = {"source", "source_event_id"}))
@Getter
@Setter
@NoArgsConstructor
public class GuideCheckInAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_in_attempt_id")
    private Long checkInAttemptId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_guide_id", nullable = false)
    private TripGuideAttendance tripGuideAttendance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_code", referencedColumnName = "code", nullable = false)
    private ActionCode operationCode; // Debe ser 523.

    @Column(name = "attempted_at", nullable = false)
    private OffsetDateTime attemptedAt;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "distance_to_start_m", precision = 10, scale = 2)
    private BigDecimal distanceToStartM;

    @Column(name = "allowed_radius_m", nullable = false, precision = 10, scale = 2)
    private BigDecimal allowedRadiusM;

    @Column(name = "within_radius", nullable = false)
    private boolean withinRadius;

    @Column(name = "within_time", nullable = false)
    private boolean withinTime;

    @Column(name = "accepted", nullable = false)
    private boolean accepted;

    @Column(name = "rejection_reason", length = 200)
    private String rejectionReason;

    @Column(name = "source", nullable = false, length = 30)
    private String source = "OPERATION";

    @Column(name = "source_event_id", length = 150)
    private String sourceEventId;

    @PrePersist
    void setAttemptedAtIfMissing() {
        if (attemptedAt == null) attemptedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
