package acbManagement.service;

import acbManagement.infra.exception.DuplicateResourceException;
import acbManagement.infra.exception.ResourceNotFoundException;
import acbManagement.model.route.DTO.*;
import acbManagement.model.route.Route;
import acbManagement.model.school.School;
import acbManagement.repository.RouteRepository;
import acbManagement.repository.SchoolRepository;
import acbManagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;

    public List<RouteResponse> findAll(Long schoolId, String zone) {
        List<Route> routes;
        if (schoolId != null) {
            routes = routeRepository.findBySchool_SchoolIdAndActiveTrue(schoolId);
        } else if (zone != null && !zone.isBlank()) {
            routes = routeRepository.findBySchool_ZoneAndActiveTrue(zone);
        } else {
            routes = routeRepository.findByActiveTrue();
        }
        return routes.stream().map(this::toResponse).toList();
    }

    public RouteResponse findById(Long id) {
        return toResponse(getActiveOrFail(id));
    }

    @Transactional
    public RouteResponse create(RouteRequest request) {
        if (routeRepository.existsByShiftCodeAndRouteCode(request.shiftCode(), request.routeCode())) {
            throw new DuplicateResourceException(
                    "Ya existe la ruta " + request.routeCode() + " en la jornada " + request.shiftCode());
        }
        School school = getSchoolOrFail(request.schoolId());

        Route route = new Route();
        route.setRouteCode(request.routeCode());
        route.setShiftCode(request.shiftCode());
        route.setDescription(request.description());
        route.setScheduledStartTime(request.scheduledStartTime());
        route.setSchool(school);
        route.setActive(true);
        return toResponse(routeRepository.save(route));
    }

    @Transactional
    public RouteResponse update(Long id, RouteRequest request) {
        Route route = getActiveOrFail(id);

        boolean pairChanged = !route.getShiftCode().equals(request.shiftCode())
                || !route.getRouteCode().equals(request.routeCode());
        if (pairChanged
                && routeRepository.existsByShiftCodeAndRouteCode(request.shiftCode(), request.routeCode())) {
            throw new DuplicateResourceException(
                    "Ya existe la ruta " + request.routeCode() + " en la jornada " + request.shiftCode());
        }
        School school = getSchoolOrFail(request.schoolId());

        route.setRouteCode(request.routeCode());
        route.setShiftCode(request.shiftCode());
        route.setDescription(request.description());
        route.setScheduledStartTime(request.scheduledStartTime());
        route.setSchool(school);
        return toResponse(routeRepository.save(route));
    }

    @Transactional
    public void deactivate(Long id) {
        Route route = getActiveOrFail(id);
        route.setActive(false);
        routeRepository.save(route);
    }

    // Asigna uno o dos guias a la ruta. Exactamente uno debe ser el principal;
    // el otro, si viene, queda como apoyo.
    @Transactional
    public RouteResponse assignGuides(Long id, AssignGuidesRequest request) {
        Route route = getActiveOrFail(id);

        List<AssignGuidesRequest.Item> primaries = request.guides().stream()
                .filter(AssignGuidesRequest.Item::primary).toList();
        if (primaries.size() != 1) {
            throw new IllegalArgumentException("debe haber exactamente un guia principal");
        }
        List<AssignGuidesRequest.Item> supports = request.guides().stream()
                .filter(g -> !g.primary()).toList();
        if (supports.size() > 1) {
            throw new IllegalArgumentException("una ruta admite maximo un guia de apoyo");
        }

        AssignGuidesRequest.Item main = primaries.get(0);
        route.setGuideMainId(main.guideId());
        route.setGuideMainName(main.name());

        if (supports.isEmpty()) {
            route.setGuideSupportId(null);
            route.setGuideSupportName(null);
        } else {
            AssignGuidesRequest.Item support = supports.get(0);
            if (support.guideId().equals(main.guideId())) {
                throw new IllegalArgumentException("el guia principal y el de apoyo no pueden ser el mismo");
            }
            route.setGuideSupportId(support.guideId());
            route.setGuideSupportName(support.name());
        }
        return toResponse(routeRepository.save(route));
    }

    private Route getActiveOrFail(Long id) {
        return routeRepository.findById(id)
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("No existe la ruta " + id));
    }

    private School getSchoolOrFail(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .filter(s -> Boolean.TRUE.equals(s.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("No existe el colegio " + schoolId));
    }

    private RouteResponse toResponse(Route route) {
        List<RouteGuideDto> guides = new ArrayList<>();
        if (route.getGuideMainId() != null) {
            guides.add(new RouteGuideDto(route.getGuideMainId(), route.getGuideMainName(), true));
        }
        if (route.getGuideSupportId() != null) {
            guides.add(new RouteGuideDto(route.getGuideSupportId(), route.getGuideSupportName(), false));
        }
        School school = route.getSchool();
        long studentCount = studentRepository.countByRoute_RouteId(route.getRouteId());

        return new RouteResponse(
                route.getRouteId(),
                route.getRouteCode(),
                route.getShiftCode(),
                route.getDescription(),
                school != null ? school.getSchoolId() : null,
                school != null ? school.getName() : null,
                school != null ? school.getZone() : null,
                route.getScheduledStartTime(),
                guides,
                studentCount,
                route.getActive()
        );
    }
}
