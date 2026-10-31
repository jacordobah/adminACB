package acbManagement.service;

import acbManagement.model.guide.DTO.GuideDetailResponse;
import acbManagement.model.guide.Guide;
import acbManagement.model.guide.GuideRegistrationDto;
import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.Role;
import acbManagement.model.user.User;
import acbManagement.repository.GuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final UserService userService;
    private final GuideRepository guideRepository;

    @Transactional
    public Guide registerCompleteGuide(UserRegistrationDto userDto, GuideRegistrationDto guideDto) {
        // FLUJO OBLIGATORIO 1: Registrar el usuario base primero
        User userBase = userService.createUserBase(userDto, Role.GUIDE);

        // FLUJO OBLIGATORIO 2: Registrar los datos específicos usando el ID generado
        Guide guide = new Guide();
        guide.setContractNumber(guideDto.contractNumber());
        guide.setEmail(guideDto.email());
        guide.setZone(guideDto.zone());
        guide.setUser(userBase); // @MapsId asocia automáticamente el ID del usuario al guide_id

        return guideRepository.save(guide);
    }

    public Page<GuideDetailResponse> getAllGuidesPaged(Pageable pageable) {
        return guideRepository.findByUserActiveTrue(pageable).map(this::convertToDetailResponse);
    }

    public GuideDetailResponse getGuideById(Long id) {
        Guide guide = guideRepository.findById(id)
                .filter(g -> g.getUser().isActive())
                .orElseThrow(() -> new RuntimeException("Guía no encontrado"));
        return convertToDetailResponse(guide);
    }

    @Transactional
    public GuideDetailResponse updateGuide(Long id, GuideRegistrationDto dto) {
        Guide guide = guideRepository.findById(id)
                .filter(g -> g.getUser().isActive())
                .orElseThrow(() -> new RuntimeException("Guía no encontrado"));

        /*guide.getUser().setName(dto.name());
        guide.getUser().setAddress(dto.address());
        guide.getUser().setPhone(dto.phone());*/

        guide.setContractNumber(dto.contractNumber());
        guide.setEmail(dto.email());
        guide.setZone(dto.zone());

        return convertToDetailResponse(guideRepository.save(guide));
    }

    public GuideDetailResponse convertToDetailResponse(Guide guide) {
        return new GuideDetailResponse(
                guide.getGuideId(),
                guide.getUser().getDocumentType().name(),
                guide.getUser().getDocumentNumber(),
                guide.getUser().getName(),
                guide.getUser().getAddress(),
                guide.getUser().getPhone(),
                guide.getContractNumber(),
                guide.getEmail(),
                guide.getZone()
        );
    }
}
