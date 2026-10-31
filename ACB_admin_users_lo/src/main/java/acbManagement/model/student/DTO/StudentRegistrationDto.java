package acbManagement.model.student.DTO;

import jakarta.validation.constraints.*;
import org.apache.logging.log4j.message.Message;

public record StudentRegistrationDto(
        @NotBlank(message = "El grado es obligatorio")
        @Size(max = 20)
        String grade,

        @NotBlank(message = "La jornada/curso es obligatoria")
        @Size(max = 15)
        String schoolShift,

        @NotBlank(message = "La sección es obligatoria")
        @Size(max = 15)
        String section,

        @NotNull(message = "El id del colegio es obligatoria")
        Long schoolId
) {
}
