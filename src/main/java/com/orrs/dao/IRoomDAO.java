package com.orrs.dao;

import com.orrs.domain.Room;
import java.util.List;

/**
 * Room Data Access Object Interface
 * Manages individual room records
 */
public interface IRoomDAO extends IGenericDAO<Room> {
    
    /**
     * Find room by room number
     */
    Room findByRoomNumber(String roomNumber) throws Exception;
    
    /**
     * Get all rooms of a specific type
     */
    List<Room> findByRoomTypeId(int roomTypeId) throws Exception;
    
    /**
     * Get all rooms on a specific floor
     */
    List<Room> findByFloor(int floor) throws Exception;
    
    /**
     * Get all available rooms
     */
    List<Room> findAvailableRooms() throws Exception;
    
    /**
     * Update room status
     */
    void updateRoomStatus(int roomId, String status) throws Exception;
}
