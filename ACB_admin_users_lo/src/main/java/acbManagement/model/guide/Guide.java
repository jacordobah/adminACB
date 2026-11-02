package acbManagement.model.guide;

import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "guides")
@PrimaryKeyJoinColumn(name = "guide_id", referencedColumnName = "user_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guide extends User {

    public Guide(UserRegistrationDto user,GuideRegistrationDto guide){
        this.contractNumber = guide.contractNumber();
        this.email = guide.email();
        this.zone = guide.email();
        this.setName(user.name());
        this.setAddress(user.address());
        this.setDocumentNumber(user.documentNumber());
        this.setDocumentType(user.documentType());
        this.setRole(user.role());
        this.setPhone(user.phone());
        this.setActive(true);
    }

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String zone;

    @Column(name = "contract_number", nullable = false, length = 50)
    private String contractNumber;
}