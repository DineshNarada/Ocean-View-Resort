package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IStaffDAO;
import com.orrs.domain.Staff;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for Staff CRUD operations
 * Endpoints: GET, POST, PUT, DELETE /resources/staff
 * Uses StaffDAO for database operations
 */
@Path("staff")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StaffResource {
    
    private final IStaffDAO staffDAO = DAOFactory.getInstance().getStaffDAO();
    
    /**
     * Create a new staff member
     * POST /api/staff
     */
    @POST
    public Response createStaff(Staff staff) {
        try {
            if (staff == null || staff.getUsername() == null || staff.getUsername().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Username is required"))
                    .build();
            }
            
            if (staff.getPasswordHash() == null || staff.getPasswordHash().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Password is required"))
                    .build();
            }
            
            if (staff.getEmail() == null || staff.getEmail().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Email is required"))
                    .build();
            }
            
            staffDAO.create(staff);
            return Response.status(Response.Status.CREATED)
                .entity(staff)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating staff member: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get staff member by ID
     * GET /api/staff/{id}
     */
    @GET
    @Path("{id}")
    public Response getStaff(@PathParam("id") int id) {
        try {
            Staff staff = staffDAO.readById(id);
            
            if (staff == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Staff member not found"))
                    .build();
            }
            
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving staff member: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all staff members
     * GET /api/staff
     */
    @GET
    public Response getAllStaff() {
        try {
            List<Staff> staff = staffDAO.readAll();
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving staff members: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update staff member
     * PUT /api/staff/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateStaff(@PathParam("id") int id, Staff staff) {
        try {
            if (staff == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Staff data is required"))
                    .build();
            }
            
            staff.setStaffId(id);
            staffDAO.update(staff);
            
            return Response.ok(new SuccessResponse("Staff member updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating staff member: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Delete staff member
     * DELETE /api/staff/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteStaff(@PathParam("id") int id) {
        try {
            staffDAO.delete(id);
            return Response.ok(new SuccessResponse("Staff member deleted successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error deleting staff member: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Find staff by username
     * GET /api/staff/username/{username}
     */
    @GET
    @Path("username/{username}")
    public Response findByUsername(@PathParam("username") String username) {
        try {
            Staff staff = staffDAO.findByUsername(username);
            
            if (staff == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Staff member not found"))
                    .build();
            }
            
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving staff member: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all active staff members
     * GET /api/staff/active
     */
    @GET
    @Path("active")
    public Response getActiveStaff() {
        try {
            List<Staff> staff = staffDAO.findAllActive();
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving active staff: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get staff by role
     * GET /api/staff/role/{role}
     */
    @GET
    @Path("role/{role}")
    public Response getStaffByRole(@PathParam("role") String role) {
        try {
            List<Staff> staff = staffDAO.findByRole(role);
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving staff by role: " + e.getMessage()))
                .build();
        }
    }
}