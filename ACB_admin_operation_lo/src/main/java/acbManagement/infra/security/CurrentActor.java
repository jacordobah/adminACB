package acbManagement.infra.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

public record CurrentActor(Long userId, String role) {
    public static CurrentActor from(Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se recibió un token válido.");
        }
        String role = jwt.getClaimAsString("role");
        if (role == null || role.isBlank()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El token no contiene el rol del usuario.");
        }
        role = role.toUpperCase();
        // La identidad interna de Geolocalización es un servicio y no un ID de usuario.
        if ("GEOLOCATION".equals(role)) return new CurrentActor(null, role);
        if (jwt.getSubject() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no contiene el ID del usuario.");
        }
        try {
            Long id = Long.valueOf(jwt.getSubject());
            return new CurrentActor(id, role);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El subject del token no es un ID numérico.");
        }
    }

    public void requireAnyRole(String... allowedRoles) {
        boolean allowed = Arrays.stream(allowedRoles).anyMatch(role::equals);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes el rol requerido para esta operación.");
        }
    }
}
