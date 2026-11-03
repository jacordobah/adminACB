package acbManagement.service;

import acbManagement.infra.security.CurrentActor;
import acbManagement.repository.TripGuideAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TripAccessService {
    private final TripGuideAttendanceRepository tripGuideRepository;

    public void requireCanAccessTrip(Long tripId, CurrentActor actor) {
        actor.requireAnyRole("GUIDE", "LEADER", "MANAGER");
        if ("GUIDE".equals(actor.role())) {
            boolean assigned = tripGuideRepository.findAllByTrip_TripIdOrderByAssignmentRole(tripId).stream()
                    .anyMatch(assignment -> Objects.equals(assignment.getActualGuideId(), actor.userId())
                            || (assignment.getActualGuideId() == null
                            && Objects.equals(assignment.getPlannedGuideId(), actor.userId())));
            if (!assigned) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "El guía no está asignado a este recorrido.");
            }
        }
    }
}
