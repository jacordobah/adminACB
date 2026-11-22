package acbManagement.controller;

import acbManagement.infra.securiity.CurrentActor;
import acbManagement.model.guide.DTO.GuideDetailResponse;
import acbManagement.model.guide.Guide;
import acbManagement.model.guide.GuideRegistrationDto;
import acbManagement.model.user.DTO.GuideRequest;
import acbManagement.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guides")
@RequiredArgsConstructor
public class GuideController {
    private final GuideService guideService;

    @PostMapping
    public ResponseEntity<Guide> createGuide(@RequestBody @Valid GuideRequest request,
                                             @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        Guide newGuide = guideService.registerCompleteGuide(request.user(), request.guide(),actor);
        return new ResponseEntity<>(newGuide, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GuideDetailResponse>> getAllGuides(Pageable pageable,
                                                                  @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(guideService.getAllGuidesPaged(pageable, actor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideDetailResponse> getGuideById(@PathVariable Long id,
                                                            @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER","MANAGER");
        return ResponseEntity.ok(guideService.getGuideById(id,actor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideDetailResponse> updateGuide(@PathVariable Long id, @RequestBody GuideRegistrationDto dto,
                                                           @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(guideService.updateGuide(id, dto, actor));
    }
}
