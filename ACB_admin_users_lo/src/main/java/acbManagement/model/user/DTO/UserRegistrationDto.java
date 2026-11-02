package acbManagement.model.user.DTO;

import acbManagement.model.user.DocumentType;
import acbManagement.model.user.Role;
import jakarta.validation.constraints.*;

public record UserRegistrationDto(
        @NotNull(message = "El tipo de documento es obligatorio")
        DocumentType documentType,

        @NotBlank(message = "El número de documento no puede estar vacío")
        @Size(max = 20, message = "El documento no puede superar los 20 caracteres")
        String documentNumber,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 25, message = "El teléfono no puede superar los 25 caracteres")
        String phone,

        @NotNull
        Role role
) {
}
