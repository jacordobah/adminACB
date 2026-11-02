package acbManagement.repository;

import acbManagement.model.user.DocumentType;
import acbManagement.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByDocumentTypeAndDocumentNumberAndActiveTrue(DocumentType type, String number);
}
