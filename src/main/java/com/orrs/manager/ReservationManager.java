package com.orrs.manager;

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
 */
public class ReservationManager {
    private List<Reservation> reservations;
    private int reservationCounter;

    public ReservationManager() {
        this.reservations = new ArrayList<>();
        this.reservationCounter = 1000; // Start reservation IDs from 1000
    }

    /**
     * Adds a new reservation to the system.
     *
     * @param guest the guest information
     * @param roomType the room type for the reservation
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @return the created Reservation object
     */
    public Reservation addReservation(Guest guest, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        String reservationId = "RES" + (++reservationCounter);
        Reservation reservation = new Reservation(reservationId, guest, roomType, checkIn, checkOut);
        reservations.add(reservation);
        return reservation;
    }

    /**
     * Finds a reservation by its ID.
     *
     * @param id the reservation ID to search for
     * @return an Optional containing the Reservation if found, empty otherwise
     */
    public Optional<Reservation> findReservation(String id) {
        return reservations.stream()
                .filter(res -> res.getId().equals(id))
                .findFirst();
    }

    /**
     * Calculates the bill for a specific reservation.
     *
     * @param reservationId the ID of the reservation
     * @return a Bill object, or null if reservation not found
     */
    public Bill calculateBill(String reservationId) {
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
     * Retrieves all reservations.
     *
     * @return a list of all reservations
     */
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param id the reservation ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReservation(String id) {
        return reservations.removeIf(res -> res.getId().equals(id));
    }
}
