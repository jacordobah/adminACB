package acbManagement.dto.operation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** Payload for a trusted callback from the geolocation component, not for the guide's browser. */
public record GeolocationCheckInRequest(
        Long guideId,
        OffsetDateTime attemptedAt,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal distanceToStartM,
        BigDecimal allowedRadiusM,
        boolean withinRadius,
        String sourceEventId
) {}
