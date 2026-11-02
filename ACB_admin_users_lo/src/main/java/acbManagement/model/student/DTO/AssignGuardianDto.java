package acbManagement.model.student.DTO;

import acbManagement.model.student.Relationship;
import jakarta.validation.constraints.*;

public record AssignGuardianDto(
        @NotNull(message = "El identificador del acudiente es obligatorio")
        Long guardianId,

        @NotNull(message = "El parentesco es obligatorio")
        Relationship relationship,

        boolean isPrimary
) {
}
