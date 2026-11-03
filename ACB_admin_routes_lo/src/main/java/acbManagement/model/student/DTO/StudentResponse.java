package acbManagement.model.student.DTO;

public record StudentResponse(
        Long studentId,
        String name,
        Boolean active,
        Long routeId,
        String routeCode
) {
}
