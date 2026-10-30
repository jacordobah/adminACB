package acbManagement.repository;

import acbManagement.model.guide.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    Optional<Guide> findByUsername(String username);
}