package com.orrs.dao;

import com.orrs.domain.Guest;
import java.util.List;

/**
 * Guest Data Access Object Interface
 */
public interface IGuestDAO extends IGenericDAO<Guest> {
    
    /**
     * Find guest by email
     */
    Guest findByEmail(String email) throws Exception;
    
    /**
     * Find guest by phone number
     */
    Guest findByPhone(String phone) throws Exception;
    
    /**
     * Search guests by name (partial match)
     */
    List<Guest> searchByName(String name) throws Exception;
}
