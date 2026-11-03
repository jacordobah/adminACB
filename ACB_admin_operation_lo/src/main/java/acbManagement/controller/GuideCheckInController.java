package acbManagement.controller;

import acbManagement.dto.operation.GeolocationCheckInRequest;
import acbManagement.dto.operation.GuideCheckInCommand;
import acbManagement.infra.security.CurrentActor;
import acbManagement.model.guide.GuideCheckInAttempt;
import acbManagement.service.GuideCheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/trip-guides/{tripGuideId}/check-ins")
@RequiredArgsConstructor
public class GuideCheckInController {
    private final GuideCheckInService checkInService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuideCheckInAttempt recordGeolocationResult(
            @PathVariable Long tripGuideId, @RequestBody GeolocationCheckInRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        // Auth debe emitir una identidad de servicio con role=GEOLOCATION.
        CurrentActor.from(jwt).requireAnyRole("GEOLOCATION");
        GuideCheckInCommand command = new GuideCheckInCommand(
                request.attemptedAt(), request.latitude(), request.longitude(),
                request.distanceToStartM(), request.allowedRadiusM(), request.withinRadius(),
                "GEOLOCATION", request.sourceEventId());
        return checkInService.registerAttempt(tripGuideId, request.guideId(), command);
    }
}
