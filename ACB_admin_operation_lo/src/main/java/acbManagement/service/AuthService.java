package acbManagement.service;

import acbManagement.dto.LoginRequest;
import acbManagement.dto.LoginResponse;
import acbManagement.model.guide.Guide;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.username() == null || request.password() == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            Guide guide = (Guide) auth.getPrincipal();
            String token = jwtService.createToken(guide);
            return new LoginResponse(token,
                    new LoginResponse.UserInfo(guide.getUserId(), guide.getName(), guide.getRole().name(), guide.getZone()));
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }
    }
}