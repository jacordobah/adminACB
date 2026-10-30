package acbManagement.model.trip;

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
public class OperationId implements Serializable {
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "guide_id")
    private Long guideId;
}
