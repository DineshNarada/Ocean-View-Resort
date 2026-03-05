package com.orrs.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTypeTest {

    @Test
    void testConstructor() {
        RoomType roomType = new RoomType("Single", 5000.0);
        assertEquals("Single", roomType.getTypeName());
        assertEquals(5000.0, roomType.getRatePerNight());
    }

    @Test
    void testSetters() {
        RoomType roomType = new RoomType("Single", 5000.0);
        roomType.setTypeName("Double");
        roomType.setRatePerNight(7500.0);
        assertEquals("Double", roomType.getTypeName());
        assertEquals(7500.0, roomType.getRatePerNight());
    }

    @Test
    void testToString() {
        RoomType roomType = new RoomType("Single", 5000.0);
        String expected = "RoomType{typeName='Single', ratePerNight=5000.0}";
        assertEquals(expected, roomType.toString());
    }
}