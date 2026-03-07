package com.orrs.controller;

import com.orrs.domain.Guest;
import com.orrs.domain.Reservation;
import com.orrs.domain.RoomType;
import com.orrs.domain.Staff;
import com.orrs.manager.ReservationManager;
import com.orrs.validator.ReservationValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ReservationServlet handles reservation management operations.
 * 
 * Processes:
 * - Display reservation form
 * - Create new reservations
 * - View existing reservations
 * - Cancel reservations
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservation"})
public class ReservationServlet extends HttpServlet {
    
    private ReservationManager reservationManager;
    private ReservationValidator validator;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Sample room types for demonstration
    private List<RoomType> sampleRoomTypes;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reservationManager = new ReservationManager();
        validator = new ReservationValidator();
        initializeSampleRoomTypes();
    }
    
    private void initializeSampleRoomTypes() {
        sampleRoomTypes = new ArrayList<>();
        RoomType single = new RoomType();
        single.setRoomTypeId(1);
        single.setTypeName("Single Room");
        single.setRatePerNight(100.0);
        
        RoomType double_room = new RoomType();
        double_room.setRoomTypeId(2);
        double_room.setTypeName("Double Room");
        double_room.setRatePerNight(150.0);
        
        RoomType suite = new RoomType();
        suite.setRoomTypeId(3);
        suite.setTypeName("Suite");
        suite.setRatePerNight(250.0);
        
        sampleRoomTypes.add(single);
        sampleRoomTypes.add(double_room);
        sampleRoomTypes.add(suite);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null || "list".equals(action)) {
            // Display all reservations
            try {
                List<Reservation> reservations = reservationManager.getAllReservations();
                request.setAttribute("reservations", reservations);
                request.getRequestDispatcher("/reservation-display.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error retrieving reservations: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else if ("new".equals(action)) {
            // Display reservation form
            request.setAttribute("roomTypes", sampleRoomTypes);
            request.getRequestDispatcher("/reservation-form.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            // Display edit form for existing reservation
            String reservationId = request.getParameter("id");
            try {
                Optional<Reservation> reservation = reservationManager.findReservation(reservationId);
                if (reservation.isPresent()) {
                    request.setAttribute("reservation", reservation.get());
                    request.setAttribute("roomTypes", sampleRoomTypes);
                    request.getRequestDispatcher("/reservation-form.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Reservation not found");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error loading reservation: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            handleSaveReservation(request, response);
        } else if ("cancel".equals(action)) {
            handleCancelReservation(request, response);
        }
    }
    
    private void handleSaveReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Parse reservation data
            String guestName = request.getParameter("guestName");
            String guestEmail = request.getParameter("guestEmail");
            String guestPhone = request.getParameter("guestPhone");
            String guestAddress = request.getParameter("guestAddress");
            String guestCity = request.getParameter("guestCity");
            String guestCountry = request.getParameter("guestCountry");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String roomTypeIdStr = request.getParameter("roomTypeId");
            
            // Validate basic input
            List<String> errors = new ArrayList<>();
            
            if (guestName == null || guestName.trim().isEmpty() || guestName.length() < 3) {
                errors.add("Guest name must be at least 3 characters");
            }
            if (guestEmail == null || !guestEmail.contains("@")) {
                errors.add("Valid email is required");
            }
            if (guestPhone == null || guestPhone.trim().isEmpty()) {
                errors.add("Phone number is required");
            }
            if (checkInStr == null || checkInStr.isEmpty()) {
                errors.add("Check-in date is required");
            }
            if (checkOutStr == null || checkOutStr.isEmpty()) {
                errors.add("Check-out date is required");
            }
            if (roomTypeIdStr == null || roomTypeIdStr.isEmpty()) {
                errors.add("Room type is required");
            }
            
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("roomTypes", sampleRoomTypes);
                request.getRequestDispatcher("/reservation-form.jsp").forward(request, response);
                return;
            }
            
            // Parse dates and create guest/reservation
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);
            
            if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
                errors.add("Check-out date must be after check-in date");
                request.setAttribute("errors", errors);
                request.setAttribute("roomTypes", sampleRoomTypes);
                request.getRequestDispatcher("/reservation-form.jsp").forward(request, response);
                return;
            }
            
            // Create guest and get room type
            Guest guest = new Guest(guestName, guestEmail, guestPhone, guestAddress, guestCity, guestCountry);
            int roomTypeId = Integer.parseInt(roomTypeIdStr);
            RoomType roomType = sampleRoomTypes.stream()
                .filter(rt -> rt.getRoomTypeId() == roomTypeId)
                .findFirst()
                .orElse(sampleRoomTypes.get(0));
            
            // Add reservation
            Reservation saved = reservationManager.addReservation(guest, roomType, checkIn, checkOut);
            
            response.sendRedirect(request.getContextPath() + "/reservation?action=list");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error saving reservation: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void handleCancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String reservationId = request.getParameter("id");
            
            if (reservationId == null || reservationId.isEmpty()) {
                request.setAttribute("error", "Reservation ID is required for cancellation");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            reservationManager.deleteReservation(reservationId);
            response.sendRedirect(request.getContextPath() + "/reservation?action=list");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error cancelling reservation: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("staff") != null;
    }
}
