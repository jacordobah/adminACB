package acbManagement.service;

import acbManagement.dto.LoginRegister;
import acbManagement.dto.LoginResponseCreateUpdate;
import acbManagement.dto.LoginUpdateRole;
import acbManagement.dto.LoginUpdateStatus;
import acbManagement.infra.security.CurrentActor;
import acbManagement.model.guide.AuditoryLogin;
import acbManagement.model.guide.Guide;
import acbManagement.model.guide.Role;
import acbManagement.repository.AuditoryLoginRepository;
import acbManagement.repository.GuideRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;
    private final AuditoryLoginRepository auditoryLoginRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponseCreateUpdate create(LoginRegister request, CurrentActor actor) {
        actor.requireAnyRole("MANAGER");

        if (guideRepository.existsById(request.guideId())) {
            throw new IllegalStateException("Ya existe un registro de acceso para el guideId: " + request.guideId());
        }
        if (guideRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("El username ya está en uso");
        }

        Guide guide = new Guide();
        guide.setGuideId(request.guideId());
        guide.setUsername(request.username());
        guide.setPassword(passwordEncoder.encode(request.password()));
        guide.setRole(request.role());
        guide.setActive(true);

        Guide saved = guideRepository.save(guide);

        registerAudit(actor.id(), saved.getGuideId(), "CREATE", null, saved.getRole().name());

        return new LoginResponseCreateUpdate(saved.getGuideId(), saved.getRole().name(), saved.isActive());
    }

    @Transactional
    public LoginResponseCreateUpdate update(LoginUpdateRole request, CurrentActor actor) {
        actor.requireAnyRole("MANAGER");

        Guide guide = guideRepository.findById(request.guideId())
                .orElseThrow(() -> new EntityNotFoundException("Guía no encontrado con ID: " + request.guideId()));

        Role previousRole = guide.getRole();
        Role newRole = request.role();

        if (previousRole == newRole) {
            throw new IllegalStateException("El usuario ya tiene asignado el rol: " + newRole.name());
        }

        guide.setRole(newRole);
        Guide updated = guideRepository.save(guide);

        registerAudit(actor.id(), updated.getGuideId(), "UPDATE_ROLE", previousRole.name(), newRole.name());

        return new LoginResponseCreateUpdate(updated.getGuideId(), updated.getRole().name(), updated.isActive());
    }

    private void registerAudit(Long managerId, Long guideId, String action, String previousRole, String newRole) {
        AuditoryLogin audit = new AuditoryLogin();
        audit.setManagerId(managerId);
        audit.setGuideId(guideId);
        audit.setAction(action);
        audit.setPreviousValue(previousRole);
        audit.setNewValue(newRole);

        auditoryLoginRepository.save(audit);
    }

        @Transactional
        public LoginResponseCreateUpdate updateStatus(LoginUpdateStatus request, CurrentActor actor) {
            actor.requireAnyRole("MANAGER");

            if (actor.id().equals(request.guideId()) && !request.active()) {
                throw new IllegalStateException("No puedes desactivar tu propio usuario");
            }

            Guide guide = guideRepository.findById(request.guideId())
                    .orElseThrow(() -> new EntityNotFoundException("Guía no encontrado con ID: " + request.guideId()));

        boolean previousStatus = guide.isActive();
        boolean newStatus = request.active();

        if (previousStatus == newStatus) {
            String stateText = newStatus ? "activo" : "inactivo";
            throw new IllegalStateException("El usuario ya se encuentra " + stateText);
        }

        guide.setActive(newStatus);
        Guide updated = guideRepository.save(guide);

        String action = newStatus ? "ACTIVATE" : "DEACTIVATE";
        registerAudit(
                actor.id(),
                updated.getGuideId(),
                action,
                String.valueOf(previousStatus),
                String.valueOf(newStatus)
        );

        return new LoginResponseCreateUpdate(
                updated.getGuideId(),
                updated.getRole().name(),
                updated.isActive()
        );
    }

}