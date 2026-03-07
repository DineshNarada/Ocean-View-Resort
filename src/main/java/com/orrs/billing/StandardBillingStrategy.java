package com.orrs.billing;

import com.orrs.domain.Bill;
import com.orrs.domain.Reservation;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Standard billing strategy for regular reservations.
 * Calculates bill as: (nights × rate per night) + tax
 * 
 * This is the base billing calculation without any special discounts or premiums.
 */
public class StandardBillingStrategy implements BillingStrategy {
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax
    
    @Override
    public Bill calculateBill(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        
        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(
            reservation.getCheckIn(), 
            reservation.getCheckOut()
        );
        
        if (nights <= 0) {
            throw new IllegalArgumentException("Reservation must be at least 1 night");
        }
        
        // Calculate subtotal (nights × rate per night)
        double ratePerNight = reservation.getRoomType().getRatePerNight();
        BigDecimal subtotal = BigDecimal.valueOf((int) nights * ratePerNight);
        
        // Calculate tax
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        
        // Create bill with no discount
        Bill bill = new Bill(reservation.getReservationId(), subtotal, tax, BigDecimal.ZERO);
        
        return bill;
    }
    
    @Override
    public String getStrategyName() {
        return "Standard";
    }
}
