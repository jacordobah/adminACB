package acbManagement.model.guide;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "guide_fault_cycle")
@Getter
@Setter
@NoArgsConstructor
public class GuideFaultCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_id")
    private Long cycleId;

    // ID externo de Users.
    @Column(name = "guide_id", nullable = false)
    private Long guideId;

    @Column(name = "status", nullable = false, length = 12)
    private String status = "OPEN";

    @Column(name = "opened_at", nullable = false)
    private OffsetDateTime openedAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @PrePersist
    void setOpenedAtIfMissing() {
        if (openedAt == null) openedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
