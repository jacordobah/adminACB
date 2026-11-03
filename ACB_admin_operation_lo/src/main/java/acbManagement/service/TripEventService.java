package acbManagement.service;

import acbManagement.dto.operation.TripEventCommand;
import acbManagement.model.event.ActionCode;
import acbManagement.model.trip.Trip;
import acbManagement.model.trip.TripEvent;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.repository.ActionCodeRepository;
import acbManagement.repository.TripEventRepository;
import acbManagement.repository.TripGuideAttendanceRepository;
import acbManagement.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripEventService {
    private final TripRepository tripRepository;
    private final TripEventRepository eventRepository;
    private final TripGuideAttendanceRepository tripGuideRepository;
    private final ActionCodeRepository actionCodeRepository;

    @Transactional(readOnly = true)
    public java.util.List<TripEvent> listByTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw OperationServiceSupport.notFound("No existe el recorrido " + tripId);
        }
        return eventRepository.findAllByTrip_TripIdOrderByOccurredAtDesc(tripId);
    }

    @Transactional
    public TripEvent registerEvent(Long tripId, TripEventCommand command) {
        if (command == null || isBlank(command.operationCode()) || isBlank(command.source())
                || isBlank(command.reporterType())) {
            throw OperationServiceSupport.badRequest("El evento requiere código, origen y tipo de reportante.");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe el recorrido " + tripId));
        validateReporter(tripId, command);

        if (command.sourceEventId() != null
                && eventRepository.findBySourceAndSourceEventId(command.source(), command.sourceEventId()).isPresent()) {
            throw OperationServiceSupport.conflict("La notificación de origen ya fue registrada.");
        }

        ActionCode actionCode = actionCodeRepository.findById(command.operationCode())
                .orElseThrow(() -> OperationServiceSupport.badRequest(
                        "No existe el código de operación " + command.operationCode()));

        TripEvent event = new TripEvent();
        event.setTrip(trip);
        event.setOperationCode(actionCode);
        event.setOccurredAt(command.occurredAt() != null
                ? command.occurredAt() : OffsetDateTime.now(ZoneOffset.UTC));
        event.setDescription(command.description());
        event.setLatitude(command.latitude());
        event.setLongitude(command.longitude());
        event.setSource(command.source().trim());
        event.setSourceEventId(command.sourceEventId());
        event.setReportedByUserId(command.reportedByUserId());
        event.setReporterType(command.reporterType().trim().toUpperCase());
        return eventRepository.save(event);
    }

    private void validateReporter(Long tripId, TripEventCommand command) {
        String type = command.reporterType().trim().toUpperCase();
        if ("SYSTEM".equals(type)) return;
        if (command.reportedByUserId() == null) {
            throw OperationServiceSupport.badRequest("El reportante debe tener un ID de usuario.");
        }
        if ("ADMIN".equals(type)) return; // La autorización de rol se valida en Security/controller.
        if (!"GUIDE".equals(type)) {
            throw OperationServiceSupport.badRequest("El tipo de reportante debe ser GUIDE, ADMIN o SYSTEM.");
        }

        boolean assigned = tripGuideRepository.findAllByTrip_TripIdOrderByAssignmentRole(tripId).stream()
                .anyMatch(assignment -> Objects.equals(assignment.getActualGuideId(), command.reportedByUserId())
                        || (assignment.getActualGuideId() == null
                        && Objects.equals(assignment.getPlannedGuideId(), command.reportedByUserId())));
        if (!assigned) {
            throw OperationServiceSupport.forbidden("El guía no está asignado a este recorrido.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
