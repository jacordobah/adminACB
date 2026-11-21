package acbManagement.repository;

import acbManagement.model.guide.AuditoryLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoryLoginRepository extends JpaRepository<AuditoryLogin,Long> {
}
