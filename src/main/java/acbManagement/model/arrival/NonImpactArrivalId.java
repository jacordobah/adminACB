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
public class NonImpactArrivalId implements Serializable {

    @Column(name = "non_impact_arrival_id")
    private Long nonImpactArrivalId;

    @Column(name = "guide_id")
    private Long guideId;
}
