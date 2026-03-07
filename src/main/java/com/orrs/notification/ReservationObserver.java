package com.orrs.notification;

import com.orrs.domain.Reservation;

/**
 * Observer interface for reservation notifications.
 * Implements the Observer design pattern for event-driven notifications.
 * 
 * Observers are notified when reservation events occur:
 * - Reservation created
 * - Reservation confirmed
 * - Reservation cancelled
 * - Check-in reminder
 * - Billing notification
 */
public interface ReservationObserver {
    
    /**
     * Called when a new reservation is created.
     * 
     * @param reservation the newly created reservation
     */
    void onReservationCreated(Reservation reservation);
    
    /**
     * Called when a reservation is confirmed.
     * 
     * @param reservation the confirmed reservation
     */
    void onReservationConfirmed(Reservation reservation);
    
    /**
     * Called when a reservation is cancelled.
     * 
     * @param reservation the cancelled reservation
     * @param reason the cancellation reason
     */
    void onReservationCancelled(Reservation reservation, String reason);
    
    /**
     * Called to send a check-in reminder.
     * 
     * @param reservation the reservation to remind about
     */
    void onCheckInReminder(Reservation reservation);
}
