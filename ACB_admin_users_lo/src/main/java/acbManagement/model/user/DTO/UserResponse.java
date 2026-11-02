package acbManagement.model.user.DTO;

import acbManagement.model.user.Role;

public record UserResponse(
        Long userId,
        String documentType,
        String documentNumber,
        String name,
        String phone,
        Role role,
        boolean active
) {
}
