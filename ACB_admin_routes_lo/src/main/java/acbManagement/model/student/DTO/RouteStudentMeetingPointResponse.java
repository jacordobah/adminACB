package acbManagement.model.student.DTO;

// Punto de encuentro de un estudiante dentro de una ruta. Incluye el
// studentId para que el gateway pueda unir estudiantes y puntos en una sola
// consulta.
public record RouteStudentMeetingPointResponse(
        Long studentId,
        String shiftCode,
        Long meetingPointId,
        String meetingPointCode,
        String address
) {}
