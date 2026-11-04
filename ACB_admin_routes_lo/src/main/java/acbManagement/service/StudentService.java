package acbManagement.service;

import acbManagement.infra.exception.DuplicateResourceException;
import acbManagement.infra.exception.ResourceNotFoundException;
import acbManagement.model.meetingPoint.MeetingPoint;
import acbManagement.model.route.Route;
import acbManagement.model.student.DTO.*;
import acbManagement.model.student.Student;
import acbManagement.model.student.StudentMeetingPoint;
import acbManagement.model.student.StudentMeetingPointId;
import acbManagement.repository.MeetingPointRepository;
import acbManagement.repository.RouteRepository;
import acbManagement.repository.StudentMeetingPointRepository;
import acbManagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RouteRepository routeRepository;
    private final MeetingPointRepository meetingPointRepository;
    private final StudentMeetingPointRepository studentMeetingPointRepository;

    // ---- Estudiantes de una ruta ----

    public List<StudentResponse> findByRoute(Long routeId) {
        getRouteOrFail(routeId);
        return studentRepository.findByRoute_RouteId(routeId)
                .stream().map(this::toResponse).toList();
    }

    public StudentResponse findById(Long id) {
        return toResponse(getStudentOrFail(id));
    }

    @Transactional
    public StudentResponse assignToRoute(Long routeId, StudentRequest request) {
        Route route = getRouteOrFail(routeId);
        if (studentRepository.existsById(request.studentId())) {
            throw new DuplicateResourceException(
                    "El estudiante " + request.studentId() + " ya esta registrado en una ruta");
        }
        Student student = new Student();
        student.setStudentId(request.studentId());
        student.setName(request.name());
        student.setActive(true);
        student.setRoute(route);
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse update(Long id, StudentUpdateRequest request) {
        Student student = getStudentOrFail(id);
        student.setName(request.name());
        student.setActive(request.active());
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void deactivate(Long id) {
        Student student = getStudentOrFail(id);
        student.setActive(false);
        studentRepository.save(student);
    }

    // ---- Punto de encuentro del estudiante por jornada ----

    @Transactional
    public List<StudentMeetingPointResponse> assignMeetingPoints(
            Long studentId, AssignMeetingPointsRequest request) {
        Student student = getStudentOrFail(studentId);
        Long studentRouteId = student.getRoute().getRouteId();

        for (AssignMeetingPointsRequest.PointAssignment item : request.assignments()) {
            MeetingPoint point = meetingPointRepository.findById(item.meetingPointId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el punto de encuentro " + item.meetingPointId()));

            // El punto debe ser de la misma ruta del estudiante
            if (!point.getRoute().getRouteId().equals(studentRouteId)) {
                throw new IllegalArgumentException(
                        "El punto " + item.meetingPointId()
                                + " no pertenece a la ruta del estudiante");
            }

            StudentMeetingPoint smp = studentMeetingPointRepository
                    .findById_StudentIdAndId_ShiftCode(studentId, item.shiftCode())
                    .orElseGet(() -> {
                        StudentMeetingPoint created = new StudentMeetingPoint();
                        created.setId(new StudentMeetingPointId(studentId, item.shiftCode()));
                        created.setStudent(student);
                        return created;
                    });
            smp.setMeetingPoint(point);
            studentMeetingPointRepository.save(smp);
        }
        return getMeetingPoints(studentId);
    }

    public List<StudentMeetingPointResponse> getMeetingPoints(Long studentId) {
        getStudentOrFail(studentId);
        return studentMeetingPointRepository.findById_StudentId(studentId).stream()
                .map(smp -> {
                    MeetingPoint p = smp.getMeetingPoint();
                    return new StudentMeetingPointResponse(
                            smp.getId().getShiftCode(),
                            p.getMeetingPointId(),
                            p.getMeetingPointCode(),
                            p.getAddress());
                })
                .toList();
    }

    // Llamado a lista: estudiantes activos que se suben en un punto en una jornada
    public List<StudentAtPointResponse> getRosterAtPoint(Long meetingPointId, String shiftCode) {
        if (!meetingPointRepository.existsById(meetingPointId)) {
            throw new ResourceNotFoundException("No existe el punto de encuentro " + meetingPointId);
        }
        return studentMeetingPointRepository
                .findByMeetingPoint_MeetingPointIdAndId_ShiftCode(meetingPointId, shiftCode).stream()
                .map(StudentMeetingPoint::getStudent)
                .filter(s -> Boolean.TRUE.equals(s.getActive()))
                .map(s -> new StudentAtPointResponse(s.getStudentId(), s.getName(), shiftCode))
                .toList();
    }

    // ---- helpers ----

    private Route getRouteOrFail(Long routeId) {
        return routeRepository.findById(routeId)
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("No existe la ruta " + routeId));
    }

    private Student getStudentOrFail(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante " + id));
    }

    private StudentResponse toResponse(Student s) {
        Route route = s.getRoute();
        return new StudentResponse(
                s.getStudentId(),
                s.getName(),
                s.getActive(),
                route != null ? route.getRouteId() : null,
                route != null ? route.getRouteCode() : null
        );
    }
}
