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
public class AttendanceId implements Serializable {
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "student_id")
    private Long studentId;
}
