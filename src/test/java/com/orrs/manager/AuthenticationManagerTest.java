package com.orrs.manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationManagerTest {

    private final AuthenticationManager authManager = new AuthenticationManager();

    @Test
    void testAuthenticateValidCredentials() {
        assertTrue(authManager.authenticate("admin", "password123"));
    }

    @Test
    void testAuthenticateInvalidUsername() {
        assertFalse(authManager.authenticate("wrong", "password123"));
    }

    @Test
    void testAuthenticateInvalidPassword() {
        assertFalse(authManager.authenticate("admin", "wrong"));
    }

    @Test
    void testAuthenticateNullUsername() {
        assertFalse(authManager.authenticate(null, "password123"));
    }

    @Test
    void testAuthenticateNullPassword() {
        assertFalse(authManager.authenticate("admin", null));
    }
}