package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IRoomDAO;
import com.orrs.domain.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for Room CRUD operations
 * Endpoints: GET, POST, PUT, DELETE /resources/rooms
 * Uses RoomDAO for database operations
 */
@Path("rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    private final IRoomDAO roomDAO = DAOFactory.getInstance().getRoomDAO();
    
    /**
     * Create a new room
     * POST /api/rooms
     */
    @POST
    public Response createRoom(Room room) {
        try {
            if (room == null || room.getRoomNumber() == null || room.getRoomNumber().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room number is required"))
                    .build();
            }
            
            if (room.getRoomTypeId() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Valid room type ID is required"))
                    .build();
            }
            
            roomDAO.create(room);
            return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating room: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get room by ID
     * GET /api/rooms/{id}
     */
    @GET
    @Path("{id}")
    public Response getRoom(@PathParam("id") int id) {
        try {
            Room room = roomDAO.readById(id);
            
            if (room == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Room not found"))
                    .build();
            }
            
            return Response.ok(room).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving room: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all rooms
     * GET /api/rooms
     */
    @GET
    public Response getAllRooms() {
        try {
            List<Room> rooms = roomDAO.readAll();
            return Response.ok(rooms).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving rooms: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update room
     * PUT /api/rooms/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateRoom(@PathParam("id") int id, Room room) {
        try {
            if (room == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room data is required"))
                    .build();
            }
            
            room.setRoomId(id);
            roomDAO.update(room);
            
            return Response.ok(new SuccessResponse("Room updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating room: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Delete room
     * DELETE /api/rooms/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteRoom(@PathParam("id") int id) {
        try {
            roomDAO.delete(id);
            return Response.ok(new SuccessResponse("Room deleted successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error deleting room: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get rooms by room type ID
     * GET /api/rooms/roomtype/{roomTypeId}
     */
    @GET
    @Path("roomtype/{roomTypeId}")
    public Response getRoomsByRoomType(@PathParam("roomTypeId") int roomTypeId) {
        try {
            List<Room> rooms = roomDAO.findByRoomTypeId(roomTypeId);
            return Response.ok(rooms).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving rooms: " + e.getMessage()))
                .build();
        }
    }
}