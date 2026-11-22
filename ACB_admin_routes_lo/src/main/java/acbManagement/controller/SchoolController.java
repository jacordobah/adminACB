package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.school.DTO.SchoolRequest;
import acbManagement.model.school.DTO.SchoolResponse;
import acbManagement.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<List<SchoolResponse>> list(
            @RequestParam(required = false) String zone, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(schoolService.findAll(zone,actor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(schoolService.findById(id, actor));
    }

    @PostMapping
    public ResponseEntity<SchoolResponse> create(@RequestBody @Valid SchoolRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        return new ResponseEntity<>(schoolService.create(request,actor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SchoolRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        return ResponseEntity.ok(schoolService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        schoolService.deactivate(id,actor);
        return ResponseEntity.noContent().build();
    }
}
