package com.orrs.resources;

import com.orrs.domain.Bill;
import com.orrs.manager.ReservationManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * REST Resource for bill operations
 * Endpoints: GET, POST /resources/bills
 */
@Path("bills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillResource {
    
    private static ReservationManager reservationManager = new ReservationManager();
    
    /**
     * Get bill for a specific reservation
     * GET /api/bills/{reservationId}
     */
    @GET
    @Path("{reservationId}")
    public Response getBill(@PathParam("reservationId") String reservationId) {
        try {
            if (reservationId == null || reservationId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            Optional<Bill> bill = reservationManager.findBill(reservationId);
            
            if (bill.isPresent()) {
                return Response.ok(new BillResponse(bill.get()))
                    .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Bill not found for this reservation"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error retrieving bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Create a bill for a reservation
     * POST /api/bills
     */
    @POST
    public Response createBill(CreateBillRequest request) {
        try {
            if (request == null || !isValidRequest(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid bill data"))
                    .build();
            }
            
            Bill bill = new Bill(
                Integer.parseInt(request.getReservationId()),
                new BigDecimal(request.getSubtotal()),
                new BigDecimal(request.getTax() != null ? request.getTax() : "0"),
                new BigDecimal(request.getDiscount() != null ? request.getDiscount() : "0")
            );
            
            reservationManager.addBill(bill);
            
            return Response.status(Response.Status.CREATED)
                .entity(new BillResponse(bill))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error creating bill: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Mark bill as paid
     * PUT /api/bills/{reservationId}/pay
     */
    @PUT
    @Path("{reservationId}/pay")
    public Response payBill(@PathParam("reservationId") String reservationId, PaymentRequest paymentRequest) {
        try {
            if (reservationId == null || reservationId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Reservation ID is required"))
                    .build();
            }
            
            Optional<Bill> bill = reservationManager.findBill(reservationId);
            
            if (!bill.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Bill not found"))
                    .build();
            }
            
            Bill existingBill = bill.get();
            
            if (paymentRequest.getAmountPaid() != null) {
                BigDecimal amount = new BigDecimal(paymentRequest.getAmountPaid());
                if (amount.compareTo(existingBill.getTotalAmount()) < 0) {
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("Payment amount is less than bill amount"))
                        .build();
                }
            }
            
            existingBill.markAsPaid();
            
            return Response.ok(new BillResponse(existingBill))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error processing payment: " + e.getMessage()))
                .build();
        }
    }
    
    private boolean isValidRequest(CreateBillRequest request) {
        return request.getReservationId() != null && !request.getReservationId().isEmpty()
            && request.getSubtotal() != null && !request.getSubtotal().isEmpty();
    }
    
    // ==================== DTOs ====================
    
    public static class CreateBillRequest {
        private String reservationId;
        private String subtotal;
        private String tax;
        private String discount;
        
        public CreateBillRequest() {}
        
        public String getReservationId() { return reservationId; }
        public void setReservationId(String reservationId) { this.reservationId = reservationId; }
        
        public String getSubtotal() { return subtotal; }
        public void setSubtotal(String subtotal) { this.subtotal = subtotal; }
        
        public String getTax() { return tax; }
        public void setTax(String tax) { this.tax = tax; }
        
        public String getDiscount() { return discount; }
        public void setDiscount(String discount) { this.discount = discount; }
    }
    
    public static class PaymentRequest {
        private String amountPaid;
        private String paymentMethod;
        
        public PaymentRequest() {}
        
        public String getAmountPaid() { return amountPaid; }
        public void setAmountPaid(String amountPaid) { this.amountPaid = amountPaid; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }
    
    public static class BillResponse {
        private int billId;
        private String reservationId;
        private String subtotal;
        private String tax;
        private String discount;
        private String totalAmount;
        private String paymentStatus;
        
        public BillResponse() {}
        
        public BillResponse(Bill bill) {
            this.billId = bill.getBillId();
            this.reservationId = bill.getReservationIdStr() != null ? bill.getReservationIdStr() : String.valueOf(bill.getReservationId());
            this.subtotal = bill.getSubtotal() != null ? bill.getSubtotal().toString() : "0";
            this.tax = bill.getTax() != null ? bill.getTax().toString() : "0";
            this.discount = bill.getDiscount() != null ? bill.getDiscount().toString() : "0";
            this.totalAmount = bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : "0";
            this.paymentStatus = bill.getPaymentStatus() != null ? bill.getPaymentStatus().toString() : "PENDING";
        }
        
        public int getBillId() { return billId; }
        public String getReservationId() { return reservationId; }
        public String getSubtotal() { return subtotal; }
        public String getTax() { return tax; }
        public String getDiscount() { return discount; }
        public String getTotalAmount() { return totalAmount; }
        public String getPaymentStatus() { return paymentStatus; }
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
