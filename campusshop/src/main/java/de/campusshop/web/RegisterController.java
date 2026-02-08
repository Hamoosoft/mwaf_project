package de.campusshop.web;

import de.campusshop.model.AppUser;
import de.campusshop.model.Role;
import de.campusshop.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(AppUserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String passwordRepeat,
                           jakarta.servlet.http.HttpServletRequest request,
                           org.springframework.ui.Model model) {

        String u = username == null ? "" : username.trim();

        if (u.isBlank()) {
            model.addAttribute("errorMessage", "Benutzername darf nicht leer sein.");
            return "register";
        }

        if (password == null || password.isBlank()) {
            model.addAttribute("errorMessage", "Passwort darf nicht leer sein.");
            return "register";
        }

        if (!password.equals(passwordRepeat)) {
            model.addAttribute("errorMessage", "Passwörter stimmen nicht überein.");
            return "register";
        }

        if (userRepository.existsByUsername(u)) {
            model.addAttribute("errorMessage", "Benutzername ist bereits vergeben.");
            return "register";
        }

        // 1) User speichern
        String hash = passwordEncoder.encode(password);
        AppUser user = new AppUser(u, hash, Role.USER, true);
        userRepository.save(user);

        // 2) AUTO-LOGIN
        var authorities =
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()));

        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user.getUsername(), null, authorities);

        org.springframework.security.core.context.SecurityContext context =
                org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);

        // 3) Session aktualisieren
        request.getSession(true).setAttribute(
                org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        // 4) Redirect (Startseite oder Profil)
        return "redirect:/";
    }

}
