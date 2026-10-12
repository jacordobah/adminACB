package acbManagement.model.arrival;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ImpactArrivalId implements Serializable {

    @Column(name = "impact_arrival_id")
    private Long impactArrivalId;

    @Column(name = "guide_id")
    private Long guideId; 
}
