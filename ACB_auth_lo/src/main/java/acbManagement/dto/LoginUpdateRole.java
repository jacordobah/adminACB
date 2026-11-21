package acbManagement.dto;

import acbManagement.model.guide.Role;
import jakarta.validation.constraints.NotNull;

public record LoginUpdateRole(
        @NotNull(message = "El ID del guía es obligatorio")
        Long guideId,
        @NotNull(message = "El nuevo rol es obligatorio")
        Role role
) {
}
