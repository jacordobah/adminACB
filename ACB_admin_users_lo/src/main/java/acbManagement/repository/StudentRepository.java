package acbManagement.repository;

import acbManagement.model.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findBySchoolIdAndUserActiveTrue(Long schoolId, Pageable pageable);
    Page<Student> findByUserActiveTrue(Pageable pageable);
}
