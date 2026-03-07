package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.IStaffDAO;
import com.orrs.domain.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Staff DAO Implementation
 * Handles all database operations for Staff entity
 */
public class StaffDAO implements IStaffDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(Staff staff) throws Exception {
        String sql = "INSERT INTO Staff (username, passwordHash, email, fullName, role, isActive) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPasswordHash());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getFullName());
            stmt.setString(5, staff.getRole().toString());
            stmt.setBoolean(6, staff.getIsActive() != null ? staff.getIsActive() : true);
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public Staff readById(int id) throws Exception {
        String sql = "SELECT * FROM Staff WHERE staffId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Staff> readAll() throws Exception {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        }
        return staffList;
    }
    
    @Override
    public void update(Staff staff) throws Exception {
        String sql = "UPDATE Staff SET username = ?, email = ?, fullName = ?, " +
                    "role = ?, isActive = ? WHERE staffId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getEmail());
            stmt.setString(3, staff.getFullName());
            stmt.setString(4, staff.getRole().toString());
            stmt.setBoolean(5, staff.getIsActive() != null ? staff.getIsActive() : true);
            stmt.setInt(6, staff.getStaffId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Staff WHERE staffId = ?";
        
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
    public Staff findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM Staff WHERE username = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        }
        return null;
    }
    
    @Override
    public Staff findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM Staff WHERE email = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Staff> findAllActive() throws Exception {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff WHERE isActive = true ORDER BY fullName ASC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        }
        return staffList;
    }
    
    @Override
    public List<Staff> findByRole(String role) throws Exception {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff WHERE role = ? ORDER BY fullName ASC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        }
        return staffList;
    }
    
    @Override
    public void deactivate(int staffId) throws Exception {
        String sql = "UPDATE Staff SET isActive = false WHERE staffId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Map ResultSet row to Staff object
     */
    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staffId"));
        staff.setUsername(rs.getString("username"));
        staff.setPasswordHash(rs.getString("passwordHash"));
        staff.setEmail(rs.getString("email"));
        staff.setFullName(rs.getString("fullName"));
        staff.setRole(Staff.StaffRole.valueOf(rs.getString("role")));
        staff.setIsActive(rs.getBoolean("isActive"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            staff.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return staff;
    }
}
