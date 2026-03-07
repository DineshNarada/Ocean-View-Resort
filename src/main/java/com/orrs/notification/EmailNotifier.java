package com.orrs.notification;

import com.orrs.domain.Reservation;
import java.time.format.DateTimeFormatter;

/**
 * Email notification implementation.
 * Sends email notifications to guests for reservation events.
 * 
 * In production, this would integrate with an email service provider
 * like JavaMail API, SendGrid, or AWS SES.
 */
public class EmailNotifier implements ReservationObserver {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public void onReservationCreated(Reservation reservation) {
        String subject = "Reservation Confirmation - Ocean View Resort";
        String body = buildReservationConfirmationEmail(reservation);
        sendEmail(reservation.getGuest().getEmail(), subject, body);
    }
    
    @Override
    public void onReservationConfirmed(Reservation reservation) {
        String subject = "Reservation Confirmed - Ocean View Resort";
        String body = buildReservationConfirmedEmail(reservation);
        sendEmail(reservation.getGuest().getEmail(), subject, body);
    }
    
    @Override
    public void onReservationCancelled(Reservation reservation, String reason) {
        String subject = "Reservation Cancelled - Ocean View Resort";
        String body = buildReservationCancelledEmail(reservation, reason);
        sendEmail(reservation.getGuest().getEmail(), subject, body);
    }
    
    @Override
    public void onCheckInReminder(Reservation reservation) {
        String subject = "Check-in Reminder - Ocean View Resort";
        String body = buildCheckInReminderEmail(reservation);
        sendEmail(reservation.getGuest().getEmail(), subject, body);
    }
    
    /**
     * Builds the reservation confirmation email body.
     */
    private String buildReservationConfirmationEmail(Reservation reservation) {
        return "Dear " + reservation.getGuest().getName() + ",\n\n" +
               "Thank you for your reservation at Ocean View Resort!\n\n" +
               "Reservation Details:\n" +
               "- Reservation ID: " + reservation.getId() + "\n" +
               "- Guest: " + reservation.getGuest().getName() + "\n" +
               "- Room Type: " + reservation.getRoomType().getTypeName() + "\n" +
               "- Check-in: " + reservation.getCheckIn().format(DATE_FORMATTER) + "\n" +
               "- Check-out: " + reservation.getCheckOut().format(DATE_FORMATTER) + "\n" +
               "- Status: " + reservation.getStatus() + "\n\n" +
               "We look forward to hosting you!\n\n" +
               "Best regards,\n" +
               "Ocean View Resort Team";
    }
    
    /**
     * Builds the reservation confirmed email body.
     */
    private String buildReservationConfirmedEmail(Reservation reservation) {
        return "Dear " + reservation.getGuest().getName() + ",\n\n" +
               "Your reservation has been confirmed!\n\n" +
               "Reservation ID: " + reservation.getId() + "\n" +
               "Check-in Date: " + reservation.getCheckIn().format(DATE_FORMATTER) + "\n" +
               "Check-out Date: " + reservation.getCheckOut().format(DATE_FORMATTER) + "\n\n" +
               "Please arrive between 15:00 and 23:00 on check-in day.\n\n" +
               "Best regards,\n" +
               "Ocean View Resort Team";
    }
    
    /**
     * Builds the reservation cancelled email body.
     */
    private String buildReservationCancelledEmail(Reservation reservation, String reason) {
        return "Dear " + reservation.getGuest().getName() + ",\n\n" +
               "We confirm that your reservation has been cancelled.\n\n" +
               "Reservation ID: " + reservation.getId() + "\n" +
               "Cancellation Reason: " + reason + "\n\n" +
               "If you have any questions, please contact our customer service.\n\n" +
               "Best regards,\n" +
               "Ocean View Resort Team";
    }
    
    /**
     * Builds the check-in reminder email body.
     */
    private String buildCheckInReminderEmail(Reservation reservation) {
        return "Dear " + reservation.getGuest().getName() + ",\n\n" +
               "This is a reminder that your check-in date is approaching!\n\n" +
               "Reservation ID: " + reservation.getId() + "\n" +
               "Check-in Date: " + reservation.getCheckIn().format(DATE_FORMATTER) + "\n" +
               "Room Type: " + reservation.getRoomType().getTypeName() + "\n\n" +
               "Please remember to check in between 15:00 and 23:00.\n\n" +
               "Best regards,\n" +
               "Ocean View Resort Team";
    }
    
    /**
     * Sends the email. In production, this would use JavaMail API or similar.
     */
    private void sendEmail(String recipient, String subject, String body) {
        if (recipient == null || recipient.isEmpty()) {
            System.err.println("Error: Invalid email recipient");
            return;
        }
        
        System.out.println("Sending email to: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
        System.out.println("---");
        
        // In production, actual email would be sent here using:
        // - JavaMail API (javax.mail.*)
        // - Jakarta Mail (jakarta.mail.*)
        // - SendGrid API
        // - AWS SES
        // - etc.
    }
}
