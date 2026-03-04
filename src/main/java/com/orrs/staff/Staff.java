package com.orrs.staff;

import com.orrs.manager.AuthenticationManager;

/**
 * Represents a staff member who interacts with the Ocean View Resort reservation system.
 * Contains credentials and login functionality.
 */
public class Staff {
    private String username;
    private String passwordHash;
    private boolean authenticated;
    private AuthenticationManager authManager;

    /**
     * Constructs a Staff member with credentials.
     *
     * @param username the staff member's username
     * @param passwordHash the hashed password for security
     * @param authManager the authentication manager for login verification
     */
    public Staff(String username, String passwordHash, AuthenticationManager authManager) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.authManager = authManager;
        this.authenticated = false;
    }

    /**
     * Attempts to log in the staff member.
     *
     * @param password the plaintext password to verify
     * @return true if authentication succeeds, false otherwise
     */
    public boolean login(String password) {
        if (authManager.authenticate(username, password)) {
            this.authenticated = true;
            return true;
        }
        return false;
    }

    /**
     * Logs out the staff member.
     */
    public void logout() {
        this.authenticated = false;
    }

    /**
     * Checks if the staff member is currently authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "username='" + username + '\'' +
                ", authenticated=" + authenticated +
                '}';
    }
}
