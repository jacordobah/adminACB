package acbManagement.controller;

import acbManagement.dto.operation.TripEventCommand;
import acbManagement.dto.operation.TripEventRequest;
import acbManagement.infra.security.CurrentActor;
import acbManagement.model.trip.TripEvent;
import acbManagement.service.TripAccessService;
import acbManagement.service.TripEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/events")
@RequiredArgsConstructor
public class TripEventController {
    private final TripEventService eventService;
    private final TripAccessService tripAccessService;

    @GetMapping
    public List<TripEvent> list(@PathVariable Long tripId, @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return eventService.listByTrip(tripId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripEvent register(
            @PathVariable Long tripId, @RequestBody TripEventRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        String reporterType;
        String source;
        Long reporterId;
        if ("GEOLOCATION".equals(actor.role())) {
            reporterType = "SYSTEM";
            source = "GEOLOCATION";
            reporterId = null;
        } else {
            tripAccessService.requireCanAccessTrip(tripId, actor);
            reporterType = "GUIDE".equals(actor.role()) ? "GUIDE" : "ADMIN";
            source = "GUIDE".equals(actor.role()) ? "GUIDE_APP" : "ADMIN_UI";
            reporterId = actor.userId();
        }

        TripEventCommand command = new TripEventCommand(
                request.operationCode(), request.occurredAt(), request.description(),
                request.latitude(), request.longitude(), source, request.sourceEventId(),
                reporterId, reporterType);
        return eventService.registerEvent(tripId, command);
    }
}
