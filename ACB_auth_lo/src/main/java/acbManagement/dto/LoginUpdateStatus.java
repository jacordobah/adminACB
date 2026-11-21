package acbManagement.dto;

import jakarta.validation.constraints.NotNull;

public record LoginUpdateStatus(
        @NotNull(message = "El ID del guía es obligatorio")
        Long guideId,
        @NotNull(message = "El estado es obligatorio (true o false)")
        Boolean active
) {
}
