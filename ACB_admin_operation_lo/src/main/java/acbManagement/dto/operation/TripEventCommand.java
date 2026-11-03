package acbManagement.dto.operation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TripEventCommand(
        String operationCode,
        OffsetDateTime occurredAt,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String source,
        String sourceEventId,
        Long reportedByUserId,
        String reporterType
) {}
