package acbManagement.repository;

import acbManagement.model.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    // La pareja jornada + codigo es unica en la base
    Optional<Route> findByShiftCodeAndRouteCode(String shiftCode, String routeCode);

    boolean existsByShiftCodeAndRouteCode(String shiftCode, String routeCode);

    List<Route> findBySchool_SchoolId(Long schoolId);

    List<Route> findByShiftCode(String shiftCode);

    // Rutas donde el guia va como principal o como apoyo
    List<Route> findByGuideMainIdOrGuideSupportId(Long guideMainId, Long guideSupportId);
}
