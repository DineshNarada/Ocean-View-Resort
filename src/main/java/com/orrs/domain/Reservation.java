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
    private String id;  // For test support
    private String reservationNumber;
    private int guestId;
    private Guest guest;  // For test support
    private int roomId;  // Reference to specific room, not just room type
    private RoomType roomType;  // For test support
    private LocalDate checkInDate;
    private LocalDate checkIn;  // For test support
    private LocalDate checkOutDate;
    private LocalDate checkOut;  // For test support
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
     * Constructs a Reservation with Guest and RoomType objects (simplified for testing).
     *
     * @param id the unique reservation ID
     * @param guest the Guest object
     * @param roomType the RoomType object
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     */
    public Reservation(String id, Guest guest, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guest = guest;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
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

    public String getId() {
        return id != null ? id : reservationNumber;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public int getGuestId() {
        return guestId;
    }

    public Guest getGuest() {
        return guest;
    }

    public int getRoomId() {
        return roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckIn() {
        return checkIn != null ? checkIn : checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getCheckOut() {
        return checkOut != null ? checkOut : checkOutDate;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (id != null && guest != null && roomType != null) {
            return "Reservation{id='" + id + "', guest=" + guest + ", roomType=" + roomType + ", checkIn=" + getCheckIn() + ", checkOut=" + getCheckOut() + ", duration=" + getDuration() + " nights}";
        }
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
