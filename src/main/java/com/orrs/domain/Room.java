package com.orrs.domain;

/**
 * Room Domain Class
 * Represents a room entity in the system
 */
public class Room {
    
    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private int floor;
    private String status;
    
    /**
     * Default constructor
     */
    public Room() {
    }
    
    /**
     * Constructor with all fields
     */
    public Room(int roomId, String roomNumber, int roomTypeId, int floor, String status) {
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomTypeId=" + roomTypeId +
                ", floor=" + floor +
                ", status='" + status + '\'' +
                '}';
    }
}
