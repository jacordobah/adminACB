package acbManagement.controller;

import acbManagement.dto.*;
import acbManagement.infra.security.CurrentActor;
import acbManagement.service.AuthService;
import acbManagement.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GuideService guideService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseCreateUpdate create(
            @Valid @RequestBody LoginRegister register,
            @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");

        return guideService.create(register, actor);
    }

    @PatchMapping("/role")
    public LoginResponseCreateUpdate update(
            @Valid @RequestBody LoginUpdateRole request,
            @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");

        return guideService.update(request, actor);
    }

    @PatchMapping("/status")
    public LoginResponseCreateUpdate updateStatus(
            @Valid @RequestBody LoginUpdateStatus request,
            @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("MANAGER");

        return guideService.updateStatus(request, actor);
    }

}