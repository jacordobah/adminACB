package acbManagement.service;


import acbManagement.model.user.DTO.UserCreationDto;
import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.DTO.UserResponse;
import acbManagement.model.user.DTO.UserUpdateDto;
import acbManagement.model.user.Role;
import acbManagement.model.user.User;
import acbManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUserDirect(UserCreationDto dto) {
        // Validación: Por esta ruta no se deben crear estudiantes ni guías
        if (dto.role() == Role.STUDENT || dto.role() == Role.GUIDE) {
            throw new IllegalArgumentException("Para registrar estudiantes o guías use sus respectivos endpoints especializados.");
        }

        User user = new User();
        user.setDocumentType(dto.documentType());
        user.setDocumentNumber(dto.documentNumber());
        user.setName(dto.name());
        user.setAddress(dto.address());
        user.setPhone(dto.phone());
        user.setRole(dto.role());
        user.setActive(true);

        return convertToResponse(userRepository.save(user));
    }

    // metodo interno que usan StudentService y GuideService para crear su base
    @Transactional
    public User createUserBase(UserRegistrationDto dto, Role role) {
        User user = new User();
        user.setDocumentType(dto.documentType());
        user.setDocumentNumber(dto.documentNumber());
        user.setName(dto.name());
        user.setAddress(dto.address());
        user.setPhone(dto.phone());
        user.setRole(role);
        user.setActive(true);
        return userRepository.save(user);
    }

    public Page<UserResponse> getAllActiveUsersPaged(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public UserResponse getUserResponseById(Long id) {
        return convertToResponse(findById(id));
    }

    // Método de utilidad para buscar la entidad real internamente
    public User findById(Long id) {
        return userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo en el sistema"));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateDto dto) {
        User user = findById(id);
        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        return convertToResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteLogically(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActive(false);
        userRepository.save(user);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getDocumentType().name(),
                user.getDocumentNumber(),
                user.getName(),
                user.getPhone(),
                user.getRole(),
                user.isActive()
        );
    }
}

