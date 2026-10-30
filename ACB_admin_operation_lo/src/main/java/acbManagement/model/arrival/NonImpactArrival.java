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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_impact_arrival_id")
    private Long nonImpactArrivalId; // ◄ PK Simple y Autoincremental, idéntica a tu SQL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false) // ◄ FK simple hacia el guía
    private Guide guide;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NonImpactStatus status;

    @Column(name = "counter", nullable = false)
    private int counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "impact_arrival_id") // ◄ FK simple y nullable hacia la PK de ImpactArrival
    private ImpactArrival impactArrival;
}
