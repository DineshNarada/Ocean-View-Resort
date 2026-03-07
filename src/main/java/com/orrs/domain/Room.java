package com.orrs.domain;

import java.time.LocalDateTime;

/**
 * Room Domain Class
 * Represents an individual room entity in the system
 */
public class Room {
    
    private int roomId;
    private String roomNumber;
    private int roomTypeId;  // Foreign key to RoomType
    private int floor;
    private RoomStatus status;
    private LocalDateTime createdAt;
    
    /**
     * Default constructor
     */
    public Room() {
    }
    
    /**
     * Constructor with all fields
     */
    public Room(int roomId, String roomNumber, int roomTypeId, int floor, RoomStatus status) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.floor = floor;
        this.status = status;
    }
    
    // Getters and Setters
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public int getRoomTypeId() {
        return roomTypeId;
    }
    
    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public RoomStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    public void setStatus(String statusStr) {
        this.status = RoomStatus.valueOf(statusStr);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Helper method to map string to RoomStatus enum
     */
    public RoomStatus mapStatus(String statusStr) {
        return RoomStatus.valueOf(statusStr);
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomTypeId=" + roomTypeId +
                ", floor=" + floor +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
    
    /**
     * Enumeration for room status
     */
    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE
    }
}
