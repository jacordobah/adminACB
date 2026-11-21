package acbManagement.dto;

import acbManagement.model.guide.Role;
import jakarta.validation.constraints.*;


public record LoginRegister(
        @NotNull(message = "El ID del guía es obligatorio")
        Long guideId,
        @NotBlank(message =" El username es obligatorio")
        @Size(max = 45)
        String username,
        @NotBlank(message = "El Password es obligatorio")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String password,
        @NotNull(message = "El rol es obligatorio")
        Role role
) {
}
