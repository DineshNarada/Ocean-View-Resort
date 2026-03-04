package com.orrs.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a reservation (booking) at the Ocean View Resort.
 * A reservation is composed of a Guest and a RoomType with specific check-in and check-out dates.
 */
public class Reservation {
    private String id;
    private Guest guest;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;

    /**
     * Constructs a Reservation with the provided details.
     *
     * @param id the unique reservation number
     * @param guest the guest making the reservation
     * @param roomType the type of room reserved
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     */
    public Reservation(String id, Guest guest, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guest = guest;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    /**
     * Calculates the duration of the reservation in nights.
     *
     * @return the number of nights between check-in and check-out
     */
    public int getDuration() {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // Getters
    public String getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", guest=" + guest +
                ", roomType=" + roomType +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", duration=" + getDuration() + " nights" +
                '}';
    }
}
