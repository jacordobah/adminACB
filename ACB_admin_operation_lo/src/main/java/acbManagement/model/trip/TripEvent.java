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
@Table(name = "trip_event", uniqueConstraints = @UniqueConstraint(
        name = "uq_trip_event_source_event", columnNames = {"source", "source_event_id"}))
@Getter
@Setter
@NoArgsConstructor
public class TripEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_event_id")
    private Long tripEventId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_code", referencedColumnName = "code", nullable = false)
    private ActionCode operationCode;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "source", nullable = false, length = 30)
    private String source;

    @Column(name = "source_event_id", length = 150)
    private String sourceEventId;

    // ID externo obtenido de la identidad autenticada; nulo en eventos automáticos.
    @Column(name = "reported_by_user_id")
    private Long reportedByUserId;

    @Column(name = "reporter_type", nullable = false, length = 10)
    private String reporterType; // GUIDE, ADMIN o SYSTEM

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;

    @PrePersist
    void setRecordedAtIfMissing() {
        if (recordedAt == null) recordedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
