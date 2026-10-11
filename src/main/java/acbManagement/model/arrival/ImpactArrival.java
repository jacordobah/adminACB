package acbManagement.model.arrival;

import acbManagement.model.guide.Guide;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "impact_arrivals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImpactArrival {

    @EmbeddedId
    private ImpactArrivalId id = new ImpactArrivalId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guideId")
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImpactStatus status;

    @Column(name = "details", columnDefinition = "TEXT", nullable = false)
    private String details;
}
