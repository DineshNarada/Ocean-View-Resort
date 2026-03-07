package com.orrs.notification;

import com.orrs.domain.Reservation;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService acts as the Subject in the Observer design pattern.
 * It manages a list of observers (notifiers) and notifies them of reservation events.
 * 
 * This service decouples notification logic from business logic, allowing
 * multiple notification channels to be supported simultaneously (email, SMS, etc.)
 */
public class NotificationService {
    
    private List<ReservationObserver> observers;
    
    /**
     * Constructs the NotificationService with an empty observer list.
     */
    public NotificationService() {
        this.observers = new ArrayList<>();
    }
    
    /**
     * Registers an observer to receive notifications.
     * 
     * @param observer the observer to register
     */
    public void registerObserver(ReservationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Unregisters an observer from receiving notifications.
     * 
     * @param observer the observer to unregister
     */
    public void unregisterObserver(ReservationObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Removes all observers.
     */
    public void removeAllObservers() {
        observers.clear();
    }
    
    /**
     * Gets the number of registered observers.
     * 
     * @return the count of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Notifies all observers that a reservation has been created.
     * 
     * @param reservation the newly created reservation
     */
    public void notifyReservationCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }
    
    /**
     * Notifies all observers that a reservation has been confirmed.
     * 
     * @param reservation the confirmed reservation
     */
    public void notifyReservationConfirmed(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationConfirmed(reservation);
        }
    }
    
    /**
     * Notifies all observers that a reservation has been cancelled.
     * 
     * @param reservation the cancelled reservation
     * @param reason the cancellation reason
     */
    public void notifyReservationCancelled(Reservation reservation, String reason) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservation, reason);
        }
    }
    
    /**
     * Notifies all observers to send a check-in reminder.
     * 
     * @param reservation the reservation to remind about
     */
    public void notifyCheckInReminder(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onCheckInReminder(reservation);
        }
    }
}
