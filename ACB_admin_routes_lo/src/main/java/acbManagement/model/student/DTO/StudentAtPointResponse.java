package acbManagement.model.student.DTO;

// Una fila del llamado a lista: quien se sube en un punto en una jornada.
public record StudentAtPointResponse(
        Long studentId,
        String name,
        String shiftCode
) {
}
