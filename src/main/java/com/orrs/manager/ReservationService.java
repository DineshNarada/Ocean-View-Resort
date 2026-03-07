package com.orrs.manager;

import com.orrs.billing.BillingEngine;
import com.orrs.dao.DAOFactory;
import com.orrs.dao.IReservationDAO;
import com.orrs.dao.IGuestDAO;
import com.orrs.dao.IBillDAO;
import com.orrs.dao.IRoomTypeDAO;
import com.orrs.domain.Bill;
import com.orrs.domain.Guest;
import com.orrs.domain.Reservation;
import com.orrs.domain.RoomType;
import com.orrs.notification.EmailNotifier;
import com.orrs.notification.NotificationService;
import com.orrs.notification.SMSNotifier;
import com.orrs.validator.ValidationEngine;
import com.orrs.validator.ValidationException;

import java.time.LocalDate;
import java.util.List;

/**
 * ReservationService implements the Facade design pattern.
 * Provides a simplified, unified interface for managing reservations.
 * 
 * This service coordinates between:
 * - Data Access Objects (DAO layer)
 * - Business Logic (BillingEngine, ValidationEngine)
 * - Notifications (NotificationService)
 * 
 * It abstracts the complexity of managing reservations from the client code,
 * reducing coupling and improving maintainability.
 */
public class ReservationService {
    
    private IReservationDAO reservationDAO;
    private IGuestDAO guestDAO;
    private IBillDAO billDAO;
    private IRoomTypeDAO roomTypeDAO;
    private BillingEngine billingEngine;
    private ValidationEngine validationEngine;
    private NotificationService notificationService;
    
    /**
     * Constructs the ReservationService with all required dependencies.
     * Initializes notification service with default notifiers (Email and SMS).
     */
    public ReservationService() {
        DAOFactory factory = DAOFactory.getInstance();
        this.reservationDAO = factory.getReservationDAO();
        this.guestDAO = factory.getGuestDAO();
        this.billDAO = factory.getBillDAO();
        this.roomTypeDAO = factory.getRoomTypeDAO();
        this.billingEngine = new BillingEngine();
        this.validationEngine = new ValidationEngine();
        
        // Initialize notification service with default notifiers
        this.notificationService = new NotificationService();
        notificationService.registerObserver(new EmailNotifier());
        notificationService.registerObserver(new SMSNotifier());
    }
    
    /**
     * Creates a new reservation with full validation and notification.
     * 
     * @param guest the guest information
     * @param roomTypeId the room type to reserve
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the created Reservation
     * @throws ValidationException if validation fails
     * @throws Exception if database operation fails
     */
    public Reservation createReservation(Guest guest, int roomTypeId, 
                                        LocalDate checkInDate, LocalDate checkOutDate)
            throws ValidationException, Exception {
        
        // Validate guest
        validationEngine.validateOrThrow(guest);
        
        // Save guest if new
        if (!guestDAO.exists(guest.getGuestId())) {
            guestDAO.create(guest);
        } else {
            guest = guestDAO.readById(guest.getGuestId());
        }
        
        // Fetch and validate room type
        RoomType roomType = roomTypeDAO.readById(roomTypeId);
        if (roomType == null) {
            throw new Exception("Room type not found: " + roomTypeId);
        }
        
        // Create reservation with proper initialization
        Reservation reservation = new Reservation();
        reservation.setGuestId(guest.getGuestId());
        reservation.setGuest(guest);
        reservation.setRoomType(roomType);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setCheckIn(checkInDate);
        reservation.setCheckOut(checkOutDate);
        
        // Validate reservation
        validationEngine.validateOrThrow(reservation);
        
        // Save reservation
        reservationDAO.create(reservation);
        
        // Notify observers
        notificationService.notifyReservationCreated(reservation);
        
        return reservation;
    }
    
    /**
     * Retrieves a reservation by ID.
     * 
     * @param reservationId the reservation ID
     * @return the Reservation if found
     * @throws Exception if database operation fails
     */
    public Reservation getReservation(int reservationId) throws Exception {
        return reservationDAO.readById(reservationId);
    }
    
    /**
     * Retrieves all reservations for a guest.
     * 
     * @param guestId the guest ID
     * @return a list of reservations
     * @throws Exception if database operation fails
     */
    public List<Reservation> getGuestReservations(int guestId) throws Exception {
        return reservationDAO.findByGuestId(guestId);
    }
    
