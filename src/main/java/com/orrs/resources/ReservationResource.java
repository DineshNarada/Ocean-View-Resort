package com.orrs.resources;

import com.orrs.domain.Reservation;
import com.orrs.domain.Guest;
import com.orrs.domain.RoomType;
import com.orrs.manager.ReservationManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Resource for reservation operations
 * Endpoints: GET, POST, PUT, DELETE /resources/reservations
 */
@Path("reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {
    
    private static ReservationManager reservationManager = new ReservationManager();
    
    /**
     * Create a new reservation
     * POST /api/reservations
     */
    @POST
    public Response createReservation(CreateReservationRequest request) {
        try {
            if (request == null || !isValidRequest(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid reservation data"))
                    .build();
            }
            
            Guest guest = new Guest(request.getGuestId(), request.getGuestName(), 
                                   request.getGuestEmail(), request.getGuestPhone());
            RoomType roomType = new RoomType(request.getRoomTypeId(), request.getRoomType(), 
                                            request.getRatePerNight());
            
            LocalDate checkIn = LocalDate.parse(request.getCheckIn());
            LocalDate checkOut = LocalDate.parse(request.getCheckOut());
            
            if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Check-out date must be after check-in date"))
                    .build();
            }
            
            Reservation reservation = reservationManager.addReservation(guest, roomType, checkIn, checkOut);
            
            return Response.status(Response.Status.CREATED)
                .entity(new ReservationResponse(reservation))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating reservation: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get a specific reservation by ID
     * GET /api/reservations/{id}
     */
    @GET
    @Path("{id}")
    public Response getReservation(@PathParam("id") String id) {
        try {
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            Optional<Reservation> reservation = reservationManager.findReservation(id);
            
            if (reservation.isPresent()) {
                return Response.ok(new ReservationResponse(reservation.get()))
                    .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Reservation not found"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservation: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all reservations
     * GET /api/reservations
     */
    @GET
    public Response getAllReservations() {
        try {
            List<Reservation> reservations = reservationManager.getAllReservations();
            List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
            
            return Response.ok(responses).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservations: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update an existing reservation
     * PUT /api/reservations/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateReservation(@PathParam("id") String id, UpdateReservationRequest request) {
        try {
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            Optional<Reservation> existingReservation = reservationManager.findReservation(id);
            
            if (!existingReservation.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Reservation not found"))
                    .build();
            }
            
            Reservation reservation = existingReservation.get();
            
            if (request.getCheckIn() != null) {
                reservation.setCheckIn(LocalDate.parse(request.getCheckIn()));
            }
            if (request.getCheckOut() != null) {
                reservation.setCheckOut(LocalDate.parse(request.getCheckOut()));
            }
            
            return Response.ok(new ReservationResponse(reservation))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating reservation: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Cancel/Delete a reservation
     * DELETE /api/reservations/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteReservation(@PathParam("id") String id) {
        try {
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            boolean deleted = reservationManager.cancelReservation(id);
            
            if (deleted) {
                return Response.ok(new AuthResource.SuccessResponse("Reservation cancelled successfully"))
                    .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Reservation not found"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error cancelling reservation: " + e.getMessage()))
                .build();
        }
    }
    
    private boolean isValidRequest(CreateReservationRequest request) {
        return request.getGuestName() != null && !request.getGuestName().isEmpty()
            && request.getCheckIn() != null && !request.getCheckIn().isEmpty()
            && request.getCheckOut() != null && !request.getCheckOut().isEmpty()
            && request.getRoomType() != null && !request.getRoomType().isEmpty();
    }
    
    // ==================== DTOs ====================
    
    public static class CreateReservationRequest {
        private int guestId;
        private String guestName;
        private String guestEmail;
        private String guestPhone;
        private int roomTypeId;
        private String roomType;
        private double ratePerNight;
        private String checkIn;
        private String checkOut;
        
        public CreateReservationRequest() {}
        
        // Getters and Setters
        public int getGuestId() { return guestId; }
        public void setGuestId(int guestId) { this.guestId = guestId; }
        
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        
        public String getGuestEmail() { return guestEmail; }
        public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
        
        public String getGuestPhone() { return guestPhone; }
        public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
        
        public int getRoomTypeId() { return roomTypeId; }
        public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }
        
        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }
        
        public double getRatePerNight() { return ratePerNight; }
        public void setRatePerNight(double ratePerNight) { this.ratePerNight = ratePerNight; }
        
        public String getCheckIn() { return checkIn; }
        public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
        
        public String getCheckOut() { return checkOut; }
        public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    }
    
    public static class UpdateReservationRequest {
        private String checkIn;
        private String checkOut;
        
        public UpdateReservationRequest() {}
        
        public String getCheckIn() { return checkIn; }
        public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
        
        public String getCheckOut() { return checkOut; }
        public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
    }
    
    public static class ReservationResponse {
        private String id;
        private String guestName;
        private String roomType;
        private String checkIn;
        private String checkOut;
        private String status;
        
        public ReservationResponse() {}
        
        public ReservationResponse(Reservation reservation) {
            this.id = reservation.getId() != null ? reservation.getId() : String.valueOf(reservation.getReservationId());
            this.guestName = reservation.getGuest() != null ? reservation.getGuest().getName() : "N/A";
            this.roomType = reservation.getRoomType() != null ? reservation.getRoomType().getRoomType() : "N/A";
            this.checkIn = reservation.getCheckIn() != null ? reservation.getCheckIn().toString() : "N/A";
            this.checkOut = reservation.getCheckOut() != null ? reservation.getCheckOut().toString() : "N/A";
            this.status = reservation.getStatus() != null ? reservation.getStatus().toString() : "PENDING";
        }
        
        public String getId() { return id; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public String getCheckIn() { return checkIn; }
        public String getCheckOut() { return checkOut; }
        public String getStatus() { return status; }
    }
    
    public static class ErrorResponse {
        private String error;
        private long timestamp;
        
        public ErrorResponse() {}
        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getError() { return error; }
        public long getTimestamp() { return timestamp; }
    }
}
