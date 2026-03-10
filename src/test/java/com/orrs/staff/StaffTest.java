package com.orrs.staff;

import com.orrs.dao.IStaffDAO;
import com.orrs.manager.AuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test suite for Staff class
 * 
 * REFACTORED: Tests now verify database-driven authentication.
 * Previously tested hardcoded credentials; now tests DAO integration.
 * Uses Mockito to mock StaffDAO for isolated testing.
 */
class StaffTest {

    @Mock
    private IStaffDAO mockStaffDAO;
    
    private AuthenticationManager authManager;
    
    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
        
        // Create AuthenticationManager with mocked DAO
        authManager = new AuthenticationManager(mockStaffDAO);
    }

    @Test
    void testConstructor() {
        Staff staff = new Staff("testuser", "hashedPassword", authManager);
        assertEquals("testuser", staff.getUsername());
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // REFACTORED: Setup mock to return a staff with matching password hash
        String correctPasswordHash = authManager.hashPassword("password123");
        
        com.orrs.domain.Staff mockStaff = new com.orrs.domain.Staff();
        mockStaff.setStaffId(1);
        mockStaff.setUsername("testuser");
        mockStaff.setPasswordHash(correctPasswordHash);  // Use computed hash
        mockStaff.setFullName("Test User");
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("testuser")).thenReturn(mockStaff);
        
        Staff staff = new Staff("testuser", "hashedPassword", authManager);
        assertTrue(staff.login("password123"));
        assertTrue(staff.isAuthenticated());
    }

    @Test
    void testLoginFailure() throws Exception {
        // REFACTORED: Setup mock to return a staff with non-matching password
        com.orrs.domain.Staff mockStaff = new com.orrs.domain.Staff();
        mockStaff.setStaffId(1);
        mockStaff.setUsername("testuser");
        mockStaff.setPasswordHash("wrongHash123");
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("testuser")).thenReturn(mockStaff);
        
        Staff staff = new Staff("testuser", "hashedPassword", authManager);
        assertFalse(staff.login("wrongPassword"));
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testLogout() throws Exception {
        // REFACTORED: Setup mock to allow successful login
        String correctPasswordHash = authManager.hashPassword("password123");
        
        com.orrs.domain.Staff mockStaff = new com.orrs.domain.Staff();
        mockStaff.setStaffId(1);
        mockStaff.setUsername("testuser");
        mockStaff.setPasswordHash(correctPasswordHash);  // Use computed hash
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("testuser")).thenReturn(mockStaff);
        
        Staff staff = new Staff("testuser", "hashedPassword", authManager);
        staff.login("password123");
        assertTrue(staff.isAuthenticated());
        staff.logout();
        assertFalse(staff.isAuthenticated());
    }

    @Test
    void testToString() {
        Staff staff = new Staff("testuser", "hashedPassword", authManager);
        String expected = "Staff{username='testuser', authenticated=false}";
        assertEquals(expected, staff.toString());
    }
}