    /**
     * Confirms a reservation and sends confirmation notification.
     * 
     * @param reservationId the reservation ID to confirm
     * @throws Exception if database operation fails or reservation not found
     */
    public void confirmReservation(int reservationId) throws Exception {
        Reservation reservation = reservationDAO.readById(reservationId);
        if (reservation != null) {
            // Update status to CONFIRMED
            reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
            reservationDAO.update(reservation);
            
            // Notify observers
            notificationService.notifyReservationConfirmed(reservation);
        }
    }
    
    /**
     * Cancels a reservation and sends cancellation notification.
     * 
     * @param reservationId the reservation ID to cancel
     * @param reason the cancellation reason
     * @throws Exception if database operation fails or reservation not found
     */
    public void cancelReservation(int reservationId, String reason) throws Exception {
        Reservation reservation = reservationDAO.readById(reservationId);
        if (reservation != null) {
            // Update status to CANCELLED
            reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
            reservationDAO.update(reservation);
            
            // Notify observers
            notificationService.notifyReservationCancelled(reservation, reason);
        }
    }
    
    /**
     * Generates a bill for a reservation.
     * Uses the configured billing strategy.
     * 
     * @param reservationId the reservation ID
     * @return the calculated Bill
     * @throws Exception if reservation not found or database operation fails
     */
    public Bill generateBill(int reservationId) throws Exception {
        Reservation reservation = reservationDAO.readById(reservationId);
        if (reservation == null) {
            throw new Exception("Reservation not found");
        }
        
        // Calculate bill using billing engine
        Bill bill = billingEngine.calculateBill(reservation);
        
        // Save bill to database
        billDAO.create(bill);
        
        return bill;
    }
    
    /**
     * Generates a bill using a specific billing strategy.
     * 
     * @param reservationId the reservation ID
     * @param strategyName the billing strategy name (Standard, Premium, Discount)
     * @return the calculated Bill
     * @throws Exception if reservation not found or invalid strategy
     */
    public Bill generateBill(int reservationId, String strategyName) throws Exception {
        Reservation reservation = reservationDAO.readById(reservationId);
        if (reservation == null) {
            throw new Exception("Reservation not found");
        }
        
        // Calculate bill using specific strategy
        Bill bill = billingEngine.calculateBill(reservation, strategyName);
        
        // Save bill to database
        billDAO.create(bill);
        
        return bill;
    }
    
    /**
     * Retrieves a bill for a reservation.
     * 
     * @param billId the bill ID
     * @return the Bill if found
     * @throws Exception if database operation fails
     */
    public Bill getBill(int billId) throws Exception {
        return billDAO.readById(billId);
    }
    
    /**
     * Sends a check-in reminder to the guest.
     * 
     * @param reservationId the reservation ID
     * @throws Exception if reservation not found
     */
    public void sendCheckInReminder(int reservationId) throws Exception {
        Reservation reservation = reservationDAO.readById(reservationId);
        if (reservation != null) {
            notificationService.notifyCheckInReminder(reservation);
        }
    }
    
    /**
     * Gets all reservations in a date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of reservations in the date range
     * @throws Exception if database operation fails
     */
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) 
            throws Exception {
        return reservationDAO.findByDateRange(startDate, endDate);
    }
    
    /**
     * Checks room availability for a specific type and date range.
     * 
     * @param roomTypeId the room type ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return a list of available room IDs
     * @throws Exception if database operation fails
     */
    public List<Integer> checkRoomAvailability(int roomTypeId, LocalDate checkInDate, 
                                               LocalDate checkOutDate) throws Exception {
        return reservationDAO.findAvailableRoomsByType(roomTypeId, checkInDate, checkOutDate);
    }
    
    /**
     * Registers a custom notification observer.
     * 
     * @param observer the observer to register
     */
    public void registerNotificationObserver(com.orrs.notification.ReservationObserver observer) {
        notificationService.registerObserver(observer);
    }
    
    /**
     * Unregisters a notification observer.
     * 
     * @param observer the observer to unregister
     */
    public void unregisterNotificationObserver(com.orrs.notification.ReservationObserver observer) {
        notificationService.unregisterObserver(observer);
    }
    
    /**
     * Gets the billing engine for advanced billing operations.
     * 
     * @return the BillingEngine
     */
    public BillingEngine getBillingEngine() {
        return billingEngine;
    }
    
    /**
     * Gets the validation engine for custom validation rules.
     * 
     * @return the ValidationEngine
     */
    public ValidationEngine getValidationEngine() {
        return validationEngine;
    }
}
