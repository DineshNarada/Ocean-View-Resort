package com.orrs.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    @Test
    void testConstructor() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        assertEquals("John Doe", guest.getName());
        assertEquals("123 Main St", guest.getAddress());
        assertEquals("555-1234", guest.getContact());
    }

    @Test
    void testSetters() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        guest.setName("Jane Doe");
        guest.setAddress("456 Elm St");
        guest.setContact("555-5678");
        assertEquals("Jane Doe", guest.getName());
        assertEquals("456 Elm St", guest.getAddress());
        assertEquals("555-5678", guest.getContact());
    }

    @Test
    void testToString() {
        Guest guest = new Guest("John Doe", "123 Main St", "555-1234");
        String expected = "Guest{name='John Doe', address='123 Main St', contact='555-1234'}";
        assertEquals(expected, guest.toString());
    }
}