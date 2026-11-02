package acbManagement.model.user.DTO;

import acbManagement.model.student.DTO.StudentRegistrationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @Valid @NotNull UserRegistrationDto user,
        @Valid @NotNull StudentRegistrationDto student
) {
}
