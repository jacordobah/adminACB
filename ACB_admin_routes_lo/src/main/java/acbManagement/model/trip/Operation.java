package acbManagement.model.trip;

import acbManagement.model.guide.Guide;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

    @EmbeddedId
    private OperationId id = new OperationId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tripId")
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guideId")
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @Column(name = "is_primary_on_trip", nullable = false)
    private boolean isPrimaryOnTrip = true;
}
