package com.orrs.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    @Test
    void testConstructor() {
        Bill bill = new Bill("RES1001", 2, 5000.0);
        assertEquals("RES1001", bill.getReservationId());
        assertEquals(10000.0, bill.getAmount());
        assertEquals(2, bill.getNumberOfNights());
        assertEquals(5000.0, bill.getRatePerNight());
    }

    @Test
    void testPrint() {
        Bill bill = new Bill("RES1001", 2, 5000.0);
        // Test that print doesn't throw exception
        assertDoesNotThrow(() -> bill.print());
    }

    @Test
    void testToString() {
        Bill bill = new Bill("RES1001", 2, 5000.0);
        String expected = "Bill{reservationId='RES1001', amount=10000.0, numberOfNights=2, ratePerNight=5000.0}";
        assertEquals(expected, bill.toString());
    }
}