package com.orrs.billing;

import com.orrs.domain.Bill;
import com.orrs.domain.Reservation;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Premium billing strategy for high-tier reservations or premium rooms.
 * Calculates bill as: (nights × rate per night × 1.25 premium multiplier) + tax
 * 
 * This strategy adds a 25% premium charge for premium room types or services.
 */
public class PremiumBillingStrategy implements BillingStrategy {
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax
    private static final BigDecimal PREMIUM_MULTIPLIER = new BigDecimal("1.25"); // 25% premium
    
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
        
        // Calculate subtotal with premium multiplier
        double baseRate = reservation.getRoomType().getRatePerNight();
        BigDecimal ratePerNight = BigDecimal.valueOf(baseRate);
        BigDecimal premiumRate = ratePerNight.multiply(PREMIUM_MULTIPLIER);
        BigDecimal subtotal = premiumRate.multiply(BigDecimal.valueOf(nights));
        
        // Calculate tax
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        
        // Create bill with no discount
        Bill bill = new Bill(reservation.getReservationId(), subtotal, tax, BigDecimal.ZERO);
        
        return bill;
    }
    
    @Override
    public String getStrategyName() {
        return "Premium";
    }
}
