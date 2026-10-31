package acbManagement.model.student.DTO;

import acbManagement.model.student.Relationship;

public record GuardianDetailResponse(
        Long userId,
        String name,
        String phone,
        Relationship relationship,
        boolean isPrimary
) {
}
