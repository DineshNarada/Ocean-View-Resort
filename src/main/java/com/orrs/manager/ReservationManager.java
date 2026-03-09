package com.orrs.manager;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IBillDAO;
import com.orrs.dao.IGuestDAO;
import com.orrs.dao.IReservationDAO;
import com.orrs.domain.Reservation;
import com.orrs.domain.Guest;
import com.orrs.domain.RoomType;
import com.orrs.domain.Bill;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages reservation operations for the Ocean View Resort system.
 * Acts as a facade/controller coordinating CRUD operations on reservations.
 * Integrates with the DAO layer for persistent database storage.
 */
public class ReservationManager {
    private IReservationDAO reservationDAO;
    private IGuestDAO guestDAO;
    private IBillDAO billDAO;

    public ReservationManager() {
        DAOFactory factory = DAOFactory.getInstance();
        this.reservationDAO = factory.getReservationDAO();
        this.guestDAO = factory.getGuestDAO();
        this.billDAO = factory.getBillDAO();
    }

    /**
     * Adds a new reservation to the system.
     * Persists both guest and reservation to the database.
     * Automatically assigns an available room of the specified type.
     *
     * @param guest the guest information
     * @param roomType the room type for the reservation
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @return the created Reservation object
     * @throws Exception if database operation fails or no rooms available
     */
    public Reservation addReservation(Guest guest, RoomType roomType, LocalDate checkIn, LocalDate checkOut) throws Exception {
        // Validate room type
        if (roomType == null || roomType.getRoomTypeId() <= 0) {
            throw new Exception("Invalid room type provided");
        }
        
        // Find an available room of this type for the given dates
        List<Integer> availableRoomIds = reservationDAO.findAvailableRoomsByType(roomType.getRoomTypeId(), checkIn, checkOut);
        if (availableRoomIds.isEmpty()) {
            throw new Exception("No available rooms of type " + roomType.getTypeName() + " for the selected dates");
        }
        
        // Save guest to database first
        guestDAO.create(guest);
        
        // Create reservation with guest and room type
        Reservation reservation = new Reservation();
        
        // Set required fields
        reservation.setReservationNumber(generateReservationNumber());
        reservation.setGuestId(guest.getGuestId());
        reservation.setGuest(guest);
        reservation.setRoomType(roomType);
        reservation.setRoomId(availableRoomIds.get(0)); // Assign first available room
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        
        // Save reservation to database
        reservationDAO.create(reservation);
        
        return reservation;
    }

    /**
     * Generates a unique reservation number
     * Format: RES-YYYYMMDD-RANDOMNUMBER
     */
    private String generateReservationNumber() {
        return "RES-" + System.currentTimeMillis();
    }

    /**
     * Finds a reservation by its ID.
     *
     * @param id the reservation ID to search for
     * @return an Optional containing the Reservation if found, empty otherwise
     * @throws Exception if database operation fails
     */
    public Optional<Reservation> findReservation(String id) throws Exception {
        try {
            // Try to parse as integer ID
            int reservationId = Integer.parseInt(id);
            Reservation reservation = reservationDAO.readById(reservationId);
            return Optional.ofNullable(reservation);
        } catch (NumberFormatException e) {
            // If not an integer, try searching by reservation number
            try {
                Reservation reservation = reservationDAO.findByReservationNumber(id);
                return Optional.ofNullable(reservation);
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
    }

    /**
     * Calculates the bill for a specific reservation.
     *
     * @param reservationId the ID of the reservation
     * @return a Bill object, or null if reservation not found
     * @throws Exception if database operation fails
     */
    public Bill calculateBill(String reservationId) throws Exception {
        Optional<Reservation> reservation = findReservation(reservationId);
        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            int duration = res.getDuration();
            double rate = res.getRoomType().getRatePerNight();
            return new Bill(reservationId, duration, rate);
        }
        return null;
    }

    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of all reservations
     * @throws Exception if database operation fails
     */
    public List<Reservation> getAllReservations() throws Exception {
        return reservationDAO.readAll();
    }

    /**
     * Deletes a reservation by its ID from the database.
     *
     * @param id the reservation ID
     * @return true if deletion was successful, false otherwise
     * @throws Exception if database operation fails
     */
    public boolean deleteReservation(String id) throws Exception {
        try {
            int reservationId = Integer.parseInt(id);
            reservationDAO.delete(reservationId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Cancels a reservation by its ID (marks as cancelled).
     *
     * @param id the reservation ID
     * @return true if cancellation was successful, false otherwise
     * @throws Exception if database operation fails
     */
    public boolean cancelReservation(String id) throws Exception {
        Optional<Reservation> reservation = findReservation(id);
        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            res.setStatus(Reservation.ReservationStatus.CANCELLED);
            reservationDAO.update(res);
            return true;
        }
        return false;
    }

    /**
     * Adds a bill to the system (persists to database).
     *
     * @param bill the Bill object to add
     * @throws Exception if database operation fails
     */
    public void addBill(Bill bill) throws Exception {
        billDAO.create(bill);
    }

    /**
     * Finds a bill by reservation ID from the database.
     *
     * @param reservationId the reservation ID
     * @return an Optional containing the Bill if found, empty otherwise
     * @throws Exception if database operation fails
     */
    public Optional<Bill> findBill(String reservationId) throws Exception {
        List<Bill> allBills = billDAO.readAll();
        return allBills.stream()
                .filter(bill -> bill.getReservationIdStr().equals(reservationId) || 
                               String.valueOf(bill.getReservationId()).equals(reservationId))
                .findFirst();
    }

    /**
     * Retrieves all bills from the database.
     *
     * @return a list of all bills
     * @throws Exception if database operation fails
     */
    public List<Bill> getAllBills() throws Exception {
        return billDAO.readAll();
    }
}
