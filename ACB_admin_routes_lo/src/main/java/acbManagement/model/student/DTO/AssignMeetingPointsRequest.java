package acbManagement.model.student.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

// Donde se sube el estudiante en cada jornada. Como mucho dos entradas
// (una por jornada), y cada jornada no se puede repetir.
public record AssignMeetingPointsRequest(
        @NotEmpty @Size(max = 2) @Valid List<PointAssignment> assignments
) {
    public record PointAssignment(
            @NotBlank @Pattern(regexp = "541|542", message = "la jornada debe ser 541 o 542")
            String shiftCode,
            @NotNull Long meetingPointId
    ) {
    }
}
