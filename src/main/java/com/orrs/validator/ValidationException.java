package com.orrs.validator;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends Exception {
    
    /**
     * Constructs a ValidationException with the specified message.
     * 
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a ValidationException with the specified message and cause.
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
