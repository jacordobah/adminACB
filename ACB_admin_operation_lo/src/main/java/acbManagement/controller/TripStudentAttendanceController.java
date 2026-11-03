package acbManagement.controller;

import acbManagement.dto.operation.AttendanceCommand;
import acbManagement.dto.operation.StudentRosterEntry;
import acbManagement.infra.security.CurrentActor;
import acbManagement.model.trip.TripStudentAttendance;
import acbManagement.service.TripAccessService;
import acbManagement.service.TripStudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/attendance")
@RequiredArgsConstructor
public class TripStudentAttendanceController {
    private final TripStudentAttendanceService attendanceService;
    private final TripAccessService tripAccessService;

    @PostMapping("/roster")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TripStudentAttendance> initializeRoster(
            @PathVariable Long tripId, @RequestBody List<StudentRosterEntry> roster,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor.from(jwt).requireAnyRole("LEADER", "MANAGER");
        return attendanceService.initializeRoster(tripId, roster);
    }

    @GetMapping
    public List<TripStudentAttendance> list(
            @PathVariable Long tripId, @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return attendanceService.listByTrip(tripId);
    }

    @PutMapping("/{studentId}")
    public TripStudentAttendance registerOrUpdate(
            @PathVariable Long tripId, @PathVariable Long studentId,
            @RequestBody AttendanceCommand request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        tripAccessService.requireCanAccessTrip(tripId, actor);
        AttendanceCommand command = new AttendanceCommand(studentId, request.studentNameSnapshot(),
                request.statusCode(), request.note(), request.accompanyingPersonName(),
                request.accompanyingPersonRelation());
        return attendanceService.registerOrUpdate(tripId, command, actor.userId());
    }
}
