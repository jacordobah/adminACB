package acbManagement.model.trip;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TripStudentAttendanceId implements Serializable {
    @Column(name = "trip_id")
    private Long tripId;

    // ID externo de Users.
    @Column(name = "student_id")
    private Long studentId;
}
