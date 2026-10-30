package acbManagement.service;

import acbManagement.repository.GuideRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GuideDetailsService implements UserDetailsService {

    private final GuideRepository guideRepository;

    public GuideDetailsService(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return guideRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña incorrectos."));
    }
}