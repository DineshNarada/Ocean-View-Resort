package com.orrs.dao;

import java.util.List;

/**
 * Generic Data Access Object Interface
 * Defines standard CRUD operations for all entities
 * @param <T> Entity type
 */
public interface IGenericDAO<T> {
    
    /**
     * Create (Insert) a new entity
     */
    void create(T entity) throws Exception;
    
    /**
     * Read (Select) entity by ID
     */
    T readById(int id) throws Exception;
    
    /**
     * Read (Select) all entities
     */
    List<T> readAll() throws Exception;
    
    /**
     * Update an existing entity
     */
    void update(T entity) throws Exception;
    
    /**
     * Delete an entity by ID
     */
    void delete(int id) throws Exception;
    
    /**
     * Check if entity exists by ID
     */
    boolean exists(int id) throws Exception;
}
