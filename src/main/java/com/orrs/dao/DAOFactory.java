package com.orrs.dao;

import com.orrs.dao.impl.*;

/**
 * DAO Factory - Factory Pattern for creating DAO instances
 * Provides centralized access to all DAO implementations
 */
public class DAOFactory {
    
    // Private constructor to prevent instantiation
    private DAOFactory() {}
    
    /**
     * Get ReservationDAO instance
     */
    public static IReservationDAO getReservationDAO() {
        return new ReservationDAO();
    }
    
    /**
     * Get GuestDAO instance
     */
    public static IGuestDAO getGuestDAO() {
        return new GuestDAO();
    }
    
    /**
     * Get RoomDAO instance
     */
    public static IRoomDAO getRoomDAO() {
        return new RoomDAO();
    }
    
    /**
     * Get RoomTypeDAO instance
     */
    public static IRoomTypeDAO getRoomTypeDAO() {
        return new RoomTypeDAO();
    }
    
    /**
     * Get BillDAO instance
     */
    public static IBillDAO getBillDAO() {
        return new BillDAO();
    }
}
