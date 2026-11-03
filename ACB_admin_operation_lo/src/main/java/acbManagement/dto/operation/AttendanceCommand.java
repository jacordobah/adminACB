package acbManagement.dto.operation;

public record AttendanceCommand(
        Long studentId,
        String studentNameSnapshot,
        String statusCode,
        String note,
        String accompanyingPersonName,
        String accompanyingPersonRelation
) {}
