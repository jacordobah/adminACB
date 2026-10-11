package acbManagement.model.route;

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
public class DefaultOperationId implements Serializable {

    @Column(name = "route_id", length = 4)
    private String routeId;

    @Column(name = "shift_code")
    private String shiftCode; 

    @Column(name = "guide_id")
    private Long guideId;
}

