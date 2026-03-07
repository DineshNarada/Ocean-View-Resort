package com.orrs.dao;

/**
 * DAO Factory Pattern Implementation
 * Provides centralized creation of DAO instances
 */
public class DAOFactory {
    
    private static volatile DAOFactory instance;
    
    // DAO instances (lazy initialization)
    private IReservationDAO reservationDAO;
    private IGuestDAO guestDAO;
    private IRoomDAO roomDAO;
    private IRoomTypeDAO roomTypeDAO;
    private IBillDAO billDAO;
    private IStaffDAO staffDAO;
    
    /**
     * Private constructor to prevent instantiation
     */
    private DAOFactory() {}
    
    /**
     * Get singleton instance of DAOFactory
     */
    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get ReservationDAO instance
     */
    public IReservationDAO getReservationDAO() {
        if (reservationDAO == null) {
            synchronized (this) {
                if (reservationDAO == null) {
                    reservationDAO = new com.orrs.dao.impl.ReservationDAO();
                }
            }
        }
        return reservationDAO;
    }
    
    /**
     * Get GuestDAO instance
     */
    public IGuestDAO getGuestDAO() {
        if (guestDAO == null) {
            synchronized (this) {
                if (guestDAO == null) {
                    guestDAO = new com.orrs.dao.impl.GuestDAO();
                }
            }
        }
        return guestDAO;
    }
    
    /**
     * Get RoomDAO instance
     */
    public IRoomDAO getRoomDAO() {
        if (roomDAO == null) {
            synchronized (this) {
                if (roomDAO == null) {
                    roomDAO = new com.orrs.dao.impl.RoomDAO();
                }
            }
        }
        return roomDAO;
    }
    
    /**
     * Get RoomTypeDAO instance
     */
    public IRoomTypeDAO getRoomTypeDAO() {
        if (roomTypeDAO == null) {
            synchronized (this) {
                if (roomTypeDAO == null) {
                    roomTypeDAO = new com.orrs.dao.impl.RoomTypeDAO();
                }
            }
        }
        return roomTypeDAO;
    }
    
    /**
     * Get BillDAO instance
     */
    public IBillDAO getBillDAO() {
        if (billDAO == null) {
            synchronized (this) {
                if (billDAO == null) {
                    billDAO = new com.orrs.dao.impl.BillDAO();
                }
            }
        }
        return billDAO;
    }
    
    /**
     * Get StaffDAO instance
     */
    public IStaffDAO getStaffDAO() {
        if (staffDAO == null) {
            synchronized (this) {
                if (staffDAO == null) {
                    staffDAO = new com.orrs.dao.impl.StaffDAO();
                }
            }
        }
        return staffDAO;
    }
}