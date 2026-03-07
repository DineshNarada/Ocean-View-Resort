package com.orrs.validator;

import java.util.List;

/**
 * Strategy interface for validation of different domain objects.
 * Implements the Strategy design pattern for flexible validation logic.
 * 
 * This interface allows different validation strategies to be plugged in
 * without modifying the client code that uses them.
 */
public interface ValidationStrategy {
    /**
     * Validates the provided object and returns a list of validation errors.
     * 
     * @param object the object to validate
     * @return a list of error messages; empty list if validation passes
     */
    List<String> validate(Object object);
}
