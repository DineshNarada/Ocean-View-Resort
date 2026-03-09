package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IRoomTypeDAO;
import com.orrs.domain.RoomType;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for RoomType CRUD operations
 * Endpoints: GET, POST, PUT, DELETE /resources/roomtypes
 * Uses RoomTypeDAO for database operations
 */
@Path("roomtypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomTypeResource {
    
    private final IRoomTypeDAO roomTypeDAO = DAOFactory.getInstance().getRoomTypeDAO();
    
    /**
     * Create a new room type
     * POST /api/roomtypes
     */
    @POST
    public Response createRoomType(RoomType roomType) {
        try {
            if (roomType == null || roomType.getTypeName() == null || roomType.getTypeName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room type name is required"))
                    .build();
            }
            
            if (roomType.getPricePerNight() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Price per night is required"))
                    .build();
            }
            
            roomTypeDAO.create(roomType);
            return Response.status(Response.Status.CREATED)
                .entity(roomType)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating room type: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get room type by ID
     * GET /api/roomtypes/{id}
     */
    @GET
    @Path("{id}")
    public Response getRoomType(@PathParam("id") int id) {
        try {
            RoomType roomType = roomTypeDAO.readById(id);
            
            if (roomType == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Room type not found"))
                    .build();
            }
            
            return Response.ok(roomType).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving room type: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all room types
     * GET /api/roomtypes
     */
    @GET
    public Response getAllRoomTypes() {
        try {
            List<RoomType> roomTypes = roomTypeDAO.readAll();
            return Response.ok(roomTypes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving room types: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update room type
     * PUT /api/roomtypes/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateRoomType(@PathParam("id") int id, RoomType roomType) {
        try {
            if (roomType == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room type data is required"))
                    .build();
            }
            
            roomType.setRoomTypeId(id);
            roomTypeDAO.update(roomType);
            
            return Response.ok(new SuccessResponse("Room type updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating room type: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Delete room type
     * DELETE /api/roomtypes/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteRoomType(@PathParam("id") int id) {
        try {
            roomTypeDAO.delete(id);
            return Response.ok(new SuccessResponse("Room type deleted successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error deleting room type: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Find room type by name
     * GET /api/roomtypes/name/{name}
     */
    @GET
    @Path("name/{name}")
    public Response findByTypeName(@PathParam("name") String name) {
        try {
            RoomType roomType = roomTypeDAO.findByTypeName(name);
            
            if (roomType == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Room type not found"))
                    .build();
            }
            
            return Response.ok(roomType).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving room type: " + e.getMessage()))
                .build();
        }
    }
}