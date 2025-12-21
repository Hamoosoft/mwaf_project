package de.campusshop.model;

import jakarta.persistence.*;

/**
 * Entity für einen Benutzer der Anwendung.
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_users_username",
                columnNames = "username"
        )
)
public class AppUser extends BaseEntity {

    /**
     * Benutzername für den Login.
     */
    @Column(nullable = false)
    private String username;

    /**
     * Passwort als BCrypt-Hash.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Rolle des Benutzers (USER / ADMIN).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    /**
     * Ist der Benutzer aktiv?
     */
    @Column(nullable = false)
    private boolean enabled = true;

    public AppUser() {
    }

    public AppUser(String username, String passwordHash, Role role, boolean enabled) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = enabled;
    }

    // ----- Getter & Setter -----

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
