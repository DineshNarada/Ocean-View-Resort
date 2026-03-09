package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IGuestDAO;
import com.orrs.domain.Guest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for Guest CRUD operations
 * Endpoints: GET, POST, PUT, DELETE /resources/guests
 * Uses GuestDAO for database operations
 */
@Path("guests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GuestResource {
    
    private final IGuestDAO guestDAO = DAOFactory.getInstance().getGuestDAO();
    
    /**
     * Create a new guest
     * POST /api/guests
     */
    @POST
    public Response createGuest(Guest guest) {
        try {
            if (guest == null || guest.getName() == null || guest.getName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Guest name is required"))
                    .build();
            }
            
            guestDAO.create(guest);
            return Response.status(Response.Status.CREATED)
                .entity(guest)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating guest: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get guest by ID
     * GET /api/guests/{id}
     */
    @GET
    @Path("{id}")
    public Response getGuest(@PathParam("id") int id) {
        try {
            Guest guest = guestDAO.readById(id);
            
            if (guest == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Guest not found"))
                    .build();
            }
            
            return Response.ok(guest).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving guest: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all guests
     * GET /api/guests
     */
    @GET
    public Response getAllGuests() {
        try {
            List<Guest> guests = guestDAO.readAll();
            return Response.ok(guests).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving guests: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update guest
     * PUT /api/guests/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateGuest(@PathParam("id") int id, Guest guest) {
        try {
            if (guest == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Guest data is required"))
                    .build();
            }
            
            guest.setGuestId(id);
            guestDAO.update(guest);
            
            return Response.ok(new SuccessResponse("Guest updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating guest: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Delete guest
     * DELETE /api/guests/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteGuest(@PathParam("id") int id) {
        try {
            guestDAO.delete(id);
            return Response.ok(new SuccessResponse("Guest deleted successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error deleting guest: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Search guests by name
     * GET /api/guests/search/{name}
     */
    @GET
    @Path("search/{name}")
    public Response searchGuestsByName(@PathParam("name") String name) {
        try {
            List<Guest> guests = guestDAO.searchByName(name);
            return Response.ok(guests).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error searching guests: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Find guest by email
     * GET /api/guests/email/{email}
     */
    @GET
    @Path("email/{email}")
    public Response findGuestByEmail(@PathParam("email") String email) {
        try {
            Guest guest = guestDAO.findByEmail(email);
            
            if (guest == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Guest not found"))
                    .build();
            }
            
            return Response.ok(guest).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving guest: " + e.getMessage()))
                .build();
        }
    }
}