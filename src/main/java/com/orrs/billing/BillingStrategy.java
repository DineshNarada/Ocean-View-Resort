package com.orrs.billing;

import com.orrs.domain.Bill;
import com.orrs.domain.Reservation;

/**
 * Strategy interface for billing calculations.
 * Implements the Strategy design pattern for flexible billing logic.
 * 
 * Different billing strategies can be applied based on customer type,
 * room type, promotions, or other business rules.
 */
public interface BillingStrategy {
    /**
     * Calculates a bill based on the reservation and billing rules.
     * 
     * @param reservation the reservation to bill
     * @return a Bill object with calculated amounts
     */
    Bill calculateBill(Reservation reservation);
    
    /**
     * Gets the name/description of this billing strategy.
     * 
     * @return the strategy name
     */
    String getStrategyName();
}
