package acbManagement.service;

import acbManagement.dto.operation.AttendanceCommand;
import acbManagement.dto.operation.StudentRosterEntry;
import acbManagement.model.trip.Trip;
import acbManagement.model.trip.TripStudentAttendance;
import acbManagement.model.trip.TripStudentAttendanceId;
import acbManagement.repository.TripRepository;
import acbManagement.repository.TripStudentAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TripStudentAttendanceService {
    private static final Set<String> VALID_STATUS_CODES = Set.of("00", "01", "0I", "0C");

    private final TripRepository tripRepository;
    private final TripStudentAttendanceRepository attendanceRepository;

    /**
     * Inicializa el padrón recibido desde Routes una sola vez por estudiante y recorrido.
     * Los registros nuevos comienzan en 00; las llamadas repetidas no reinician asistencias.
     */
    @Transactional
    public List<TripStudentAttendance> initializeRoster(Long tripId, List<StudentRosterEntry> roster) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe el recorrido " + tripId));
        if (roster == null) {
            throw OperationServiceSupport.badRequest("El padrón de estudiantes es obligatorio.");
        }

        Set<Long> existingStudentIds = new HashSet<>();
        attendanceRepository.findAllByTrip_TripIdOrderByStudentNameSnapshot(tripId)
                .forEach(row -> existingStudentIds.add(row.getId().getStudentId()));

        List<TripStudentAttendance> newRows = new ArrayList<>();
        for (StudentRosterEntry entry : roster) {
            if (entry == null || entry.studentId() == null || isBlank(entry.studentName())) {
                throw OperationServiceSupport.badRequest("Cada estudiante del padrón requiere ID y nombre.");
            }
            if (existingStudentIds.add(entry.studentId())) {
                TripStudentAttendance row = new TripStudentAttendance();
                row.setId(new TripStudentAttendanceId(tripId, entry.studentId()));
                row.setTrip(trip);
                row.setStudentNameSnapshot(entry.studentName().trim());
                row.setStatusCode("00");
                row.setRecordedAt(OffsetDateTime.now(ZoneOffset.UTC));
                newRows.add(row);
            }
        }
        return attendanceRepository.saveAll(newRows);
    }

    @Transactional
    public TripStudentAttendance registerOrUpdate(
            Long tripId, AttendanceCommand command, Long recordedByUserId) {
        if (command == null || command.studentId() == null || isBlank(command.studentNameSnapshot())) {
            throw OperationServiceSupport.badRequest("Se requiere el estudiante y su nombre.");
        }
        if (command.statusCode() == null || !VALID_STATUS_CODES.contains(command.statusCode())) {
            throw OperationServiceSupport.badRequest("El estado debe ser 00, 01, 0I o 0C.");
        }
        if ("0I".equals(command.statusCode()) && isBlank(command.note())) {
            throw OperationServiceSupport.badRequest("La inasistencia justificada requiere una observación.");
        }
        if ("0C".equals(command.statusCode()) && isBlank(command.accompanyingPersonName())) {
            throw OperationServiceSupport.badRequest("El caso especial requiere registrar quién acompañó al estudiante.");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe el recorrido " + tripId));

        TripStudentAttendanceId id = new TripStudentAttendanceId(tripId, command.studentId());
        TripStudentAttendance attendance = attendanceRepository.findById(id)
                .orElseGet(() -> {
                    TripStudentAttendance created = new TripStudentAttendance();
                    created.setId(id);
                    created.setTrip(trip);
                    return created;
                });

        attendance.setStudentNameSnapshot(command.studentNameSnapshot().trim());
        attendance.setStatusCode(command.statusCode());
        attendance.setNote(trimToNull(command.note()));
        attendance.setAccompanyingPersonName(trimToNull(command.accompanyingPersonName()));
        attendance.setAccompanyingPersonRelation(trimToNull(command.accompanyingPersonRelation()));
        attendance.setRecordedByUserId(recordedByUserId);
        attendance.setRecordedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public List<TripStudentAttendance> listByTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw OperationServiceSupport.notFound("No existe el recorrido " + tripId);
        }
        return attendanceRepository.findAllByTrip_TripIdOrderByStudentNameSnapshot(tripId);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String trimToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }
}
