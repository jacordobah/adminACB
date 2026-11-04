package acbManagement.repository;

import acbManagement.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByRoute_RouteId(Long routeId);

    List<Student> findByRoute_RouteIdAndActiveTrue(Long routeId);

    long countByRoute_RouteId(Long routeId);
}
