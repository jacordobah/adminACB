package acbManagement.model.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operation_code")
@Getter
@Setter
@NoArgsConstructor
public class ActionCode {
    @Id
    @Column(name = "code", nullable = false, length = 3)
    private String code;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}
