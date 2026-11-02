package acbManagement.model.user.DTO;

import acbManagement.model.user.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String name;
    private String address;
    private String phone;
    private Role role;
}
