package com.orrs.resources;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IBillDAO;
import com.orrs.domain.Bill;
import com.orrs.domain.Bill.BillStatus;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Resource for bill operations
 * Endpoints: GET, POST, PUT, DELETE /resources/bills
 * Uses BillDAO for database operations
 */
@Path("bills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillResource {
    
    private final IBillDAO billDAO = DAOFactory.getInstance().getBillDAO();
    
    /**
     * Create a new bill
     * POST /api/bills
     */
    @POST
    public Response createBill(Bill bill) {
        try {
            if (bill == null || bill.getReservationIdAsInt() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            if (bill.getSubtotal() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Subtotal is required"))
                    .build();
            }
            
            billDAO.create(bill);
            return Response.status(Response.Status.CREATED)
                .entity(bill)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get bill by ID
     * GET /api/bills/{id}
     */
    @GET
    @Path("{id}")
    public Response getBill(@PathParam("id") int id) {
        try {
            Bill bill = billDAO.readById(id);
            
            if (bill == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Bill not found"))
                    .build();
            }
            
            return Response.ok(bill).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all bills
     * GET /api/bills
     */
    @GET
    public Response getAllBills() {
        try {
            List<Bill> bills = billDAO.readAll();
            return Response.ok(bills).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving bills: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Update bill
     * PUT /api/bills/{id}
     */
    @PUT
    @Path("{id}")
    public Response updateBill(@PathParam("id") int id, Bill bill) {
        try {
            if (bill == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bill data is required"))
                    .build();
            }
            
            bill.setBillId(id);
            billDAO.update(bill);
            
            return Response.ok(new SuccessResponse("Bill updated successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error updating bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Delete bill
     * DELETE /api/bills/{id}
     */
    @DELETE
    @Path("{id}")
    public Response deleteBill(@PathParam("id") int id) {
        try {
            billDAO.delete(id);
            return Response.ok(new SuccessResponse("Bill deleted successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error deleting bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get bill by reservation ID
     * GET /api/bills/reservation/{reservationId}
     */
    @GET
    @Path("reservation/{reservationId}")
    public Response getBillByReservationId(@PathParam("reservationId") int reservationId) {
        try {
            Bill bill = billDAO.findByReservationId(reservationId);
            
            if (bill == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Bill not found for this reservation"))
                    .build();
            }
            
            return Response.ok(bill).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get all unpaid bills
     * GET /api/bills/unpaid
     */
    @GET
    @Path("unpaid")
    public Response getUnpaidBills() {
        try {
            List<Bill> unpaidBills = billDAO.findUnpaidBills();
            return Response.ok(unpaidBills).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving unpaid bills: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Mark bill as paid
     * PUT /api/bills/{id}/pay
     */
    @PUT
    @Path("{id}/pay")
    public Response payBill(@PathParam("id") int id) {
        try {
            Bill bill = billDAO.readById(id);
            
            if (bill == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Bill not found"))
                    .build();
            }
            
            bill.setPaymentStatus(BillStatus.PAID);
            bill.setPaymentDate(LocalDateTime.now());
            billDAO.update(bill);
            
            return Response.ok(new SuccessResponse("Bill marked as paid")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error processing payment: " + e.getMessage()))
                .build();
        }
    }
}
