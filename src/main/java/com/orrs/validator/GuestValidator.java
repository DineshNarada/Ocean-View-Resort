package com.orrs.validator;

import com.orrs.domain.Guest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator for Guest objects.
 * Validates guest information including name, email, phone, and address.
 * 
 * Validation Rules:
 * - Name: 3-100 characters, letters and spaces only
 * - Email: valid email format
 * - Phone: 10-15 digits with optional + or -
 * - Address: 10-200 characters, alphanumeric and punctuation
 */
public class GuestValidator implements ValidationStrategy {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[0-9\\-()]{10,15}$");
    
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s]{3,100}$");
    
    private static final Pattern ADDRESS_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9\\s.,#-]{10,200}$");

    @Override
    public List<String> validate(Object object) {
        List<String> errors = new ArrayList<>();
        
        if (!(object instanceof Guest)) {
            errors.add("Invalid object type for GuestValidator");
            return errors;
        }
        
        Guest guest = (Guest) object;
        
        // Validate name
        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            errors.add("Guest name is required");
        } else if (!NAME_PATTERN.matcher(guest.getName()).matches()) {
            errors.add("Guest name must be 3-100 characters with letters and spaces only");
        }
        
        // Validate email
        if (guest.getEmail() != null && !guest.getEmail().trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(guest.getEmail()).matches()) {
                errors.add("Invalid email format");
            }
        }
        
        // Validate phone
        if (guest.getPhone() == null || guest.getPhone().trim().isEmpty()) {
            errors.add("Phone number is required");
        } else if (!PHONE_PATTERN.matcher(guest.getPhone()).matches()) {
            errors.add("Phone number must be 10-15 digits with optional + or -");
        }
        
        // Validate address
        if (guest.getAddress() != null && !guest.getAddress().trim().isEmpty()) {
            if (!ADDRESS_PATTERN.matcher(guest.getAddress()).matches()) {
                errors.add("Address must be 10-200 characters with alphanumeric and basic punctuation");
            }
        }
        
        return errors;
    }
}
