package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IReservationDAO;
import com.orrs.domain.Reservation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

/**
 * REST Resource for reservation operations
 * Endpoints: GET, POST, PUT, DELETE /resources/reservations
 * Uses ReservationDAO for database operations
 */
@Path("reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {
    
    private final IReservationDAO reservationDAO = DAOFactory.getInstance().getReservationDAO();
    
    /**
     * Create a new reservation
     * POST /api/reservations
     */
    @POST
    public Response createReservation(Reservation reservation) {
        try {
            if (reservation == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation data is required"))
                    .build();
            }
            
            if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate()) || 
                reservation.getCheckOutDate().equals(reservation.getCheckInDate())) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Check-out date must be after check-in date"))
                    .build();
            }
            
            reservationDAO.create(reservation);
            return Response.status(Response.Status.CREATED)
                .entity(reservation)
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
    public Response getReservation(@PathParam("id") int id) {
        try {
            Reservation reservation = reservationDAO.readById(id);
            
            if (reservation == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Reservation not found"))
                    .build();
            }
            
            return Response.ok(reservation).build();
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
            List<Reservation> reservations = reservationDAO.readAll();
            return Response.ok(reservations).build();
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
    public Response updateReservation(@PathParam("id") int id, Reservation reservation) {
        try {
            if (reservation == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation data is required"))
                    .build();
            }
            
            reservation.setReservationId(id);
            reservationDAO.update(reservation);
            
            return Response.ok(new SuccessResponse("Reservation updated successfully")).build();
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
    public Response deleteReservation(@PathParam("id") int id) {
        try {
            reservationDAO.delete(id);
            return Response.ok(new SuccessResponse("Reservation cancelled successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error cancelling reservation: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Find reservation by reservation number
     * GET /api/reservations/number/{reservationNumber}
     */
    @GET
    @Path("number/{reservationNumber}")
    public Response findByReservationNumber(@PathParam("reservationNumber") String reservationNumber) {
        try {
            Reservation reservation = reservationDAO.findByReservationNumber(reservationNumber);
            
            if (reservation == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Reservation not found"))
                    .build();
            }
            
            return Response.ok(reservation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservation: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get reservations for a guest
     * GET /api/reservations/guest/{guestId}
     */
    @GET
    @Path("guest/{guestId}")
    public Response findByGuestId(@PathParam("guestId") int guestId) {
        try {
            List<Reservation> reservations = reservationDAO.findByGuestId(guestId);
            return Response.ok(reservations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservations: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get reservations by status
     * GET /api/reservations/status/{status}
     */
    @GET
    @Path("status/{status}")
    public Response findByStatus(@PathParam("status") String status) {
        try {
            List<Reservation> reservations = reservationDAO.findByStatus(status);
            return Response.ok(reservations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservations: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get reservations by date range
     * GET /api/reservations/daterange?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     */
    @GET
    @Path("daterange")
    public Response findByDateRange(@QueryParam("startDate") String startDate, 
                                    @QueryParam("endDate") String endDate) {
        try {
            if (startDate == null || endDate == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Start date and end date are required"))
                    .build();
            }
            
            List<Reservation> reservations = reservationDAO.findByDateRange(
                LocalDate.parse(startDate), 
                LocalDate.parse(endDate)
            );
            return Response.ok(reservations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving reservations: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get available rooms for date range
     * GET /api/reservations/available?roomTypeId=X&checkIn=YYYY-MM-DD&checkOut=YYYY-MM-DD
     */
    @GET
    @Path("available")
    public Response findAvailableRoomsByType(@QueryParam("roomTypeId") int roomTypeId,
                                            @QueryParam("checkIn") String checkIn,
                                            @QueryParam("checkOut") String checkOut) {
        try {
            if (roomTypeId <= 0 || checkIn == null || checkOut == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room type ID, check-in date, and check-out date are required"))
                    .build();
            }
            
            List<Integer> availableRooms = reservationDAO.findAvailableRoomsByType(
                roomTypeId,
                LocalDate.parse(checkIn),
                LocalDate.parse(checkOut)
            );
            return Response.ok(availableRooms).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving available rooms: " + e.getMessage()))
                .build();
        }
    }
}
