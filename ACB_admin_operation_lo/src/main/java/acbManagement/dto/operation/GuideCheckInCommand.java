package acbManagement.dto.operation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entrada para el resultado que entrega el proceso confiable de validación geográfica.
 * No exponer withinRadius como dato modificable por la aplicación del guía.
 */
public record GuideCheckInCommand(
        OffsetDateTime attemptedAt,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal distanceToStartM,
        BigDecimal allowedRadiusM,
        boolean withinRadius,
        String source,
        String sourceEventId
) {}
