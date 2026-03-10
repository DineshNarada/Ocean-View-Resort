package com.orrs.staff;

import com.orrs.manager.AuthenticationManager;

/**
 * Represents a staff member who interacts with the Ocean View Resort reservation system.
 * Contains credentials and login functionality.
 * 
 * REFACTORED: Now works with database-driven authentication.
 * Login method verifies credentials against database via AuthenticationManager.
 * Authentication is no longer hardcoded; it comes from the Staff table.
 */
public class Staff {
    private String username;
    @SuppressWarnings("unused")
    private String passwordHash;
    private boolean authenticated;
    private AuthenticationManager authManager;

    /**
     * Constructs a Staff member with credentials and authentication manager.
     *
     * @param username the staff member's username
     * @param passwordHash the hashed password for security
     * @param authManager the authentication manager for database-driven login verification
     */
    public Staff(String username, String passwordHash, AuthenticationManager authManager) {
        this.username = username;
        this.passwordHash = passwordHash;  // Stored for reference
        this.authManager = authManager;
        this.authenticated = false;
    }

    /**
     * Attempts to log in the staff member.
     * REFACTORED: Now validates against database credentials.
     *
     * @param password the plaintext password to verify against database
     * @return true if authentication succeeds (staff found with matching password), false otherwise
     */
    public boolean login(String password) {
        try {
            // REFACTORED: authenticate() now queries database and returns Staff object
            // If Staff is returned (not null), authentication succeeded
            com.orrs.domain.Staff authenticatedStaff = authManager.authenticate(username, password);
            
            if (authenticatedStaff != null) {
                this.authenticated = true;
                return true;
            }
            return false;
        } catch (Exception e) {
            // Log error in production
            System.err.println("Login error: " + e.getMessage());
            return false;
        }
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
