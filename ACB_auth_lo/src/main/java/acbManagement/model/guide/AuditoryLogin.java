package acbManagement.model.guide;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "auditory_login")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoryLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "guide_id", nullable = false)
    private Long guideId;

    @Column(name = "action", nullable = false, length = 30)
    private String action;

    @Column(name = "previous_value", length = 50)
    private String previousValue;

    @Column(name = "new_value", nullable = false, length = 50)
    private String newValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}