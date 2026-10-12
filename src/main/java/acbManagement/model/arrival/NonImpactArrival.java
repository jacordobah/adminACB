package acbManagement.model.arrival;

import acbManagement.model.guide.Guide;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "non_impact_arrivals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NonImpactArrival {

    @EmbeddedId
    private NonImpactArrivalId id = new NonImpactArrivalId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guideId")
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NonImpactStatus status;

    @Column(name = "counter", nullable = false)
    private int counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "impact_arrival_id", referencedColumnName = "impact_arrival_id"),
            @JoinColumn(name = "guide_id", referencedColumnName = "guide_id", insertable = false, updatable = false)
    })
    private ImpactArrival impactArrival;
}
