package com.orrs.dao;

import com.orrs.domain.Reservation;
import java.time.LocalDate;
import java.util.List;

/**
 * Reservation Data Access Object Interface
 * Extends generic DAO with reservation-specific queries
 */
public interface IReservationDAO extends IGenericDAO<Reservation> {
    
    /**
     * Find reservation by reservation number
     */
    Reservation findByReservationNumber(String reservationNumber) throws Exception;
    
    /**
     * Get all reservations for a guest
     */
    List<Reservation> findByGuestId(int guestId) throws Exception;
    
    /**
     * Get reservations by status
     */
    List<Reservation> findByStatus(String status) throws Exception;
    
    /**
     * Find overlapping/conflicting reservations for a specific room on given dates
     */
    List<Reservation> findOverlappingReservations(int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception;
    
    /**
     * Get all reservations for a specific room
     */
    List<Reservation> findByRoomId(int roomId) throws Exception;
    
    /**
     * Get reservations between date range
     */
    List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;
    
    /**
     * Find available rooms of a specific type for given date range
     */
    List<Integer> findAvailableRoomsByType(int roomTypeId, LocalDate checkIn, LocalDate checkOut) throws Exception;
}
