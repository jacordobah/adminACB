package acbManagement.dto.operation;

import java.time.LocalDate;

// Una fila plana del historial de asistencia de una ruta. El gateway la usa
// para armar el contrato que consume presentationACB.
public record RouteAttendanceRow(
        Long tripId,
        Long routeId,
        LocalDate date,
        String tripCode,
        Long studentId,
        String studentName,
        String statusCode
) {}
