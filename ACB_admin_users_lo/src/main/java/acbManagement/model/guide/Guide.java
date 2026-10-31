package acbManagement.model.guide;

import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "guides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guide extends User {

    @Id
    @Column(name = "guide_id")
    private Long guideId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String zone;

    @Column(name = "contract_number", nullable = false, length = 50)
    private String contractNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "guide_id")
    private User user;
}