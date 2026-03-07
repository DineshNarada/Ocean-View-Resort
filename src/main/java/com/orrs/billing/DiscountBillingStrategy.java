package com.orrs.billing;

import com.orrs.domain.Bill;
import com.orrs.domain.Reservation;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Discount billing strategy for promotional or group reservations.
 * Calculates bill as: (nights × rate per night) - discount + tax
 * 
 * Applied discount based on:
 * - 5% for stays of 7-13 nights
 * - 10% for stays of 14-30 nights
 * - 15% for stays of 31+ nights
 */
public class DiscountBillingStrategy implements BillingStrategy {
    
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
        
        // Calculate discount based on duration
        BigDecimal discount = calculateDiscount(subtotal, nights);
        
        // Calculate tax on subtotal (before discount)
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        
        // Create bill with calculated discount
        Bill bill = new Bill(reservation.getReservationId(), subtotal, tax, discount);
        
        return bill;
    }
    
    /**
     * Calculates discount percentage based on stay duration.
     * 
     * @param subtotal the subtotal amount
     * @param nights the number of nights
     * @return the discount amount
     */
    private BigDecimal calculateDiscount(BigDecimal subtotal, long nights) {
        BigDecimal discountPercent = BigDecimal.ZERO;
        
        if (nights >= 31) {
            discountPercent = new BigDecimal("0.15"); // 15% discount
        } else if (nights >= 14) {
            discountPercent = new BigDecimal("0.10"); // 10% discount
        } else if (nights >= 7) {
            discountPercent = new BigDecimal("0.05"); // 5% discount
        }
        
        return subtotal.multiply(discountPercent);
    }
    
    @Override
    public String getStrategyName() {
        return "Discount";
    }
}
