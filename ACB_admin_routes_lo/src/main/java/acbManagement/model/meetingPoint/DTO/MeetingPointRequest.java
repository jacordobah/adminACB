package acbManagement.model.meetingPoint.DTO;

import jakarta.validation.constraints.*;

public record MeetingPointRequest(
        @NotBlank @Size(max = 4) String meetingPointCode,
        @NotBlank @Size(max = 45) String address,
        @PositiveOrZero Integer distance,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude
) {
}
