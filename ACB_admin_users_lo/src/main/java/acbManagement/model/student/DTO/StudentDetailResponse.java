package acbManagement.model.student.DTO;

import java.util.List;

public record StudentDetailResponse(
        Long userId,
        String documentType,
        String documentNumber,
        String name,
        String address,
        String phone,
        String grade,
        String schoolShift,
        String section,
        Long schoolId,
        List<GuardianDetailResponse> guardians
) {
}
