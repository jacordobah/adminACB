package acbManagement.service;

import acbManagement.dto.operation.GuideCheckInCommand;
import acbManagement.model.event.ActionCode;
import acbManagement.model.guide.GuideCheckInAttempt;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.repository.ActionCodeRepository;
import acbManagement.repository.GuideCheckInAttemptRepository;
import acbManagement.repository.TripGuideAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideCheckInService {
    private static final String CHECK_IN_CODE = "523";
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");

    private final TripGuideAttendanceRepository tripGuideRepository;
    private final GuideCheckInAttemptRepository attemptRepository;
    private final ActionCodeRepository actionCodeRepository;
    private final GuideFaultService guideFaultService;

    @Transactional(readOnly = true)
    public List<GuideCheckInAttempt> listAttempts(Long tripGuideId) {
        if (!tripGuideRepository.existsById(tripGuideId)) {
            throw OperationServiceSupport.notFound("No existe la asignación de guía " + tripGuideId);
        }
        return attemptRepository.findAllByTripGuideAttendance_TripGuideIdOrderByAttemptedAtDesc(tripGuideId);
    }

    /**
     * Persiste también intentos rechazados. withinRadius debe venir de la validación
     * confiable del componente de geolocalización, nunca del cliente móvil directamente.
     */
    @Transactional
    public GuideCheckInAttempt registerAttempt(
            Long tripGuideId, Long authenticatedGuideId, GuideCheckInCommand command) {
        if (command == null || authenticatedGuideId == null) {
            throw OperationServiceSupport.badRequest("Falta la identidad autenticada del guía o el intento.");
        }
        if (command.latitude() == null || command.longitude() == null || command.allowedRadiusM() == null) {
            throw OperationServiceSupport.badRequest("El intento requiere coordenadas y radio permitido.");
        }
        if (command.allowedRadiusM().signum() < 0
                || (command.distanceToStartM() != null && command.distanceToStartM().signum() < 0)) {
            throw OperationServiceSupport.badRequest("La distancia y el radio no pueden ser negativos.");
        }

        TripGuideAttendance tripGuide = tripGuideRepository.findById(tripGuideId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la asignación de guía " + tripGuideId));

        Long assignedGuideId = tripGuide.getActualGuideId() != null
                ? tripGuide.getActualGuideId()
                : tripGuide.getPlannedGuideId();
        if (!Objects.equals(assignedGuideId, authenticatedGuideId)) {
            throw OperationServiceSupport.forbidden("El guía no está asignado a este recorrido.");
        }

        OffsetDateTime attemptedAt = command.attemptedAt() != null
                ? command.attemptedAt()
                : OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime scheduledStart = tripGuide.getTrip().getTripDate()
                .atTime(tripGuide.getTrip().getScheduledStartSnapshot())
                .atZone(BUSINESS_ZONE)
                .toOffsetDateTime();
        boolean withinTime = !attemptedAt.isAfter(scheduledStart.minusMinutes(15));

        String source = command.source() == null || command.source().isBlank()
                ? "OPERATION" : command.source().trim();
        if (command.sourceEventId() != null
                && attemptRepository.existsBySourceAndSourceEventId(source, command.sourceEventId())) {
            throw OperationServiceSupport.conflict("El intento de llegada ya fue recibido.");
        }

        ActionCode checkInCode = actionCodeRepository.findById(CHECK_IN_CODE)
                .orElseThrow(() -> OperationServiceSupport.conflict("No existe el código de operación 523."));

        GuideCheckInAttempt attempt = new GuideCheckInAttempt();
        attempt.setTripGuideAttendance(tripGuide);
        attempt.setOperationCode(checkInCode);
        attempt.setAttemptedAt(attemptedAt);
        attempt.setLatitude(command.latitude());
        attempt.setLongitude(command.longitude());
        attempt.setDistanceToStartM(command.distanceToStartM());
        attempt.setAllowedRadiusM(command.allowedRadiusM());
        attempt.setWithinRadius(command.withinRadius());
        attempt.setWithinTime(withinTime);
        attempt.setAccepted(command.withinRadius());
        attempt.setSource(source);
        attempt.setSourceEventId(command.sourceEventId());
        if (!command.withinRadius()) {
            attempt.setRejectionReason("El intento se realizó fuera del radio permitido.");
        }

        GuideCheckInAttempt saved = attemptRepository.save(attempt);
        if (saved.isAccepted() && tripGuide.getArrivedAt() == null) {
            tripGuide.setArrivedAt(attemptedAt);
            if (tripGuide.getActualGuideId() == null) {
                tripGuide.setActualGuideId(authenticatedGuideId);
                tripGuide.setActualGuideNameSnapshot(tripGuide.getPlannedGuideNameSnapshot());
            }
            tripGuideRepository.save(tripGuide);
            guideFaultService.registerArrivalFault(tripGuide, attemptedAt);
        }
        return saved;
    }
}
