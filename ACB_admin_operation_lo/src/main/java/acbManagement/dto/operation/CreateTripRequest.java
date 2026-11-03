package acbManagement.dto.operation;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTripRequest(
        Long routeId,
        String routeCode,
        String shiftCode,
        LocalDate tripDate,
        String schoolName,
        LocalTime scheduledStart,
        Long startPointId,
        String startPointCode,
        Long mainGuideId,
        String mainGuideName,
        Long supportGuideId,
        String supportGuideName
) {}
