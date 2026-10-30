package acbManagement.service;

import acbManagement.model.guide.Guide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationHours;

    public JwtService(JwtEncoder jwtEncoder,
                      @Value("${acb.jwt.expiration-hours}") long expirationHours) {
        this.jwtEncoder = jwtEncoder;
        this.expirationHours = expirationHours;
    }

    public String createToken(Guide guide) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(guide.getUserId()))
                .issuedAt(now)
                .expiresAt(now.plus(expirationHours, ChronoUnit.HOURS))
                .claim("name", guide.getName())
                .claim("role", guide.getRole().name())
                .claim("zone", guide.getZone())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}