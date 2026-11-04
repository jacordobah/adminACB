package acbManagement.controller;

import acbManagement.model.route.DTO.AssignGuidesRequest;
import acbManagement.model.route.DTO.RouteRequest;
import acbManagement.model.route.DTO.RouteResponse;
import acbManagement.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteResponse>> list(
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String zone) {
        return ResponseEntity.ok(routeService.findAll(schoolId, zone));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody @Valid RouteRequest request) {
        return new ResponseEntity<>(routeService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid RouteRequest request) {
        return ResponseEntity.ok(routeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        routeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/guides")
    public ResponseEntity<RouteResponse> assignGuides(
            @PathVariable Long id,
            @RequestBody @Valid AssignGuidesRequest request) {
        return ResponseEntity.ok(routeService.assignGuides(id, request));
    }
}
