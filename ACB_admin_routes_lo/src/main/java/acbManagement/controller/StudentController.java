package acbManagement.controller;

import acbManagement.model.student.DTO.*;
import acbManagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ---- estudiantes de una ruta ----

    @GetMapping("/routes/{routeId}/students")
    public ResponseEntity<List<StudentResponse>> listByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.findByRoute(routeId));
    }

    @PostMapping("/routes/{routeId}/students")
    public ResponseEntity<StudentResponse> assignToRoute(
            @PathVariable Long routeId,
            @RequestBody @Valid StudentRequest request) {
        return new ResponseEntity<>(studentService.assignToRoute(routeId, request), HttpStatus.CREATED);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        studentService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // ---- punto de encuentro del estudiante por jornada ----

    @PutMapping("/students/{id}/meeting-points")
    public ResponseEntity<List<StudentMeetingPointResponse>> assignMeetingPoints(
            @PathVariable Long id,
            @RequestBody @Valid AssignMeetingPointsRequest request) {
        return ResponseEntity.ok(studentService.assignMeetingPoints(id, request));
    }

    @GetMapping("/students/{id}/meeting-points")
    public ResponseEntity<List<StudentMeetingPointResponse>> getMeetingPoints(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getMeetingPoints(id));
    }

    // ---- llamado a lista del guia ----

    @GetMapping("/meeting-points/{meetingPointId}/students")
    public ResponseEntity<List<StudentAtPointResponse>> rosterAtPoint(
            @PathVariable Long meetingPointId,
            @RequestParam String shift) {
        return ResponseEntity.ok(studentService.getRosterAtPoint(meetingPointId, shift));
    }
}
