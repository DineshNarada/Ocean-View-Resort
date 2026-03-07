package com.orrs.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a room type available at the Ocean View Resort.
 * Encapsulates room type details including pricing and capacity.
 */
public class RoomType {
    private int roomTypeId;
    private String typeName;
    private BigDecimal pricePerNight;
    private int capacity;
    private String description;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public RoomType() {
    }

    /**
     * Constructs a RoomType with the provided details.
     *
     * @param typeName the name of the room type (e.g., "Single", "Double", "Suite")
     * @param pricePerNight the cost per night for this room type
     * @param capacity the number of guests this room type can accommodate
     */
    public RoomType(String typeName, BigDecimal pricePerNight, int capacity) {
        this.typeName = typeName;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
    }

    /**
     * Constructs a RoomType with just type name and price per night (simplified).
     *
     * @param typeName the name of the room type (e.g., "Single", "Double", "Suite")
     * @param ratePerNight the cost per night for this room type (as double)
     */
    public RoomType(String typeName, double ratePerNight) {
        this.typeName = typeName;
        this.pricePerNight = BigDecimal.valueOf(ratePerNight);
        this.capacity = 1;  // Default capacity
    }

    // Getters
    public int getRoomTypeId() {
        return roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public double getRatePerNight() {
        return pricePerNight != null ? pricePerNight.doubleValue() : 0.0;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setRatePerNight(double ratePerNight) {
        this.pricePerNight = BigDecimal.valueOf(ratePerNight);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RoomType{typeName='" + typeName + "', ratePerNight=" + getRatePerNight() + "}";
    }
}
