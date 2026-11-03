package acbManagement.repository;

import acbManagement.model.event.ActionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionCodeRepository extends JpaRepository<ActionCode, String> {
    List<ActionCode> findAllByActiveTrueOrderByCode();
}
