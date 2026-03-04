package com.orrs.domain;

/**
 * Represents a room type available at the Ocean View Resort.
 * Encapsulates room type name and nightly rate.
 */
public class RoomType {
    private String typeName;
    private double ratePerNight;

    /**
     * Constructs a RoomType with the provided details.
     *
     * @param typeName the name of the room type (e.g., "Single", "Double", "Suite")
     * @param ratePerNight the cost per night for this room type
     */
    public RoomType(String typeName, double ratePerNight) {
        this.typeName = typeName;
        this.ratePerNight = ratePerNight;
    }

    // Getters
    public String getTypeName() {
        return typeName;
    }

    public double getRatePerNight() {
        return ratePerNight;
    }

    // Setters
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setRatePerNight(double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "typeName='" + typeName + '\'' +
                ", ratePerNight=" + ratePerNight +
                '}';
    }
}
