package de.campusshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security-Konfiguration:
 * - Public: Startseite, Login/Register + statische Assets
 * - USER/ADMIN: Shop-Seiten + API
 * - ADMIN: /admin/**
 *
 * CSRF:
 * - bleibt für HTML-Forms aktiv (sicher)
 * - wird für /api/** deaktiviert (praktisch für Postman)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ CSRF nur für API ausschalten (Postman), Web-Forms bleiben geschützt
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        new AntPathRequestMatcher("/api/**")
                ))

                .authorizeHttpRequests(auth -> auth

                        // ✅ Static + public pages
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // ✅ Admin Bereich
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ✅ REST API (für USER + ADMIN)
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")

                        // ✅ Shop Seiten (USER + ADMIN)
                        .requestMatchers(
                                "/addresses/**",
                                "/orders/**",
                                "/profile/**",
                                "/cart/**",
                                "/checkout/**",
                                "/web/products/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // ✅ Alles andere: Login nötig
                        .anyRequest().authenticated()
                )

                // ✅ Web-Login (Thymeleaf)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // ✅ Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // ✅ Optional für Postman / Basic Auth
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
