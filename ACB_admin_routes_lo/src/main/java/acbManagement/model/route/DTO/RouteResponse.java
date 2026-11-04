package acbManagement.model.route.DTO;

import java.time.LocalTime;
import java.util.List;

public record RouteResponse(
        Long routeId,
        String routeCode,
        String shiftCode,
        String description,
        Long schoolId,
        String schoolName,
        String zone,
        LocalTime scheduledStartTime,
        List<RouteGuideDto> guides,
        long studentCount,
        Boolean active
) {
}
