package com.orrs.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testConstructor() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        RoomType roomType = new RoomType("Single", 5000.0);
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);
        Reservation reservation = new Reservation("RES1001", guest, roomType, checkIn, checkOut);
        assertEquals("RES1001", reservation.getId());
        assertEquals(guest, reservation.getGuest());
        assertEquals(roomType, reservation.getRoomType());
        assertEquals(checkIn, reservation.getCheckIn());
        assertEquals(checkOut, reservation.getCheckOut());
    }

    @Test
    void testGetDuration() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        RoomType roomType = new RoomType("Single", 5000.0);
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);
        Reservation reservation = new Reservation("RES1001", guest, roomType, checkIn, checkOut);
        assertEquals(2, reservation.getDuration());
    }

    @Test
    void testSetters() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        RoomType roomType = new RoomType("Single", 5000.0);
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);
        Reservation reservation = new Reservation("RES1001", guest, roomType, checkIn, checkOut);

        Guest newGuest = new Guest("Jane Doe", "456 Elm St", "555-5678");
        RoomType newRoomType = new RoomType("Double", 7500.0);
        LocalDate newCheckIn = LocalDate.of(2026, 3, 15);
        LocalDate newCheckOut = LocalDate.of(2026, 3, 17);

        reservation.setId("RES1002");
        reservation.setGuest(newGuest);
        reservation.setRoomType(newRoomType);
        reservation.setCheckIn(newCheckIn);
        reservation.setCheckOut(newCheckOut);

        assertEquals("RES1002", reservation.getId());
        assertEquals(newGuest, reservation.getGuest());
        assertEquals(newRoomType, reservation.getRoomType());
        assertEquals(newCheckIn, reservation.getCheckIn());
        assertEquals(newCheckOut, reservation.getCheckOut());
    }

    @Test
    void testToString() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        RoomType roomType = new RoomType("Single", 5000.0);
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);
        Reservation reservation = new Reservation("RES1001", guest, roomType, checkIn, checkOut);
        String expected = "Reservation{id='RES1001', guest=" + guest + ", roomType=" + roomType + ", checkIn=2026-03-10, checkOut=2026-03-12, duration=2 nights}";
        assertEquals(expected, reservation.toString());
    }
}