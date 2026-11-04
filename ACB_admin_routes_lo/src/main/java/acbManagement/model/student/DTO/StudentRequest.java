package acbManagement.model.student.DTO;

import jakarta.validation.constraints.*;

// Registra en este componente un estudiante que ya existe en Usuarios.
// studentId lo da el llamador, no se genera aqui.
public record StudentRequest(
        @NotNull Long studentId,
        @NotBlank @Size(max = 150) String name
) {
}
