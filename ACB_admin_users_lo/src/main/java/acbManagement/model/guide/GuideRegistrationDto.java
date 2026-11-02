package acbManagement.model.guide;

import jakarta.validation.constraints.*;

public record GuideRegistrationDto(
        @NotBlank(message = "El número de contrato es obligatorio")
        @Size(max = 45)
        String contractNumber,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 45)
        String email,

        @NotBlank(message = "La zona es obligatoria")
        @Size(max = 45)
        String zone
) {
}
