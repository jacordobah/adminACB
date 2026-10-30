package acbManagement.model.route;

import acbManagement.model.guide.Guide;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "default_operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultOperation {

    @EmbeddedId
    private DefaultOperationId id = new DefaultOperationId();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "route_id", referencedColumnName = "route_id", insertable = false, updatable = false),
            @JoinColumn(name = "shift_code", referencedColumnName = "shift_code", insertable = false, updatable = false)
    })
    private Route route;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guideId")
    @JoinColumn(name = "guide_id", insertable = false, updatable = false)
    private Guide guide;


    @Column(name = "is_primary_on_route", nullable = false)
    private boolean isPrimaryOnRoute = true; // TRUE = Encargado principal, FALSE = Apoyo
}

