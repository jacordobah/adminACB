package acbManagement.service;

import acbManagement.infra.exception.DuplicateResourceException;
import acbManagement.infra.exception.ResourceNotFoundException;
import acbManagement.model.meetingPoint.DTO.MeetingPointRequest;
import acbManagement.model.meetingPoint.DTO.MeetingPointResponse;
import acbManagement.model.meetingPoint.MeetingPoint;
import acbManagement.model.route.Route;
import acbManagement.repository.MeetingPointRepository;
import acbManagement.repository.RouteRepository;
import acbManagement.repository.StudentMeetingPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingPointService {

    private final MeetingPointRepository meetingPointRepository;
    private final RouteRepository routeRepository;
    private final StudentMeetingPointRepository studentMeetingPointRepository;

    public List<MeetingPointResponse> findByRoute(Long routeId) {
        getRouteOrFail(routeId);
        return meetingPointRepository.findByRoute_RouteIdOrderByDistanceAsc(routeId)
                .stream().map(this::toResponse).toList();
    }

    public MeetingPointResponse findById(Long id) {
        return toResponse(getOrFail(id));
    }

    @Transactional
    public MeetingPointResponse create(Long routeId, MeetingPointRequest request) {
        Route route = getRouteOrFail(routeId);
        if (meetingPointRepository.existsByRoute_RouteIdAndMeetingPointCode(routeId, request.meetingPointCode())) {
            throw new DuplicateResourceException(
                    "La ruta ya tiene un punto con el codigo " + request.meetingPointCode());
        }
        MeetingPoint point = new MeetingPoint();
        copyInto(point, request);
        point.setRoute(route);
        return toResponse(meetingPointRepository.save(point));
    }

    @Transactional
    public MeetingPointResponse update(Long id, MeetingPointRequest request) {
        MeetingPoint point = getOrFail(id);
        Long routeId = point.getRoute().getRouteId();
        boolean codeChanged = !point.getMeetingPointCode().equals(request.meetingPointCode());
        if (codeChanged
                && meetingPointRepository.existsByRoute_RouteIdAndMeetingPointCode(routeId, request.meetingPointCode())) {
            throw new DuplicateResourceException(
                    "La ruta ya tiene un punto con el codigo " + request.meetingPointCode());
        }
        copyInto(point, request);
        return toResponse(meetingPointRepository.save(point));
    }

    // Borrado real: un punto no tiene baja logica. Se bloquea si hay
    // estudiantes asignados a el, para no dejar asignaciones colgando.
    @Transactional
    public void delete(Long id) {
        MeetingPoint point = getOrFail(id);
        if (!studentMeetingPointRepository.findByMeetingPoint_MeetingPointId(id).isEmpty()) {
            throw new DuplicateResourceException(
                    "No se puede borrar el punto " + id + ": tiene estudiantes asignados");
        }
        meetingPointRepository.delete(point);
    }

    private Route getRouteOrFail(Long routeId) {
        return routeRepository.findById(routeId)
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("No existe la ruta " + routeId));
    }

    private MeetingPoint getOrFail(Long id) {
        return meetingPointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el punto de encuentro " + id));
    }

    private void copyInto(MeetingPoint point, MeetingPointRequest request) {
        point.setMeetingPointCode(request.meetingPointCode());
        point.setAddress(request.address());
        point.setDistance(request.distance());
        point.setLatitude(request.latitude());
        point.setLongitude(request.longitude());
    }

    private MeetingPointResponse toResponse(MeetingPoint p) {
        Route route = p.getRoute();
        return new MeetingPointResponse(
                p.getMeetingPointId(),
                p.getMeetingPointCode(),
                p.getAddress(),
                p.getDistance(),
                p.getLatitude(),
                p.getLongitude(),
                route != null ? route.getRouteId() : null,
                route != null ? route.getRouteCode() : null
        );
    }
}
