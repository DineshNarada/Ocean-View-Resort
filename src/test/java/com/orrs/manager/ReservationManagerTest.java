package com.orrs.manager;

import com.orrs.dao.IBillDAO;
import com.orrs.dao.IGuestDAO;
import com.orrs.dao.IReservationDAO;
import com.orrs.domain.Guest;
import com.orrs.domain.RoomType;
import com.orrs.domain.Reservation;
import com.orrs.domain.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationManagerTest {

    private ReservationManager manager;
    
    @Mock
    private IReservationDAO reservationDAO;
    
    @Mock
    private IGuestDAO guestDAO;
    
    @Mock
    private IBillDAO billDAO;
    
    private Guest guest;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Reservation testReservation;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Create manager with mocked DAOs using reflection
        manager = new ReservationManager();
        injectMockedDAOs();
        
        // Test data
        guest = new Guest("John Doe", "123 Main St", "555-1234");
        guest.setGuestId(1);
        roomType = new RoomType("Single", 5000.0);
        checkIn = LocalDate.of(2026, 3, 10);
        checkOut = LocalDate.of(2026, 3, 12);
        
        // Setup default test reservation
        testReservation = new Reservation();
        testReservation.setId("RES001");
        testReservation.setReservationNumber("RES001");
        testReservation.setGuest(guest);
        testReservation.setRoomType(roomType);
        testReservation.setCheckInDate(checkIn);
        testReservation.setCheckOutDate(checkOut);
        testReservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
    }
    
    /**
     * Injects mocked DAOs into the manager using reflection
     */
    private void injectMockedDAOs() throws Exception {
        java.lang.reflect.Field reservationDAOField = ReservationManager.class.getDeclaredField("reservationDAO");
        reservationDAOField.setAccessible(true);
        reservationDAOField.set(manager, reservationDAO);
        
        java.lang.reflect.Field guestDAOField = ReservationManager.class.getDeclaredField("guestDAO");
        guestDAOField.setAccessible(true);
        guestDAOField.set(manager, guestDAO);
        
        java.lang.reflect.Field billDAOField = ReservationManager.class.getDeclaredField("billDAO");
        billDAOField.setAccessible(true);
        billDAOField.set(manager, billDAO);
    }

    @Test
    void testAddReservation() throws Exception {
        // Arrange
        doNothing().when(guestDAO).create(any(Guest.class));
        doNothing().when(reservationDAO).create(any(Reservation.class));
        
        // Act
        Reservation reservation = manager.addReservation(guest, roomType, checkIn, checkOut);
        
        // Assert
        assertNotNull(reservation);
        assertEquals(guest, reservation.getGuest());
        assertEquals(roomType, reservation.getRoomType());
        assertEquals(checkIn, reservation.getCheckInDate());
        assertEquals(checkOut, reservation.getCheckOutDate());
        verify(guestDAO, times(1)).create(guest);
        verify(reservationDAO, times(1)).create(any(Reservation.class));
    }

    @Test
    void testFindReservation() throws Exception {
        // Arrange
        when(reservationDAO.findByReservationNumber("RES001")).thenReturn(testReservation);
        
        // Act
        Optional<Reservation> found = manager.findReservation("RES001");
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals(testReservation, found.get());
    }

    @Test
    void testFindReservationNotFound() throws Exception {
        // Arrange
        when(reservationDAO.findByReservationNumber("NONEXISTENT")).thenReturn(null);
        when(reservationDAO.readById(anyInt())).thenReturn(null);
        
        // Act
        Optional<Reservation> found = manager.findReservation("NONEXISTENT");
        
        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testCalculateBill() throws Exception {
        // Arrange - mock readById since "1" will be parsed as integer
        when(reservationDAO.readById(1)).thenReturn(testReservation);
        
        // Act
        Bill bill = manager.calculateBill("1");
        
        // Assert
        assertNotNull(bill);
        assertEquals("1", bill.getReservationIdStr());
        assertEquals(2, bill.getNumberOfNights());
        assertEquals(5000.0, bill.getRatePerNight());
    }

    @Test
    void testCalculateBillNotFound() throws Exception {
        // Arrange
        when(reservationDAO.readById(anyInt())).thenReturn(null);
        when(reservationDAO.findByReservationNumber(anyString())).thenReturn(null);
        
        // Act
        Bill bill = manager.calculateBill("999");
        
        // Assert
        assertNull(bill);
    }

    @Test
    void testGetAllReservations() throws Exception {
        // Arrange
        Reservation res1 = new Reservation();
        res1.setId("RES001");
        res1.setReservationNumber("RES001");
        res1.setGuest(guest);
        res1.setRoomType(roomType);
        
        Reservation res2 = new Reservation();
        res2.setId("RES002");
        res2.setReservationNumber("RES002");
        res2.setGuest(guest);
        res2.setRoomType(roomType);
        
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(res1);
        reservations.add(res2);
        
        when(reservationDAO.readAll()).thenReturn(reservations);
        
        // Act
        List<Reservation> all = manager.getAllReservations();
        
        // Assert
        assertEquals(2, all.size());
        assertTrue(all.contains(res1));
        assertTrue(all.contains(res2));
        verify(reservationDAO, times(1)).readAll();
    }

    @Test
    void testDeleteReservation() throws Exception {
        // Arrange
        doNothing().when(reservationDAO).delete(1);
        when(reservationDAO.exists(1)).thenReturn(false);
        when(reservationDAO.readById(1)).thenReturn(null);
        
        // Act
        boolean deleted = manager.deleteReservation("1");
        
        // Assert
        assertTrue(deleted);
        verify(reservationDAO, times(1)).delete(1);
    }

    @Test
    void testDeleteReservationNotFound() throws Exception {
        // Arrange
        doNothing().when(reservationDAO).delete(999);
        when(reservationDAO.exists(999)).thenReturn(false);
        
        // Act
        boolean deleted = manager.deleteReservation("999");
        
        // Assert
        assertTrue(deleted);
    }
}