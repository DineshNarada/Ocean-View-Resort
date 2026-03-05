package com.orrs.manager;

import com.orrs.domain.Guest;
import com.orrs.domain.RoomType;
import com.orrs.domain.Reservation;
import com.orrs.domain.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ReservationManagerTest {

    private ReservationManager manager;
    private Guest guest;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        manager = new ReservationManager();
        guest = new Guest("John Doe", "123 Main St", "555-1234");
        roomType = new RoomType("Single", 5000.0);
        checkIn = LocalDate.of(2026, 3, 10);
        checkOut = LocalDate.of(2026, 3, 12);
    }

    @Test
    void testAddReservation() {
        Reservation reservation = manager.addReservation(guest, roomType, checkIn, checkOut);
        assertNotNull(reservation);
        assertEquals(guest, reservation.getGuest());
        assertEquals(roomType, reservation.getRoomType());
        assertEquals(checkIn, reservation.getCheckIn());
        assertEquals(checkOut, reservation.getCheckOut());
        assertTrue(reservation.getId().startsWith("RES"));
    }

    @Test
    void testFindReservation() {
        Reservation added = manager.addReservation(guest, roomType, checkIn, checkOut);
        Optional<Reservation> found = manager.findReservation(added.getId());
        assertTrue(found.isPresent());
        assertEquals(added, found.get());
    }

    @Test
    void testFindReservationNotFound() {
        Optional<Reservation> found = manager.findReservation("NONEXISTENT");
        assertFalse(found.isPresent());
    }

    @Test
    void testCalculateBill() {
        Reservation added = manager.addReservation(guest, roomType, checkIn, checkOut);
        Bill bill = manager.calculateBill(added.getId());
        assertNotNull(bill);
        assertEquals(added.getId(), bill.getReservationId());
        assertEquals(2, bill.getNumberOfNights());
        assertEquals(5000.0, bill.getRatePerNight());
        assertEquals(10000.0, bill.getAmount());
    }

    @Test
    void testCalculateBillNotFound() {
        Bill bill = manager.calculateBill("NONEXISTENT");
        assertNull(bill);
    }

    @Test
    void testGetAllReservations() {
        Reservation res1 = manager.addReservation(guest, roomType, checkIn, checkOut);
        Reservation res2 = manager.addReservation(guest, roomType, checkIn.plusDays(5), checkOut.plusDays(5));
        List<Reservation> all = manager.getAllReservations();
        assertEquals(2, all.size());
        assertTrue(all.contains(res1));
        assertTrue(all.contains(res2));
    }

    @Test
    void testDeleteReservation() {
        Reservation added = manager.addReservation(guest, roomType, checkIn, checkOut);
        assertTrue(manager.deleteReservation(added.getId()));
        Optional<Reservation> found = manager.findReservation(added.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteReservationNotFound() {
        assertFalse(manager.deleteReservation("NONEXISTENT"));
    }
}