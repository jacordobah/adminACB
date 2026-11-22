package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.route.DTO.AssignGuidesRequest;
import acbManagement.model.route.DTO.RouteRequest;
import acbManagement.model.route.DTO.RouteResponse;
import acbManagement.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
            @RequestParam(required = false) String zone, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(routeService.findAll(schoolId, zone, actor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(routeService.findById(id,actor));
    }

    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody @Valid RouteRequest request,
                                                @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        return new ResponseEntity<>(routeService.create(request,actor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid RouteRequest request,@AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        return ResponseEntity.ok(routeService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        routeService.deactivate(id, actor);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/guides")
    public ResponseEntity<RouteResponse> assignGuides(
            @PathVariable Long id,
            @RequestBody @Valid AssignGuidesRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(routeService.assignGuides(id, request, actor));
    }
}
