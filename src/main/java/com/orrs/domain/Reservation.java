package com.orrs.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a reservation (booking) at the Ocean View Resort.
 * A reservation references a specific Room (not just a RoomType) with check-in and check-out dates.
 */
public class Reservation {
    private int reservationId;
    private String reservationNumber;
    private int guestId;
    private int roomId;  // Reference to specific room, not just room type
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalCost;
    private ReservationStatus status;

    /**
     * Default constructor
     */
    public Reservation() {
    }

    /**
     * Constructs a Reservation with the provided details.
     *
     * @param reservationNumber the unique reservation number
     * @param guestId the ID of the guest making the reservation
     * @param roomId the ID of the specific room reserved
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     */
    public Reservation(String reservationNumber, int guestId, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationNumber = reservationNumber;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Calculates the duration of the reservation in nights.
     *
     * @return the number of nights between check-in and check-out
     */
    public int getDuration() {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    /**
     * Maps a string status to ReservationStatus enum
     */
    public ReservationStatus mapStatus(String statusStr) {
        return ReservationStatus.valueOf(statusStr);
    }

    // Getters
    public int getReservationId() {
        return reservationId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    // Setters
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalCost=" + totalCost +
                ", status=" + status +
                ", duration=" + getDuration() + " nights" +
                '}';
    }

    /**
     * Enumeration for reservation status
     */
    public enum ReservationStatus {
        PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED
    }
}
