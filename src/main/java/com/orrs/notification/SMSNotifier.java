package com.orrs.notification;

import com.orrs.domain.Reservation;
import java.time.format.DateTimeFormatter;

/**
 * SMS notification implementation.
 * Sends SMS text messages to guests for reservation events.
 * 
 * In production, this would integrate with an SMS service provider
 * like Twilio, AWS SNS, or Nexmo.
 */
public class SMSNotifier implements ReservationObserver {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    @Override
    public void onReservationCreated(Reservation reservation) {
        String message = "Ocean View Resort: Reservation " + reservation.getId() + 
                       " created for " + reservation.getGuest().getName() + 
                       ". Check-in: " + reservation.getCheckIn().format(DATE_FORMATTER);
        sendSMS(reservation.getGuest().getPhone(), message);
    }
    
    @Override
    public void onReservationConfirmed(Reservation reservation) {
        String message = "Ocean View Resort: Your reservation " + reservation.getId() + 
                       " is confirmed! Check-in: " + 
                       reservation.getCheckIn().format(DATE_FORMATTER) + 
                       ". Room: " + reservation.getRoomType().getTypeName();
        sendSMS(reservation.getGuest().getPhone(), message);
    }
    
    @Override
    public void onReservationCancelled(Reservation reservation, String reason) {
        String message = "Ocean View Resort: Reservation " + reservation.getId() + 
                       " cancelled. Reason: " + reason + 
                       ". Contact us if you have questions.";
        sendSMS(reservation.getGuest().getPhone(), message);
    }
    
    @Override
    public void onCheckInReminder(Reservation reservation) {
        String message = "Ocean View Resort: Reminder! Check-in today between 15:00-23:00. " +
                       "Reservation: " + reservation.getId() + 
                       ". Room: " + reservation.getRoomType().getTypeName();
        sendSMS(reservation.getGuest().getPhone(), message);
    }
    
    /**
     * Sends the SMS message. In production, this would use Twilio or similar.
     */
    private void sendSMS(String phoneNumber, String message) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            System.err.println("Error: Invalid phone number");
            return;
        }
        
        System.out.println("Sending SMS to: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("---");
        
        // In production, actual SMS would be sent here using:
        // - Twilio API
        // - AWS SNS
        // - Nexmo/Vonage
        // - Infobip
        // - etc.
    }
}
