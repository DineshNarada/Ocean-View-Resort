package com.orrs.billing;

import com.orrs.domain.Reservation;
import com.orrs.domain.Bill;
import com.orrs.domain.RoomType;
import java.util.HashMap;
import java.util.Map;

/**
 * BillingEngine coordinates billing calculations using different strategies.
 * Implements the Strategy design pattern to support multiple billing approaches.
 * 
 * The engine selects appropriate billing strategies based on:
 * - Room type (premium, standard, budget)
 * - Guest type (regular, VIP, group)
 * - Duration of stay (length of stay discounts)
 * - Promotional codes or special offers
 */
public class BillingEngine {
    
    private BillingStrategy defaultStrategy;
    private Map<String, BillingStrategy> strategies;
    private Map<RoomType, String> roomTypeStrategies; // Map room types to strategy names
    
    /**
     * Constructs the BillingEngine with default strategies.
     */
    public BillingEngine() {
        this.strategies = new HashMap<>();
        this.roomTypeStrategies = new HashMap<>();
        this.defaultStrategy = new StandardBillingStrategy();
        
        // Initialize built-in strategies
        registerStrategy("Standard", new StandardBillingStrategy());
        registerStrategy("Premium", new PremiumBillingStrategy());
        registerStrategy("Discount", new DiscountBillingStrategy());
    }
    
    /**
     * Registers a billing strategy.
     * 
     * @param name the strategy identifier
     * @param strategy the billing strategy implementation
     */
    public void registerStrategy(String name, BillingStrategy strategy) {
        strategies.put(name, strategy);
    }
    
    /**
     * Sets the default strategy to use when no specific strategy is matched.
     * 
     * @param strategy the default billing strategy
     */
    public void setDefaultStrategy(BillingStrategy strategy) {
        if (strategy != null) {
            this.defaultStrategy = strategy;
        }
    }
    
    /**
     * Associates a room type with a specific billing strategy.
     * 
     * @param roomType the room type
     * @param strategyName the strategy name to use for this room type
     */
    public void setStrategyForRoomType(RoomType roomType, String strategyName) {
        if (roomType != null && strategyName != null && strategies.containsKey(strategyName)) {
            roomTypeStrategies.put(roomType, strategyName);
        }
    }
    
    /**
     * Calculates a bill for a reservation using the appropriate strategy.
     * 
     * The strategy is selected in this order:
     * 1. Room type-specific strategy
     * 2. Default strategy
     * 
     * @param reservation the reservation to bill
     * @return a calculated Bill object
     */
    public Bill calculateBill(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        
        BillingStrategy strategy = selectStrategy(reservation);
        return strategy.calculateBill(reservation);
    }
    
    /**
     * Calculates a bill using a specific named strategy.
     * 
     * @param reservation the reservation to bill
     * @param strategyName the name of the strategy to use
     * @return a calculated Bill object
     * @throws IllegalArgumentException if strategy is not found
     */
    public Bill calculateBill(Reservation reservation, String strategyName) {
        if (!strategies.containsKey(strategyName)) {
            throw new IllegalArgumentException("Unknown billing strategy: " + strategyName);
        }
        
        BillingStrategy strategy = strategies.get(strategyName);
        return strategy.calculateBill(reservation);
    }
    
    /**
     * Selects the appropriate billing strategy for a reservation.
     * 
     * @param reservation the reservation
     * @return the selected strategy
     */
    private BillingStrategy selectStrategy(Reservation reservation) {
        // Check if there's a strategy mapped for this room type
        if (reservation.getRoomType() != null) {
            String strategyName = roomTypeStrategies.get(reservation.getRoomType());
            if (strategyName != null && strategies.containsKey(strategyName)) {
                return strategies.get(strategyName);
            }
        }
        
        // Return default strategy
        return defaultStrategy;
    }
    
    /**
     * Gets a list of all available strategy names.
     * 
     * @return an array of available strategy names
     */
    public String[] getAvailableStrategies() {
        return strategies.keySet().toArray(new String[0]);
    }
}
