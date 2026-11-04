package acbManagement.controller;

import acbManagement.model.meetingPoint.DTO.MeetingPointRequest;
import acbManagement.model.meetingPoint.DTO.MeetingPointResponse;
import acbManagement.service.MeetingPointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MeetingPointController {

    private final MeetingPointService meetingPointService;

    @GetMapping("/routes/{routeId}/meeting-points")
    public ResponseEntity<List<MeetingPointResponse>> listByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(meetingPointService.findByRoute(routeId));
    }

    @PostMapping("/routes/{routeId}/meeting-points")
    public ResponseEntity<MeetingPointResponse> create(
            @PathVariable Long routeId,
            @RequestBody @Valid MeetingPointRequest request) {
        return new ResponseEntity<>(meetingPointService.create(routeId, request), HttpStatus.CREATED);
    }

    @GetMapping("/meeting-points/{id}")
    public ResponseEntity<MeetingPointResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingPointService.findById(id));
    }

    @PutMapping("/meeting-points/{id}")
    public ResponseEntity<MeetingPointResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MeetingPointRequest request) {
        return ResponseEntity.ok(meetingPointService.update(id, request));
    }

    @DeleteMapping("/meeting-points/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        meetingPointService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
