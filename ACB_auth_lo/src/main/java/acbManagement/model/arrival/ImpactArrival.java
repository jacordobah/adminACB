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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impact_arrival_id")
    private Long impactArrivalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImpactStatus status;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @OneToMany(mappedBy = "impactArrival", fetch = FetchType.LAZY)
    private java.util.List<NonImpactArrival> nonImpactArrivals;
}
