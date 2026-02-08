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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Für klassische HTML-Forms (Login/Register) ist CSRF eigentlich gut.
                // ABER: wenn du viel mit Postman/REST testest, lässt man es oft erstmal aus.
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**").permitAll()

                        // REST API
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")

                        // Seiten
                        .requestMatchers("/addresses", "/orders", "/profile", "/cart")
                        .hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )


                // Web-Login (Thymeleaf Login Page)
                .formLogin(form -> form
                        .loginPage("/login")
                        // wenn Login erfolgreich-> auf Startseite (du kannst auch "/products" nehmen)
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                // Optional: damit Postman weiterhin geht
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
