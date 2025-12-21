package de.campusshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Verschlüsselt Passwörter mit BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Zentrale Security-Regeln für die API.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF für REST deaktiviert
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // ===============================
                        // PUBLIC (kein Login nötig)
                        // ===============================
                        .requestMatchers(HttpMethod.GET,
                                "/products", "/products/**",
                                "/categories", "/categories/**"
                        ).permitAll()

                        // ===============================
                        // USER oder ADMIN (eingeloggt)
                        // ===============================
                        .requestMatchers("/carts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/addresses/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders/me").hasAnyRole("USER", "ADMIN")

                        // ===============================
                        // ADMIN
                        // ===============================
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders", "/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        // ===============================
                        // Alles andere
                        // ===============================
                        .anyRequest().authenticated()
                )

                // Basic Auth (z.B. für Postman)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
