package acbManagement.model.guide;

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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "minor_fault", uniqueConstraints = @UniqueConstraint(
        name = "uq_minor_fault_cycle_sequence", columnNames = {"cycle_id", "sequence_in_cycle"}))
@Getter
@Setter
@NoArgsConstructor
public class MinorFault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minor_fault_id")
    private Long minorFaultId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cycle_id", nullable = false)
    private GuideFaultCycle cycle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_guide_id", nullable = false)
    private TripGuideAttendance tripGuideAttendance;

    @Column(name = "sequence_in_cycle", nullable = false)
    private Short sequenceInCycle;

    @Column(name = "reason", nullable = false, length = 200)
    private String reason;

    @Column(name = "status", nullable = false, length = 10)
    private String status = "PENDING";

    @Column(name = "notification_type", nullable = false, length = 20)
    private String notificationType;

    @Column(name = "managed_by_user_id")
    private Long managedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;

    @Column(name = "notified_at")
    private OffsetDateTime notifiedAt;

    @PrePersist
    void setRecordedAtIfMissing() {
        if (recordedAt == null) recordedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
