package de.campusshop.service;

import de.campusshop.model.AppUser;
import de.campusshop.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security ruft diese Klasse beim Login auf.
 *
 * Ablauf:
 * 1) username kommt rein (z.B. "admin")
 * 2) Wir suchen den User in der DB
 * 3) Wir geben ein Spring-Security UserDetails Objekt zurück
 *
 * Das UserDetails Objekt enthält:
 * - username
 * - passwordHash
 * - authorities (Rollen)
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));

        // Rolle -> Authority (ROLE_ADMIN / ROLE_USER)
        String authority = "ROLE_" + user.getRole().name();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                true,   // accountNonExpired
                true,   // credentialsNonExpired
                true,   // accountNonLocked
                List.of(new SimpleGrantedAuthority(authority))
        );
    }
}
