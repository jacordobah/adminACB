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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "major_fault")
@Getter
@Setter
@NoArgsConstructor
public class MajorFault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_fault_id")
    private Long majorFaultId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_guide_id", nullable = false)
    private TripGuideAttendance tripGuideAttendance;

    // Nulo para falta directa; único si deriva de un ciclo de cinco leves.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", unique = true)
    private GuideFaultCycle cycle;

    @Column(name = "origin", nullable = false, length = 20)
    private String origin; // DIRECT o FIVE_MINORS

    @Column(name = "status", nullable = false, length = 10)
    private String status = "OPEN";

    @Column(name = "managed_by_user_id")
    private Long managedByUserId;

    @Column(name = "opened_at", nullable = false)
    private OffsetDateTime openedAt;

    @Column(name = "notified_at")
    private OffsetDateTime notifiedAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @PrePersist
    void setOpenedAtIfMissing() {
        if (openedAt == null) openedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
