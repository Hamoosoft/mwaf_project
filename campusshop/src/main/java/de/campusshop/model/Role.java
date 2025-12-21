package de.campusshop.model;

/**
 * Rollen im System.
 * Spring Security nutzt intern oft "ROLE_<NAME>".
 * Wenn wir roles("ADMIN") setzen, wird daraus "ROLE_ADMIN".
 */
public enum Role {
    USER,
    ADMIN
}
