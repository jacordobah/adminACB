package acbManagement.controller;

import acbManagement.dto.operation.CreateTripRequest;
import acbManagement.dto.operation.ReplaceGuideRequest;
import acbManagement.dto.operation.RouteAttendanceRow;
import acbManagement.dto.operation.TripLifecycleRequest;
import acbManagement.infra.security.CurrentActor;
import acbManagement.model.trip.Trip;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.service.TripAccessService;
import acbManagement.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;
    private final TripAccessService tripAccessService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip create(@RequestBody CreateTripRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor.from(jwt).requireAnyRole("LEADER", "MANAGER");
        return tripService.create(request);
    }

    @GetMapping("/{tripId}")
    public Trip get(@PathVariable Long tripId, @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return tripService.get(tripId);
    }

    @GetMapping("/{tripId}/guides")
    public java.util.List<TripGuideAttendance> listGuides(
            @PathVariable Long tripId, @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return tripService.listGuides(tripId);
    }

    @PutMapping("/{tripId}/guides/{tripGuideId}/replacement")
    public TripGuideAttendance replaceGuide(
            @PathVariable Long tripId, @PathVariable Long tripGuideId,
            @RequestBody ReplaceGuideRequest request, @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("LEADER", "MANAGER");
        return tripService.replaceGuide(tripId, tripGuideId, request, actor.userId());
    }

    @GetMapping
    public java.util.List<Trip> list(
            @RequestParam LocalDate date,
            @RequestParam(required = false) Long guideId,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE", "LEADER", "MANAGER");
        // Un guia solo ve sus propios recorridos, aunque no envie guideId.
        Long effectiveGuideId = "GUIDE".equals(actor.role()) ? actor.userId() : guideId;
        return tripService.listByDate(date, effectiveGuideId);
    }

    @GetMapping("/by-route/{routeId}")
    public Trip getByRouteAndDate(
            @PathVariable Long routeId, @RequestParam LocalDate date,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor.from(jwt).requireAnyRole("LEADER", "MANAGER");
        return tripService.getByRouteAndDate(routeId, date);
    }

    @GetMapping("/by-route/{routeId}/attendance")
    public java.util.List<RouteAttendanceRow> listAttendanceByRoute(
            @PathVariable Long routeId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @AuthenticationPrincipal Jwt jwt) {
        CurrentActor.from(jwt).requireAnyRole("LEADER", "MANAGER");
        return tripService.listAttendanceByRoute(routeId, from, to);
    }

    @PostMapping("/{tripId}/start")
    public Trip start(
            @PathVariable Long tripId, @RequestBody(required = false) TripLifecycleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return tripService.start(tripId, request == null ? null : request.occurredAt());
    }

    @PostMapping("/{tripId}/finish")
    public Trip finish(
            @PathVariable Long tripId, @RequestBody(required = false) TripLifecycleRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        tripAccessService.requireCanAccessTrip(tripId, CurrentActor.from(jwt));
        return tripService.finish(tripId, request == null ? null : request.occurredAt());
    }
}
