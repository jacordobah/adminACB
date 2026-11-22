package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.student.DTO.*;
import acbManagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ---- estudiantes de una ruta ----

    @GetMapping("/routes/{routeId}/students")
    public ResponseEntity<List<StudentResponse>> listByRoute(@PathVariable Long routeId,
                                                             @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.findByRoute(routeId, actor));
    }

    @GetMapping("/routes/{routeId}/students/meeting-points")
    public ResponseEntity<List<RouteStudentMeetingPointResponse>> listMeetingPointsByRoute(
            @PathVariable Long routeId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getMeetingPointsByRoute(routeId,actor));
    }

    @PostMapping("/routes/{routeId}/students")
    public ResponseEntity<StudentResponse> assignToRoute(
            @PathVariable Long routeId,
            @RequestBody @Valid StudentRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return new ResponseEntity<>(studentService.assignToRoute(routeId, request, actor), HttpStatus.CREATED);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.findById(id,actor));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid StudentUpdateRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.update(id, request, actor));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
            CurrentActor actor = CurrentActor.from(jwt);
            actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        studentService.deactivate(id, actor);
        return ResponseEntity.noContent().build();
    }

    // ---- punto de encuentro del estudiante por jornada ----

    @PutMapping("/students/{id}/meeting-points")
    public ResponseEntity<List<StudentMeetingPointResponse>> assignMeetingPoints(
            @PathVariable Long id,
            @RequestBody @Valid AssignMeetingPointsRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.assignMeetingPoints(id, request, actor));
    }

    @GetMapping("/students/{id}/meeting-points")
    public ResponseEntity<List<StudentMeetingPointResponse>> getMeetingPoints(@PathVariable Long id,
                                                                              @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getMeetingPoints(id, actor));
    }

    // ---- llamado a lista del guia ----

    @GetMapping("/meeting-points/{meetingPointId}/students")
    public ResponseEntity<List<StudentAtPointResponse>> rosterAtPoint(
            @PathVariable Long meetingPointId,
            @RequestParam String shift, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getRosterAtPoint(meetingPointId, shift, actor));
    }
}
