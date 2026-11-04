package acbManagement.model.school.DTO;

import jakarta.validation.constraints.*;

public record SchoolRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 45) String schoolCode,
        @NotBlank @Size(max = 45) String phone,
        @NotBlank @Size(max = 200) String address,
        @NotBlank @Size(max = 45) String zone,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude
) {
}
