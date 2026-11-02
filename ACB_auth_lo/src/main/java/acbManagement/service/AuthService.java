package acbManagement.service;

import acbManagement.dto.LoginRequest;
import acbManagement.dto.LoginResponse;
import acbManagement.model.guide.Guide;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        /*
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashdb = "$2y$10$0HZhIkxULORjuJEyidsnY.31MatWZSr8MCDRCZBce1lA4oWN1F6N2";
        String prueba = "admin2026";
        System.out.println(passwordEncoder.encode("admin2026"));
        System.out.println(passwordEncoder.matches(hashdb,prueba)?"coincide":"no coincide");
        */
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            Guide guide = (Guide) auth.getPrincipal();
            String token = jwtService.createToken(guide);
            return new LoginResponse(token,
                    new LoginResponse.UserInfo(guide.getGuideId(),guide.getRole().name()));
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
        }
    }
}