package acbManagement.model.student.DTO;

import jakarta.validation.constraints.*;

public record StudentUpdateRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull Boolean active
) {
}
