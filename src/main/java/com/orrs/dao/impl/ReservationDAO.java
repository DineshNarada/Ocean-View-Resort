package com.orrs.dao.impl;

import com.orrs.config.DatabaseConfig;
import com.orrs.dao.DAOFactory;
import com.orrs.dao.IReservationDAO;
import com.orrs.dao.IGuestDAO;
import com.orrs.domain.Reservation;
import com.orrs.domain.RoomType;
import com.orrs.domain.Guest;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation DAO Implementation
 * Handles all database operations for Reservation entity
 * Now properly manages individual rooms instead of just room types
 */
public class ReservationDAO implements IReservationDAO {
    
    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    
    @Override
    public void create(Reservation reservation) throws Exception {
        // Calculate numberOfNights and totalCost using the room's type price
        long numberOfNights = java.time.temporal.ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        
        // Get RoomType - use provided roomType if available, otherwise lookup by roomId
        RoomType roomType = reservation.getRoomType();
        if (roomType == null) {
            if (reservation.getRoomId() <= 0) {
                throw new Exception("Neither roomId nor roomType is set. Cannot save reservation.");
            }
            roomType = getRoomTypeForRoom(reservation.getRoomId());
        }
        
        BigDecimal totalCost = roomType.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        reservation.setTotalCost(totalCost);
        
        String sql = "INSERT INTO Reservation (reservationNumber, guestId, roomId, " +
                    "checkInDate, checkOutDate, totalCost, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservation.getReservationNumber());
            stmt.setInt(2, reservation.getGuestId());
            stmt.setInt(3, reservation.getRoomId());
            stmt.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setBigDecimal(6, reservation.getTotalCost());
            stmt.setString(7, reservation.getStatus().toString());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public Reservation readById(int id) throws Exception {
        String sql = "SELECT * FROM Reservation WHERE reservationId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Reservation> readAll() throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public void update(Reservation reservation) throws Exception {
        // Recalculate numberOfNights and totalCost using room's type price for live pricing
        long numberOfNights = java.time.temporal.ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        
        // Get RoomType - use provided roomType if available, otherwise lookup by roomId
        RoomType roomType = reservation.getRoomType();
        if (roomType == null) {
            if (reservation.getRoomId() <= 0) {
                throw new Exception("Neither roomId nor roomType is set. Cannot update reservation.");
            }
            roomType = getRoomTypeForRoom(reservation.getRoomId());
        }
        
        BigDecimal totalCost = roomType.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        reservation.setTotalCost(totalCost);
        
        String sql = "UPDATE Reservation SET guestId = ?, roomId = ?, " +
                    "checkInDate = ?, checkOutDate = ?, totalCost = ?, status = ? " +
                    "WHERE reservationId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setBigDecimal(5, reservation.getTotalCost());
            stmt.setString(6, reservation.getStatus().toString());
            stmt.setInt(7, reservation.getReservationId());
            
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Reservation WHERE reservationId = ?";
        
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
    public Reservation findByReservationNumber(String reservationNumber) throws Exception {
        String sql = "SELECT * FROM Reservation WHERE reservationNumber = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservationNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        }
        return null;
    }
    
    @Override
    public List<Reservation> findByGuestId(int guestId) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE guestId = ? ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public List<Reservation> findByStatus(String status) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE status = ? ORDER BY createdAt DESC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public List<Reservation> findOverlappingReservations(int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE roomId = ? " +
                    "AND status IN ('CONFIRMED', 'CHECKED_IN') " +
                    "AND checkInDate < ? AND checkOutDate > ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOut));
            stmt.setDate(3, Date.valueOf(checkIn));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public List<Reservation> findByRoomId(int roomId) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE roomId = ? ORDER BY checkInDate ASC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE checkInDate >= ? AND checkOutDate <= ? " +
                    "ORDER BY checkInDate ASC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }
    
    @Override
    public List<Integer> findAvailableRoomsByType(int roomTypeId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        List<Integer> availableRoomIds = new ArrayList<>();
        String sql = "SELECT r.roomId FROM Room r " +
                    "WHERE r.roomTypeId = ? AND r.status = 'AVAILABLE' " +
                    "AND r.roomId NOT IN (" +
                    "  SELECT res.roomId FROM Reservation res " +
                    "  WHERE res.status IN ('CONFIRMED', 'CHECKED_IN') " +
                    "  AND res.checkInDate < ? AND res.checkOutDate > ?" +
                    ")";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomTypeId);
            stmt.setDate(2, Date.valueOf(checkOut));
            stmt.setDate(3, Date.valueOf(checkIn));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                availableRoomIds.add(rs.getInt("roomId"));
            }
        }
        return availableRoomIds;
    }
    
    /**
     * Map ResultSet row to Reservation object
     * Also fetches associated Guest object to prevent lazy loading issues
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservationId"));
        reservation.setReservationNumber(rs.getString("reservationNumber"));
        int guestId = rs.getInt("guestId");
        reservation.setGuestId(guestId);
        int roomId = rs.getInt("roomId");
        reservation.setRoomId(roomId);
        reservation.setCheckInDate(rs.getDate("checkInDate").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("checkOutDate").toLocalDate());
        reservation.setTotalCost(rs.getBigDecimal("totalCost"));
        reservation.setStatus(reservation.mapStatus(rs.getString("status")));
        
        // Load Guest object to prevent NullPointerException when accessing guest details
        try {
            IGuestDAO guestDAO = DAOFactory.getInstance().getGuestDAO();
            Guest guest = guestDAO.readById(guestId);
            if (guest != null) {
                reservation.setGuest(guest);
            }
        } catch (Exception e) {
            // Log error but don't fail - reservation data is still valid
            System.err.println("Warning: Failed to load Guest for guestId " + guestId + ": " + e.getMessage());
        }
        
        // Load RoomType object based on roomId
        try {
            if (roomId > 0) {
                RoomType roomType = getRoomTypeForRoom(roomId);
                if (roomType != null) {
                    reservation.setRoomType(roomType);
                }
            }
        } catch (Exception e) {
            // Log error but don't fail - reservation data is still valid
            System.err.println("Warning: Failed to load RoomType for roomId " + roomId + ": " + e.getMessage());
        }
        
        return reservation;
    }
    
    /**
     * Helper method to get RoomType by Room ID
     * Fetches the room type associated with a specific room
     */
    private RoomType getRoomTypeForRoom(int roomId) throws Exception {
        String sql = "SELECT rt.roomTypeId, rt.typeName, rt.pricePerNight, rt.capacity, rt.description " +
                    "FROM RoomType rt " +
                    "JOIN Room r ON rt.roomTypeId = r.roomTypeId " +
                    "WHERE r.roomId = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getInt("roomTypeId"));
                roomType.setTypeName(rs.getString("typeName"));
                roomType.setPricePerNight(rs.getBigDecimal("pricePerNight"));
                roomType.setCapacity(rs.getInt("capacity"));
                roomType.setDescription(rs.getString("description"));
                return roomType;
            }
        }
        throw new Exception("RoomType not found for Room ID: " + roomId);
    }
}
