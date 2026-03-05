package com.orrs.staff;

import com.orrs.manager.AuthenticationManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    private final AuthenticationManager authManager = new AuthenticationManager();

    @Test
    void testConstructor() {
        Staff staff = new Staff("admin", "password123", authManager);
        assertEquals("admin", staff.getUsername());
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testLoginSuccess() {
        Staff staff = new Staff("admin", "password123", authManager);
        assertTrue(staff.login("password123"));
        assertTrue(staff.isAuthenticated());
    }

    @Test
    void testLoginFailure() {
        Staff staff = new Staff("admin", "password123", authManager);
        assertFalse(staff.login("wrong"));
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testLogout() {
        Staff staff = new Staff("admin", "password123", authManager);
        staff.login("password123");
        assertTrue(staff.isAuthenticated());
        staff.logout();
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testToString() {
        Staff staff = new Staff("admin", "password123", authManager);
        String expected = "Staff{username='admin', authenticated=false}";
        assertEquals(expected, staff.toString());
    }
}