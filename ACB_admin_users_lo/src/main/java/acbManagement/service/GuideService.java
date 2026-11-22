package acbManagement.service;

import acbManagement.infra.securiity.CurrentActor;
import acbManagement.model.guide.DTO.GuideDetailResponse;
import acbManagement.model.guide.Guide;
import acbManagement.model.guide.GuideRegistrationDto;
import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.Role;
import acbManagement.repository.GuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;

    @Transactional
    public Guide registerCompleteGuide(UserRegistrationDto userDto, GuideRegistrationDto guideDto, CurrentActor actor) {
        actor.requireAnyRole("MANAGER");
        // FLUJO OBLIGATORIO 1: Registrar el usuario base primero

        Guide guide = new Guide(userDto, guideDto);

        return guideRepository.save(guide);
    }

    public Page<GuideDetailResponse> getAllGuidesPaged(Pageable pageable, CurrentActor actor) {
        actor.requireAnyRole("LEADER","MANAGER");
        return guideRepository.findByActiveTrue(pageable).map(this::convertToDetailResponse);
    }

    public GuideDetailResponse getGuideById(Long id, CurrentActor actor) {
        actor.requireAnyRole("LEADER","MANAGER");
        Guide guide = guideRepository.findById(id)
                .filter(g -> g.isActive())
                .orElseThrow(() -> new RuntimeException("Guía no encontrado"));
        return convertToDetailResponse(guide);
    }

    @Transactional
    public GuideDetailResponse updateGuide(Long id, GuideRegistrationDto dto, CurrentActor actor) {
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        Guide guide = guideRepository.findById(id)
                .filter(g -> g.isActive())
                .orElseThrow(() -> new RuntimeException("Guía no encontrado"));
        if(Role.GUIDE.name().equals(actor.role()) && !actor.id().equals(guide.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permiso para editar este perfil.");
        }
        guide.setContractNumber(dto.contractNumber());
        guide.setEmail(dto.email());
        guide.setZone(dto.zone());

        return convertToDetailResponse(guideRepository.save(guide));
    }

    public GuideDetailResponse convertToDetailResponse(Guide guide) {
        return new GuideDetailResponse(
                guide.getUserId(),
                guide.getDocumentType().name(),
                guide.getDocumentNumber(),
                guide.getName(),
                guide.getRole().toString(),
                guide.getAddress(),
                guide.getPhone(),
                guide.getContractNumber(),
                guide.getEmail(),
                guide.getZone()
        );
    }
}
