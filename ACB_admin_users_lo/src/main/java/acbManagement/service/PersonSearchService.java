package acbManagement.service;
import acbManagement.model.user.DTO.UserResponse;
import acbManagement.model.user.DocumentType;
import acbManagement.model.user.User;
import acbManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonSearchService {

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final GuideService guideService;

    public Object searchByDocument(String documentType, String documentNumber) {
        //busca en la tabla base de usuarios y, dependiendo de su rol, extrae la información unificada correspondiente
        DocumentType type = DocumentType.valueOf(documentType.toUpperCase());
        User user = userRepository.findByDocumentTypeAndDocumentNumberAndActiveTrue(type, documentNumber)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con el documento suministrado" + documentType));

        return switch (user.getRole()) {
            case STUDENT -> studentService.getStudentById(user.getUserId());
            case GUIDE -> guideService.getGuideById(user.getUserId());
            default -> new UserResponse(
                    user.getUserId(),
                    user.getDocumentType().name(),
                    user.getDocumentNumber(),
                    user.getName(),
                    user.getPhone(),
                    user.getRole(),
                    user.isActive()
            );
        };
    }
}
