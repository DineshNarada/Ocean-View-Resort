package com.orrs.dao;

import com.orrs.domain.Staff;
import java.util.List;

/**
 * Staff Data Access Object Interface
 * Handles database operations for Staff entity (authentication and management)
 */
public interface IStaffDAO extends IGenericDAO<Staff> {
    
    /**
     * Find staff by username
     */
    Staff findByUsername(String username) throws Exception;
    
    /**
     * Find staff by email
     */
    Staff findByEmail(String email) throws Exception;
    
    /**
     * Get all active staff members
     */
    List<Staff> findAllActive() throws Exception;
    
    /**
     * Get all staff members with a specific role
     */
    List<Staff> findByRole(String role) throws Exception;
    
    /**
     * Deactivate a staff member account
     */
    void deactivate(int staffId) throws Exception;
}
