package acbManagement.controller;

import acbManagement.infra.security.CurrentActor;
import acbManagement.model.guide.GuideFaultCycle;
import acbManagement.model.guide.MajorFault;
import acbManagement.model.guide.MinorFault;
import acbManagement.service.GuideFaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guide-faults")
@RequiredArgsConstructor
public class GuideFaultController {
    private final GuideFaultService faultService;

    @GetMapping("/guides/{guideId}/cycles")
    public List<GuideFaultCycle> listCycles(
            @PathVariable Long guideId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE", "LEADER", "MANAGER");
        if ("GUIDE".equals(actor.role()) && !actor.userId().equals(guideId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Solo puedes consultar tus ciclos de faltas.");
        }
        return faultService.listCyclesForGuide(guideId);
    }

    @GetMapping("/cycles/{cycleId}/minor")
    public List<MinorFault> listMinorFaults(
            @PathVariable Long cycleId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER", "MANAGER");
        return faultService.listMinorFaults(cycleId);
    }

    @GetMapping("/major/{majorFaultId}")
    public MajorFault getMajorFault(
            @PathVariable Long majorFaultId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER", "MANAGER");
        return faultService.getMajorFault(majorFaultId);
    }

    @PutMapping("/minor/{minorFaultId}/notified")
    public MinorFault markMinorNotified(
            @PathVariable Long minorFaultId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER", "MANAGER");
        return faultService.markMinorFaultNotified(minorFaultId, actor.userId());
    }

    @PutMapping("/major/{majorFaultId}/notified")
    public MajorFault markMajorNotified(
            @PathVariable Long majorFaultId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");
        return faultService.markMajorFaultNotified(majorFaultId, actor.userId());
    }

    @PutMapping("/major/{majorFaultId}/minuted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markMajorMinuted(
            @PathVariable Long majorFaultId, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor.from(jwt).requireAnyRole("MANAGER");
        faultService.markMajorFaultMinuted(majorFaultId);
    }
}
