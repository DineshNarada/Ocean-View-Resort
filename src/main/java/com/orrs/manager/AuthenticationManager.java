package com.orrs.manager;

/**
 * Handles user authentication for the Ocean View Resort system.
 * Verifies username and password credentials for staff access.
 */
public class AuthenticationManager {
    // Simple in-memory credentials for demonstration
    // In production, this would interface with a secure authentication service/database
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password123";

    /**
     * Authenticates a user based on provided credentials.
     *
     * @param username the username to authenticate
     * @param password the password to verify
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD);
    }
}
