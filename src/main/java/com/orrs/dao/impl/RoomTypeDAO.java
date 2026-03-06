package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.IRoomTypeDAO;
import com.orrs.domain.RoomType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomType DAO Implementation
 * Handles all database operations for RoomType entity
 * Manages room type definitions with pricing, capacity, and descriptions
 */
public class RoomTypeDAO implements IRoomTypeDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(RoomType roomType) throws Exception {
        String sql = "INSERT INTO RoomType (typeName, pricePerNight, capacity, description) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getPricePerNight());
            stmt.setInt(3, roomType.getCapacity());
            stmt.setString(4, roomType.getDescription());
            
            stmt.executeUpdate();
            
            // Get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                roomType.setRoomTypeId(rs.getInt(1));
            }
        }
    }
    
    @Override
    public RoomType readById(int id) throws Exception {
        String sql = "SELECT * FROM RoomType WHERE roomTypeId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoomType(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<RoomType> readAll() throws Exception {
        List<RoomType> roomTypes = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY typeName ASC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                roomTypes.add(mapResultSetToRoomType(rs));
            }
        }
        return roomTypes;
    }
    
    @Override
    public void update(RoomType roomType) throws Exception {
        String sql = "UPDATE RoomType SET typeName = ?, pricePerNight = ?, capacity = ?, " +
                    "description = ? WHERE roomTypeId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getPricePerNight());
            stmt.setInt(3, roomType.getCapacity());
            stmt.setString(4, roomType.getDescription());
            stmt.setInt(5, roomType.getRoomTypeId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM RoomType WHERE roomTypeId = ?";
        
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
    public RoomType findByTypeName(String typeName) throws Exception {
        String sql = "SELECT * FROM RoomType WHERE typeName = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, typeName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoomType(rs);
            }
        }
        return null;
    }
    
    /**
     * Map ResultSet to RoomType object
     */
    private RoomType mapResultSetToRoomType(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(rs.getInt("roomTypeId"));
        roomType.setTypeName(rs.getString("typeName"));
        roomType.setPricePerNight(rs.getBigDecimal("pricePerNight"));
        roomType.setCapacity(rs.getInt("capacity"));
        roomType.setDescription(rs.getString("description"));
        roomType.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return roomType;
    }
}
