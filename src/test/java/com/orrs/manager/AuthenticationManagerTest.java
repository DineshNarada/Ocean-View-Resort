package com.orrs.manager;

import com.orrs.dao.IStaffDAO;
import com.orrs.domain.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test suite for AuthenticationManager
 * 
 * REFACTORED: Tests now verify database-driven authentication.
 * Previously tested hardcoded credentials; now tests DAO integration.
 * 
 * Uses Mockito to mock StaffDAO and test authentication logic in isolation
 * from actual database.
 */
class AuthenticationManagerTest {

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
    void testAuthenticateValidCredentials() throws Exception {
        // REFACTORED: Setup mock to return a staff with matching password hash
        // Compute correct hash using the same method as production code
        
        String correctPasswordHash = authManager.hashPassword("password123");
        
        Staff mockStaff = new Staff();
        mockStaff.setStaffId(1);
        mockStaff.setUsername("admin");
        mockStaff.setPasswordHash(correctPasswordHash);  // Use computed hash
        mockStaff.setFullName("Administrator");
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("admin")).thenReturn(mockStaff);
        
        // Should return Staff object (not null when authenticated)
        Staff result = authManager.authenticate("admin", "password123");
        assertNotNull(result, "Authentication should return Staff object for valid credentials");
        assertEquals("admin", result.getUsername());
    }

    @Test
    void testAuthenticateInvalidUsername() throws Exception {
        // REFACTORED: When DAO returns null (staff not found), should return null
        when(mockStaffDAO.findByUsername("nonexistent")).thenReturn(null);
        
        Staff result = authManager.authenticate("nonexistent", "password123");
        assertNull(result, "Authentication should return null for invalid username");
    }

    @Test
    void testAuthenticateInvalidPassword() throws Exception {
        // REFACTORED: When password hash doesn't match, should return null
        Staff mockStaff = new Staff();
        mockStaff.setStaffId(1);
        mockStaff.setUsername("admin");
        mockStaff.setPasswordHash("wrongHash123");
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("admin")).thenReturn(mockStaff);
        
        Staff result = authManager.authenticate("admin", "wrongPassword");
        assertNull(result, "Authentication should return null for invalid password");
    }

    @Test
    void testAuthenticateNullUsername() {
        // REFACTORED: Null checks still apply to prevent NPE
        Staff result = authManager.authenticate(null, "password123");
        assertNull(result, "Authentication should return null for null username");
    }

    @Test
    void testAuthenticateNullPassword() {
        // REFACTORED: Null checks still apply to prevent NPE
        Staff result = authManager.authenticate("admin", null);
        assertNull(result, "Authentication should return null for null password");
    }
    
    @Test
    void testAuthenticateInactiveStaff() throws Exception {
        // REFACTORED: Should reject authentication for inactive staff
        String correctPasswordHash = authManager.hashPassword("password123");
        
        Staff mockStaff = new Staff();
        mockStaff.setStaffId(2);
        mockStaff.setUsername("inactive");
        mockStaff.setPasswordHash(correctPasswordHash);  // Use computed hash
        mockStaff.setIsActive(false);  // Inactive staff
        
        when(mockStaffDAO.findByUsername("inactive")).thenReturn(mockStaff);
        
        Staff result = authManager.authenticate("inactive", "password123");
        assertNull(result, "Authentication should return null for inactive staff");
    }
    
    @Test
    void testAuthenticateReturnsCompleteStaffObject() throws Exception {
        // REFACTORED: Verify that authenticated staff has all database properties
        String correctPasswordHash = authManager.hashPassword("password123");
        
        Staff mockStaff = new Staff();
        mockStaff.setStaffId(5);
        mockStaff.setUsername("testuser");
        mockStaff.setPasswordHash(correctPasswordHash);  // Use computed hash
        mockStaff.setFullName("Test User");
        mockStaff.setEmail("test@example.com");
        mockStaff.setRole(Staff.StaffRole.STAFF);
        mockStaff.setIsActive(true);
        
        when(mockStaffDAO.findByUsername("testuser")).thenReturn(mockStaff);
        
        Staff result = authManager.authenticate("testuser", "password123");
        assertNotNull(result);
        assertEquals(5, result.getStaffId());
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Staff.StaffRole.STAFF, result.getRole());
    }
}