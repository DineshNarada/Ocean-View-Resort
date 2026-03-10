package com.orrs.manager;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IStaffDAO;
import com.orrs.domain.Staff;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Handles user authentication for the Ocean View Resort system.
 * Verifies username and password credentials against the database.
 * Provides demo login fallback for testing and demonstrations.
 * 
 * DATA LAYER: Integrates with StaffDAO to retrieve staff from database
 * SECURITY: Passwords are stored as hashes in the database
 * 
 * Design Pattern: Uses dependency injection (DAO) for database access
 * Separation of Concerns: Authentication logic separated from HTTP handling
 */
public class AuthenticationManager {
    private final IStaffDAO staffDAO;
    
    // DEMO CREDENTIALS: Hardcoded for testing and demonstrations
    // Priority: Database credentials first, demo only if database lookup fails
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "password123";
    
    /**
     * Constructs AuthenticationManager with injected StaffDAO
     * This promotes loose coupling and testability
     * 
     * @param staffDAO the data access object for staff operations
     */
    public AuthenticationManager(IStaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }
    
    /**
     * Constructs AuthenticationManager with default StaffDAO from factory
     * Convenient constructor for production code
     */
    public AuthenticationManager() {
        this.staffDAO = DAOFactory.getInstance().getStaffDAO();
    }

    /**
     * Authenticates a user based on provided credentials.
     * Priority order:
     * 1. Check database credentials (staff from database with password hash)
     * 2. Fallback to demo credentials (hardcoded for testing/demonstrations)
     *
     * @param username the username to authenticate
     * @param password the plain-text password to verify
     * @return the authenticated Staff object if successful, null otherwise
     * 
     * DATABASE INTERACTION:
     * 1. Query database via StaffDAO.findByUsername()
     * 2. Compare password hash using SHA-256
     * 3. If no match, check demo credentials as fallback
     * 4. Return Staff object with database ID and all properties (or demo staff)
     */
    public Staff authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        try {
            // DYNAMIC DATA RETRIEVAL: Query database for staff by username
            Staff staff = staffDAO.findByUsername(username);
            
            // If staff found and active, verify password
            if (staff != null && (staff.getIsActive() == null || staff.getIsActive())) {
                // SECURITY: Verify password hash (supports SHA-256 for now)
                // In production, use bcrypt: BCryptPasswordEncoder.matches(password, staff.getPasswordHash())
                String passwordHash = hashPassword(password);
                if (staff.getPasswordHash() != null && staff.getPasswordHash().equals(passwordHash)) {
                    return staff;  // Return authenticated database staff
                }
            }
            
            // FALLBACK TO DEMO LOGIN: If database authentication fails, try demo credentials
            // This is useful for testing and demonstrations
            if (DEMO_USERNAME.equals(username) && DEMO_PASSWORD.equals(password)) {
                return createDemoStaff();  // Return demo staff object
            }
            
            return null;  // No valid credentials found
            
        } catch (Exception e) {
            // Log error in production
            System.err.println("Authentication error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a demo Staff object for testing/demonstration purposes.
     * This staff object has demo credentials but full staff properties.
     * 
     * @return a demo Staff object with ADMIN role
     */
    private Staff createDemoStaff() {
        Staff demoStaff = new Staff();
        demoStaff.setStaffId(-1);  // Use -1 as placeholder ID for demo staff
        demoStaff.setUsername(DEMO_USERNAME);
        demoStaff.setPasswordHash(hashPassword(DEMO_PASSWORD));
        demoStaff.setFullName("Demo Administrator");
        demoStaff.setEmail("demo@oceanview.com");
        demoStaff.setRole(Staff.StaffRole.ADMIN);
        demoStaff.setIsActive(true);
        return demoStaff;
    }
    
    /**
     * Hash a plain-text password using SHA-256.
     * 
     * NOTE: This is a simple implementation for demonstration.
     * In production, use bcrypt, Argon2, or PBKDF2 for better security.
     * 
     * Public visibility allows tests to compute correct password hashes
     * and enables external code to hash passwords when needed.
     * 
     * @param password plain-text password to hash
     * @return Base64-encoded SHA-256 hash
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
