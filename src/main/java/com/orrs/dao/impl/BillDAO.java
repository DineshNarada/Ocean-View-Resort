package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.IBillDAO;
import com.orrs.domain.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill DAO Implementation
 * Handles all database operations for Bill entity
 */
public class BillDAO implements IBillDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(Bill bill) throws Exception {
        String sql = "INSERT INTO Bill (reservationId, subtotal, tax, discount, paymentStatus) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, bill.getReservationIdAsInt());
            stmt.setBigDecimal(2, bill.getSubtotal());
            stmt.setBigDecimal(3, bill.getTax());
            stmt.setBigDecimal(4, bill.getDiscount());
            stmt.setString(5, bill.getPaymentStatus().toString());
            
            stmt.executeUpdate();
            
            // Get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                bill.setBillId(rs.getInt(1));
            }
        }
    }
    
    @Override
    public Bill readById(int id) throws Exception {
        String sql = "SELECT * FROM Bill WHERE billId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBill(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Bill> readAll() throws Exception {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM Bill ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        }
        return bills;
    }
    
    @Override
    public void update(Bill bill) throws Exception {
        String sql = "UPDATE Bill SET reservationId = ?, subtotal = ?, tax = ?, " +
                    "discount = ?, paymentStatus = ?, paymentDate = ? WHERE billId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bill.getReservationIdAsInt());
            stmt.setBigDecimal(2, bill.getSubtotal());
            stmt.setBigDecimal(3, bill.getTax());
            stmt.setBigDecimal(4, bill.getDiscount());
            stmt.setString(5, bill.getPaymentStatus().toString());
            if (bill.getPaymentDate() != null) {
                stmt.setTimestamp(6, java.sql.Timestamp.valueOf(bill.getPaymentDate()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            stmt.setInt(7, bill.getBillId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Bill WHERE billId = ?";
        
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
    public Bill findByReservationId(int reservationId) throws Exception {
        String sql = "SELECT * FROM Bill WHERE reservationId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBill(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Bill> findUnpaidBills() throws Exception {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM Bill WHERE paymentStatus IN ('PENDING', 'PARTIAL') " +
                    "ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        }
        return bills;
    }
    
    /**
     * Map ResultSet row to Bill object
     */
    private Bill mapResultSetToBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("billId"));
        bill.setReservationId(rs.getInt("reservationId"));
        bill.setSubtotal(rs.getBigDecimal("subtotal"));
        bill.setTax(rs.getBigDecimal("tax"));
        bill.setDiscount(rs.getBigDecimal("discount"));
        bill.setTotalAmount(rs.getBigDecimal("totalAmount"));
        bill.setPaymentStatus(rs.getString("paymentStatus"));
        java.sql.Timestamp paymentDate = rs.getTimestamp("paymentDate");
        if (paymentDate != null) {
            bill.setPaymentDate(paymentDate.toLocalDateTime());
        }
        return bill;
    }
}
