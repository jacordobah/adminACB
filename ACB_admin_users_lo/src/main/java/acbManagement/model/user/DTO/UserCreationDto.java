package acbManagement.model.user.DTO;

import acbManagement.model.user.DocumentType;
import acbManagement.model.user.Role;
import jakarta.validation.constraints.*;

public record UserCreationDto(
        @NotNull(message = "El tipo de documento es obligatorio")
        DocumentType documentType,

        @NotBlank(message = "El número de documento no puede estar vacío")
        @Size(max = 20)
        String documentNumber,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        String name,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200)
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 25)
        String phone,

        @NotNull(message = "El rol es obligatorio")
        Role role
) {
}
