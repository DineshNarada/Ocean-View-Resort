package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.IGuestDAO;
import com.orrs.domain.Guest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Guest DAO Implementation
 * Handles all database operations for Guest entity
 */
public class GuestDAO implements IGuestDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(Guest guest) throws Exception {
        String sql = "INSERT INTO Guest (name, email, phone, address, city, country) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getEmail());
            stmt.setString(3, guest.getPhone());
            stmt.setString(4, guest.getAddress());
            stmt.setString(5, guest.getCity());
            stmt.setString(6, guest.getCountry());
            
            stmt.executeUpdate();
            
            // Get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                guest.setGuestId(rs.getInt(1));
            }
        }
    }
    
    @Override
    public Guest readById(int id) throws Exception {
        String sql = "SELECT * FROM Guest WHERE guestId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Guest> readAll() throws Exception {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guest ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
        }
        return guests;
    }
    
    @Override
    public void update(Guest guest) throws Exception {
        String sql = "UPDATE Guest SET name = ?, email = ?, phone = ?, " +
                    "address = ?, city = ?, country = ? WHERE guestId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getEmail());
            stmt.setString(3, guest.getPhone());
            stmt.setString(4, guest.getAddress());
            stmt.setString(5, guest.getCity());
            stmt.setString(6, guest.getCountry());
            stmt.setInt(7, guest.getGuestId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Guest WHERE guestId = ?";
        
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
    public Guest findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM Guest WHERE email = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
        }
        return null;
    }
    
    @Override
    public Guest findByPhone(String phone) throws Exception {
        String sql = "SELECT * FROM Guest WHERE phone = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Guest> searchByName(String name) throws Exception {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guest WHERE name LIKE ? ORDER BY name ASC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
        }
        return guests;
    }
    
    /**
     * Map ResultSet to Guest object
     */
    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestId(rs.getInt("guestId"));
        guest.setName(rs.getString("name"));
        guest.setEmail(rs.getString("email"));
        guest.setPhone(rs.getString("phone"));
        guest.setAddress(rs.getString("address"));
        guest.setCity(rs.getString("city"));
        guest.setCountry(rs.getString("country"));
        guest.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return guest;
    }
}
