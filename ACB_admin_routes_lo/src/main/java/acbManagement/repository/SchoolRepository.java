package acbManagement.repository;

import acbManagement.model.school.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findBySchoolCode(String schoolCode);

    boolean existsBySchoolCode(String schoolCode);

    List<School> findByActiveTrue();

    List<School> findByZoneAndActiveTrue(String zone);
}
