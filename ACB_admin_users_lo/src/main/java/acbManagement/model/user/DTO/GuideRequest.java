package acbManagement.model.user.DTO;

import acbManagement.model.guide.GuideRegistrationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GuideRequest(
        @Valid @NotNull UserRegistrationDto user,
        @Valid @NotNull GuideRegistrationDto guide) {
}
