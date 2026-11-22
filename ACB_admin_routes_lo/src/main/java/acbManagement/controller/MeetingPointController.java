package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.meetingPoint.DTO.MeetingPointRequest;
import acbManagement.model.meetingPoint.DTO.MeetingPointResponse;
import acbManagement.service.MeetingPointService;
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
public class MeetingPointController {

    private final MeetingPointService meetingPointService;

    @GetMapping("/routes/{routeId}/meeting-points")
    public ResponseEntity<List<MeetingPointResponse>> listByRoute(@PathVariable Long routeId,
                                                                  @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(meetingPointService.findByRoute(routeId, actor));
    }

    @PostMapping("/routes/{routeId}/meeting-points")
    public ResponseEntity<MeetingPointResponse> create(
            @PathVariable Long routeId,
            @RequestBody @Valid MeetingPointRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return new ResponseEntity<>(meetingPointService.create(routeId, request, actor), HttpStatus.CREATED);
    }

    @GetMapping("/meeting-points/{id}")
    public ResponseEntity<MeetingPointResponse> getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(meetingPointService.findById(id, actor));
    }

    @PutMapping("/meeting-points/{id}")
    public ResponseEntity<MeetingPointResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MeetingPointRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(meetingPointService.update(id, request, actor));
    }

    @DeleteMapping("/meeting-points/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        meetingPointService.delete(id, actor);
        return ResponseEntity.noContent().build();
    }
}
