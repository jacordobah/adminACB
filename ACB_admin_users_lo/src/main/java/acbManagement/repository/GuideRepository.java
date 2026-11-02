package acbManagement.repository;

import acbManagement.model.guide.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    Page<Guide> findByActiveTrue(Pageable pageable);
}