package acbManagement.model.event;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "action_codes")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionCode {

    @Id
    @Column(name = "code_id") 
    private int codeId;

    @Column(name = "description", nullable = false, length = 150)
    private String description;
}
