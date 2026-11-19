package acbManagement.service;

import acbManagement.dto.operation.CreateTripRequest;
import acbManagement.dto.operation.ReplaceGuideRequest;
import acbManagement.dto.operation.RouteAttendanceRow;
import acbManagement.model.event.ActionCode;
import acbManagement.model.trip.Trip;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.model.trip.TripStudentAttendance;
import acbManagement.repository.TripGuideAttendanceRepository;
import acbManagement.repository.TripRepository;
import acbManagement.repository.TripStudentAttendanceRepository;
import acbManagement.repository.ActionCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TripService {
    private static final Set<String> VALID_SHIFTS = Set.of("541", "542");

    private final TripRepository tripRepository;
    private final TripGuideAttendanceRepository tripGuideRepository;
    private final TripStudentAttendanceRepository tripStudentAttendanceRepository;
    private final ActionCodeRepository actionCodeRepository;

    @Transactional
    public Trip create(CreateTripRequest command) {
        if (command == null || command.routeId() == null || command.tripDate() == null
                || command.scheduledStart() == null || command.startPointId() == null
                || command.mainGuideId() == null || command.supportGuideId() == null
                || isBlank(command.routeCode()) || isBlank(command.schoolName())
                || isBlank(command.startPointCode()) || isBlank(command.mainGuideName())
                || isBlank(command.supportGuideName())) {
            throw OperationServiceSupport.badRequest("Completa ruta, fecha, horario, punto inicial, colegio y ambos guías.");
        }
        if (command.shiftCode() == null || !VALID_SHIFTS.contains(command.shiftCode())) {
            throw OperationServiceSupport.badRequest("El sentido del recorrido debe ser 541 o 542.");
        }
        if (command.mainGuideId().equals(command.supportGuideId())) {
            throw OperationServiceSupport.badRequest("El guía principal y el de apoyo deben ser distintos.");
        }
        if (tripRepository.findByRouteIdAndTripDate(command.routeId(), command.tripDate()).isPresent()) {
            throw OperationServiceSupport.conflict("Ya existe un recorrido para esa ruta y fecha.");
        }

        Trip trip = new Trip();
        trip.setRouteId(command.routeId());
        trip.setRouteCodeSnapshot(command.routeCode().trim());
        trip.setShiftCodeSnapshot(command.shiftCode());
        trip.setTripDate(command.tripDate());
        trip.setSchoolNameSnapshot(command.schoolName().trim());
        trip.setScheduledStartSnapshot(command.scheduledStart());
        trip.setStartPointId(command.startPointId());
        trip.setStartPointCodeSnapshot(command.startPointCode().trim());
        Trip savedTrip = tripRepository.save(trip);

        TripGuideAttendance main = createGuideAssignment(
                savedTrip, "MAIN", command.mainGuideId(), command.mainGuideName());
        TripGuideAttendance support = createGuideAssignment(
                savedTrip, "SUPPORT", command.supportGuideId(), command.supportGuideName());
        tripGuideRepository.saveAll(List.of(main, support));
        return savedTrip;
    }

    @Transactional(readOnly = true)
    public Trip get(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe el recorrido " + tripId));
    }

    @Transactional(readOnly = true)
    public Trip getByRouteAndDate(Long routeId, java.time.LocalDate tripDate) {
        return tripRepository.findByRouteIdAndTripDate(routeId, tripDate)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe recorrido para esa ruta y fecha."));
    }

    @Transactional(readOnly = true)
    public List<Trip> listByDate(LocalDate tripDate, Long guideId) {
        if (tripDate == null) {
            throw OperationServiceSupport.badRequest("La fecha es obligatoria.");
        }
        List<Trip> trips = tripRepository.findAllByTripDateOrderByRouteCodeSnapshot(tripDate);
        if (guideId == null) {
            return trips;
        }
        return trips.stream()
                .filter(trip -> tripGuideRepository
                        .findAllByTrip_TripIdOrderByAssignmentRole(trip.getTripId()).stream()
                        .anyMatch(assignment -> guideId.equals(assignment.getPlannedGuideId())
                                || guideId.equals(assignment.getActualGuideId())))
                .toList();
    }

    // Historial de asistencia de una ruta en un rango de fechas. Devuelve una
    // fila por estudiante y recorrido para que el gateway solo tenga que
    // traducir el estado.
    @Transactional(readOnly = true)
    public List<RouteAttendanceRow> listAttendanceByRoute(Long routeId, LocalDate from, LocalDate to) {
        if (routeId == null || from == null || to == null) {
            throw OperationServiceSupport.badRequest("La ruta y el rango de fechas son obligatorios.");
        }
        if (from.isAfter(to)) {
            throw OperationServiceSupport.badRequest("La fecha inicial no puede ser posterior a la final.");
        }
        return tripRepository.findAllByRouteIdOrderByTripDateDesc(routeId).stream()
                .filter(trip -> !trip.getTripDate().isBefore(from) && !trip.getTripDate().isAfter(to))
                .flatMap(trip -> tripStudentAttendanceRepository
                        .findAllByTrip_TripIdOrderByStudentNameSnapshot(trip.getTripId()).stream()
                        .map(attendance -> toAttendanceRow(trip, attendance)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TripGuideAttendance> listGuides(Long tripId) {
        get(tripId);
        return tripGuideRepository.findAllByTrip_TripIdOrderByAssignmentRole(tripId);
    }

    @Transactional
    public TripGuideAttendance replaceGuide(
            Long tripId, Long tripGuideId, ReplaceGuideRequest request, Long changedByUserId) {
        if (request == null || request.replacementGuideId() == null
                || isBlank(request.replacementGuideName()) || isBlank(request.reason())) {
            throw OperationServiceSupport.badRequest("El reemplazo requiere ID, nombre y motivo.");
        }
        TripGuideAttendance assignment = tripGuideRepository.findById(tripGuideId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la asignación de guía " + tripGuideId));
        if (!Objects.equals(assignment.getTrip().getTripId(), tripId)) {
            throw OperationServiceSupport.badRequest("La asignación no pertenece a este recorrido.");
        }
        if (assignment.getArrivedAt() != null) {
            throw OperationServiceSupport.conflict("No se puede reemplazar un guía después de registrar su llegada.");
        }
        if (Objects.equals(assignment.getPlannedGuideId(), request.replacementGuideId())) {
            throw OperationServiceSupport.badRequest("El reemplazo debe ser distinto del guía originalmente asignado.");
        }

        boolean alreadyAssigned = tripGuideRepository
                .findAllByTrip_TripIdOrderByAssignmentRole(tripId).stream()
                .filter(other -> !other.getTripGuideId().equals(tripGuideId))
                .anyMatch(other -> Objects.equals(other.getPlannedGuideId(), request.replacementGuideId())
                        || Objects.equals(other.getActualGuideId(), request.replacementGuideId()));
        if (alreadyAssigned) {
            throw OperationServiceSupport.conflict("Ese guía ya está asignado a otro puesto en este recorrido.");
        }

        assignment.setActualGuideId(request.replacementGuideId());
        assignment.setActualGuideNameSnapshot(request.replacementGuideName().trim());
        assignment.setSubstitutionReason(request.reason().trim());
        assignment.setReplacementRecordedByUserId(changedByUserId);
        assignment.setReplacementRecordedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return tripGuideRepository.save(assignment);
    }

    @Transactional
    public Trip start(Long tripId, OffsetDateTime occurredAt) {
        Trip trip = get(tripId);
        if (trip.getActualStartAt() != null) {
            throw OperationServiceSupport.conflict("El recorrido ya tiene hora real de inicio.");
        }
        trip.setActualStartAt(occurredAt != null ? occurredAt : OffsetDateTime.now(ZoneOffset.UTC));
        return tripRepository.save(trip);
    }

    @Transactional
    public Trip finish(Long tripId, OffsetDateTime occurredAt) {
        Trip trip = get(tripId);
        if (trip.getActualStartAt() == null) {
            throw OperationServiceSupport.conflict("Registra el inicio antes de finalizar el recorrido.");
        }
        if (trip.getActualEndAt() != null) {
            throw OperationServiceSupport.conflict("El recorrido ya fue finalizado.");
        }
        OffsetDateTime endAt = occurredAt != null ? occurredAt : OffsetDateTime.now(ZoneOffset.UTC);
        if (endAt.isBefore(trip.getActualStartAt())) {
            throw OperationServiceSupport.badRequest("La hora final no puede ser anterior al inicio.");
        }
        ActionCode completionCode = actionCodeRepository.findById("524")
                .orElseThrow(() -> OperationServiceSupport.conflict("No existe el código de finalización 524."));
        trip.setActualEndAt(endAt);
        trip.setCompletionCode(completionCode);
        return tripRepository.save(trip);
    }

    private static RouteAttendanceRow toAttendanceRow(
            Trip trip, TripStudentAttendance attendance) {
        return new RouteAttendanceRow(
                trip.getTripId(),
                trip.getRouteId(),
                trip.getTripDate(),
                trip.getShiftCodeSnapshot(),
                attendance.getId().getStudentId(),
                attendance.getStudentNameSnapshot(),
                attendance.getStatusCode());
    }

    private static TripGuideAttendance createGuideAssignment(
            Trip trip, String role, Long guideId, String guideName) {
        TripGuideAttendance assignment = new TripGuideAttendance();
        assignment.setTrip(trip);
        assignment.setAssignmentRole(role);
        assignment.setPlannedGuideId(guideId);
        assignment.setPlannedGuideNameSnapshot(guideName.trim());
        return assignment;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
