package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.IRoomDAO;
import com.orrs.domain.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Room DAO Implementation
 * Handles all database operations for Room entity
 * Manages individual room records with their types, floors, and status
 */
public class RoomDAO implements IRoomDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(Room room) throws Exception {
        String sql = "INSERT INTO Room (roomNumber, roomTypeId, floor, status) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeId());
            stmt.setInt(3, room.getFloor());
            stmt.setString(4, room.getStatus().toString());
            
            stmt.executeUpdate();
            
            // Get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                room.setRoomId(rs.getInt(1));
            }
        }
    }
    
    @Override
    public Room readById(int id) throws Exception {
        String sql = "SELECT * FROM Room WHERE roomId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Room> readAll() throws Exception {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room ORDER BY roomNumber";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }
    
    @Override
    public void update(Room room) throws Exception {
        String sql = "UPDATE Room SET roomNumber = ?, roomTypeId = ?, floor = ?, status = ? " +
                    "WHERE roomId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeId());
            stmt.setInt(3, room.getFloor());
            stmt.setString(4, room.getStatus().toString());
            stmt.setInt(5, room.getRoomId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Room WHERE roomId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public boolean exists(int id) throws Exception {
        return readById(id) != null;
    }
    
    @Override
    public Room findByRoomNumber(String roomNumber) throws Exception {
        String sql = "SELECT * FROM Room WHERE roomNumber = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Room> findByRoomTypeId(int roomTypeId) throws Exception {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE roomTypeId = ? ORDER BY roomNumber";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomTypeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }
    
    @Override
    public List<Room> findByFloor(int floor) throws Exception {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE floor = ? ORDER BY roomNumber";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, floor);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }
    
    @Override
    public List<Room> findAvailableRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE status = 'AVAILABLE' ORDER BY roomNumber";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }
    
    @Override
    public void updateRoomStatus(int roomId, String status) throws Exception {
        String sql = "UPDATE Room SET status = ? WHERE roomId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Map ResultSet to Room object
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("roomId"));
        room.setRoomNumber(rs.getString("roomNumber"));
        room.setRoomTypeId(rs.getInt("roomTypeId"));
        room.setFloor(rs.getInt("floor"));
        room.setStatus(rs.getString("status"));
        room.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return room;
    }
}
