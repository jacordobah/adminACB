package acbManagement.model.route.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalTime;

public record RouteRequest(
        @NotBlank @Size(max = 4) String routeCode,
        @NotBlank @Pattern(regexp = "541|542", message = "la jornada debe ser 541 o 542")
        String shiftCode,
        @Size(max = 2000) String description,
        @NotNull Long schoolId,
        @NotNull LocalTime scheduledStartTime
) {
}
