package com.orrs.validator;

import com.orrs.domain.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for Reservation objects.
 * Validates reservation details including check-in/check-out dates and room availability.
 * 
 * Validation Rules:
 * - Check-in date cannot be in the past
 * - Check-in date must be before check-out date
 * - Stay duration must not exceed 365 days
 * - Guest information must be present
 * - Room type must be present
 */
public class ReservationValidator implements ValidationStrategy {

    @Override
    public List<String> validate(Object object) {
        List<String> errors = new ArrayList<>();
        
        if (!(object instanceof Reservation)) {
            errors.add("Invalid object type for ReservationValidator");
            return errors;
        }
        
        Reservation reservation = (Reservation) object;
        
        // Validate check-in date
        LocalDate checkInDate = reservation.getCheckIn();
        if (checkInDate == null) {
            errors.add("Check-in date is required");
        } else if (checkInDate.isBefore(LocalDate.now())) {
            errors.add("Check-in date cannot be in the past");
        }
        
        // Validate check-out date
        LocalDate checkOutDate = reservation.getCheckOut();
        if (checkOutDate == null) {
            errors.add("Check-out date is required");
        } else if (checkInDate != null && checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            errors.add("Check-out date must be after check-in date");
        }
        
        // Validate duration
        if (checkInDate != null && checkOutDate != null) {
            long duration = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (duration > 365) {
                errors.add("Maximum reservation duration is 365 days");
            }
            if (duration < 1) {
                errors.add("Reservation must be at least 1 night");
            }
        }
        
        // Validate guest
        if (reservation.getGuest() == null) {
            errors.add("Guest information is required");
        }
        
        // Validate room type
        if (reservation.getRoomType() == null) {
            errors.add("Room type is required");
        }
        
        return errors;
    }
}
