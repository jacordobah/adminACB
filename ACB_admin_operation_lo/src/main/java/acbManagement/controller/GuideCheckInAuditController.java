package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.guide.GuideCheckInAttempt;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.repository.TripGuideAttendanceRepository;
import acbManagement.service.GuideCheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/trip-guides/{tripGuideId}/check-ins")
@RequiredArgsConstructor
public class GuideCheckInAuditController {
    private final GuideCheckInService checkInService;
    private final TripGuideAttendanceRepository tripGuideRepository;

    @GetMapping
    public List<GuideCheckInAttempt> list(
            @PathVariable Long tripGuideId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE", "LEADER", "MANAGER");

        TripGuideAttendance assignment = tripGuideRepository.findById(tripGuideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la asignación de guía " + tripGuideId));
        if ("GUIDE".equals(actor.role())) {
            Long assignedGuideId = assignment.getActualGuideId() != null
                    ? assignment.getActualGuideId() : assignment.getPlannedGuideId();
            if (!Objects.equals(actor.userId(), assignedGuideId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Solo puedes consultar tus intentos de llegada.");
            }
        }
        return checkInService.listAttempts(tripGuideId);
    }
}
