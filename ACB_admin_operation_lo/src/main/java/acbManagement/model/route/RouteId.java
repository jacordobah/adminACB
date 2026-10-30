package acbManagement.model.route;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RouteId implements Serializable {

    @Column(name = "route_id", length = 4, nullable = false)
    private String routeId; // "MM01"

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_code", nullable = false)
    private ShiftCode shiftCode; // SHIFT_541 o SHIFT_542
}
