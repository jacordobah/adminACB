package acbManagement.model.route.DTO;

// Un guia asignado a una ruta. guideId es el id del componente de Usuarios.
public record RouteGuideDto(
        Long guideId,
        String name,
        boolean primary
) {
}
