package acbManagement.model.guide;

import acbManagement.model.route.DefaultOperation;
import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "guides")
@PrimaryKeyJoinColumn(name = "guide_id") // Enlaza la PK de Guide con la PK de User
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guide extends User {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "zone", nullable = false, length = 100)
    private String zone;

    @Column(name = "contract_number", nullable = false, length = 50)
    private String contractNumber;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DefaultOperation> defaultRoutes;

}
