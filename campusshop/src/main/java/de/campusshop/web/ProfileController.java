package de.campusshop.web;

import de.campusshop.model.AppUser;
import de.campusshop.repository.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        String username = auth.getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User nicht gefunden: " + username));

        // in deiner UI: E-Mail = username (read-only)
        model.addAttribute("email", user.getUsername());

        // Benutzername editierbar (A-Variante)
        model.addAttribute("name", user.getUsername());

        return "profile";
    }

    @PostMapping("/profile")
    public String updateUsername(Authentication auth,
                                 @RequestParam("name") String newUsername,
                                 jakarta.servlet.http.HttpServletRequest request,
                                 jakarta.servlet.http.HttpServletResponse response,
                                 Model model) {

        String username = auth.getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User nicht gefunden: " + username));

        String trimmed = newUsername == null ? "" : newUsername.trim();

        // View wieder befüllen (falls Fehler)
        model.addAttribute("email", user.getUsername());

        if (trimmed.isBlank()) {
            model.addAttribute("profileMessage", "Fehler: Benutzername darf nicht leer sein.");
            model.addAttribute("profileError", true);
            model.addAttribute("name", user.getUsername());
            return "profile";
        }

        if (!trimmed.equalsIgnoreCase(user.getUsername()) && userRepository.existsByUsername(trimmed)) {
            model.addAttribute("profileMessage", "Fehler: Dieser Benutzername ist bereits vergeben.");
            model.addAttribute("profileError", true);
            model.addAttribute("name", user.getUsername());
            return "profile";
        }

        // Speichern
        user.setUsername(trimmed);
        userRepository.save(user);

        // ✅ Sauber ausloggen (Session, SecurityContext)
        try {
            request.logout();
        } catch (Exception ignored) {
            // fallback: wenn logout() nicht klappt, machen wir trotzdem redirect
        }

        // Redirect zur Login-Seite, damit alles konsistent ist
        return "redirect:/login?logout";
    }


    @PostMapping("/profile/password")
    public String changePassword(Authentication auth,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPasswordRepeat") String newPasswordRepeat,
                                 Model model) {

        String username = auth.getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User nicht gefunden: " + username));

        // View wieder befüllen
        model.addAttribute("email", user.getUsername());
        model.addAttribute("name", user.getUsername());

        if (currentPassword == null || currentPassword.isBlank()
                || newPassword == null || newPassword.isBlank()
                || newPasswordRepeat == null || newPasswordRepeat.isBlank()) {

            model.addAttribute("passwordMessage", "Fehler: Bitte alle Felder ausfüllen.");
            model.addAttribute("passwordError", true);
            return "profile";
        }

        if (!newPassword.equals(newPasswordRepeat)) {
            model.addAttribute("passwordMessage", "Fehler: Die neuen Passwörter stimmen nicht überein.");
            model.addAttribute("passwordError", true);
            return "profile";
        }

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            model.addAttribute("passwordMessage", "Fehler: Aktuelles Passwort ist falsch.");
            model.addAttribute("passwordError", true);
            return "profile";
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("passwordMessage", "Passwort wurde erfolgreich geändert.");
        model.addAttribute("passwordError", false);

        return "profile";
    }
}
