package com.orrs.validator;

import com.orrs.domain.Guest;
import com.orrs.domain.Reservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ValidationEngine coordinates validation using different strategies.
 * Implements the Strategy design pattern to delegate validation to appropriate validators.
 * 
 * This class acts as a factory and coordinator for validation strategies,
 * allowing flexible validation based on object type.
 */
public class ValidationEngine {
    
    private Map<Class<?>, ValidationStrategy> strategies;
    
    /**
     * Constructs the ValidationEngine with built-in validators.
     */
    public ValidationEngine() {
        this.strategies = new HashMap<>();
        registerDefaultStrategies();
    }
    
    /**
     * Registers default validation strategies for common domain objects.
     */
    private void registerDefaultStrategies() {
        strategies.put(Guest.class, new GuestValidator());
        strategies.put(Reservation.class, new ReservationValidator());
    }
    
    /**
     * Registers a custom validation strategy for a specific class.
     * 
     * @param clazz the class type to validate
     * @param strategy the validation strategy to use
     */
    public void registerStrategy(Class<?> clazz, ValidationStrategy strategy) {
        strategies.put(clazz, strategy);
    }
    
    /**
     * Validates an object using its registered strategy.
     * 
     * @param object the object to validate
     * @return a list of validation error messages; empty list if validation passes
     */
    public List<String> validate(Object object) {
        if (object == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Object to validate cannot be null");
            return errors;
        }
        
        ValidationStrategy strategy = strategies.get(object.getClass());
        if (strategy == null) {
            List<String> errors = new ArrayList<>();
            errors.add("No validation strategy registered for " + object.getClass().getSimpleName());
            return errors;
        }
        
        return strategy.validate(object);
    }
    
    /**
     * Validates an object and throws an exception if validation fails.
     * 
     * @param object the object to validate
     * @throws ValidationException if validation fails
     */
    public void validateOrThrow(Object object) throws ValidationException {
        List<String> errors = validate(object);
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: " + String.join(", ", errors));
        }
    }
    
    /**
     * Checks if an object is valid based on its registered strategy.
     * 
     * @param object the object to validate
     * @return true if validation passes, false otherwise
     */
    public boolean isValid(Object object) {
        return validate(object).isEmpty();
    }
}
