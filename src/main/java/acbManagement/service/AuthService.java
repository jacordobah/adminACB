package acbManagement.service;

import acbManagement.dto.LoginRequest;
import acbManagement.dto.LoginResponse;
import acbManagement.model.guide.Guide;
import acbManagement.repository.GuideRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final GuideRepository guideRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(GuideRepository guideRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.guideRepository = guideRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.username() == null || request.password() == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }
        Guide guide = guideRepository.findByUsername(request.username())
                .filter(g -> g.isActive() && passwordEncoder.matches(request.password(), g.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos."));

        String token = jwtService.createToken(guide);
        return new LoginResponse(token,
                new LoginResponse.UserInfo(guide.getUserId(), guide.getName(), guide.getRole().name(), guide.getZone()));
    }
}