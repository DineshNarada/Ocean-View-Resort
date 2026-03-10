package com.orrs.controller;

import com.orrs.dao.DAOFactory;
import com.orrs.dao.IBillDAO;
import com.orrs.domain.Bill;
import com.orrs.domain.Reservation;
import com.orrs.manager.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BillingServlet handles billing and payment operations.
 * 
 * REFACTORED: Now implements dynamic, database-driven billing
 * - Retrieves bills from database via BillDAO
 * - Processes payments with database persistence
 * - No hardcoded sample data
 * 
 * DATA FLOW:
 * 1. Request to /billing?action=list
 * 2. BillDAO.readAll() queries database for all bills
 * 3. Bills are bound to bill.jsp for display
 * 4. On payment: BillDAO.findByReservationId() validates bill
 * 5. BillDAO.update() persists payment status to database
 * 
 * Processes:
 * - Display bills for reservations (from database)
 * - Calculate and retrieve bill amounts (from database)
 * - Process payments with persistence (update database)
 * - View payment history (from database records)
 */
@WebServlet(name = "BillingServlet", urlPatterns = {"/billing"})
public class BillingServlet extends HttpServlet {
    
    private ReservationManager reservationManager;
    private IBillDAO billDAO;  // REFACTORED: Use DAO for database access
    
    @Override
    public void init() throws ServletException {
        super.init();
        reservationManager = new ReservationManager();
        // REFACTORED: Inject BillDAO from factory
        billDAO = DAOFactory.getInstance().getBillDAO();
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
            // ============================================================
            // REFACTORED: Display all bills from database
            // ============================================================
            try {
                // DYNAMIC DATA RETRIEVAL: Get all bills from database
                List<Bill> bills = billDAO.readAll();
                
                // If no bills exist, display informative message
                if (bills.isEmpty()) {
                    request.setAttribute("message", "No bills found. Create reservations to generate bills.");
                }
                
                request.setAttribute("bills", bills);
                request.getRequestDispatcher("/bill.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error retrieving bills: " + e.getMessage());
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
        
        if ("process_payment".equals(action)) {
            handlePayment(request, response);
        }
    }
    
    /**
     * Handles payment processing with database persistence
     */
    private void handlePayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String billIdStr = request.getParameter("billId");
            String paymentAmountStr = request.getParameter("paymentAmount");
            
            // Validate input
            if (billIdStr == null || billIdStr.isEmpty()) {
                request.setAttribute("error", "Bill ID is required");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            if (paymentAmountStr == null || paymentAmountStr.isEmpty()) {
                request.setAttribute("error", "Payment amount is required");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            int billId = Integer.parseInt(billIdStr);
            double paymentAmount = Double.parseDouble(paymentAmountStr);
            
            if (paymentAmount <= 0) {
                request.setAttribute("error", "Payment amount must be greater than 0");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            // ============================================================
            // REFACTORED: Retrieve bill from database and validate
            // ============================================================
            try {
                // DYNAMIC DATA RETRIEVAL: Get bill from database
                Bill bill = billDAO.readById(billId);
                
                if (bill == null) {
                    request.setAttribute("error", "Bill not found");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
                
                // Validate payment amount
                double billAmount = bill.getTotalAmount() != null ? 
                                  bill.getTotalAmount().doubleValue() : 0.0;
                
                if (paymentAmount > billAmount) {
                    request.setAttribute("error", "Payment amount cannot exceed bill amount of $" + 
                                       String.format("%.2f", billAmount));
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
                
                // ============================================================
                // REFACTORED: Update bill status in database
                // ============================================================
                if (paymentAmount >= billAmount) {
                    // Full payment - mark as paid (using Bill's inner enum)
                    bill.setPaymentStatus(Bill.BillStatus.PAID);
                    bill.setPaymentDate(java.time.LocalDateTime.now());
                } else {
                    // Partial payment - keep as pending but record payment (using Bill's inner enum)
                    bill.setPaymentStatus(Bill.BillStatus.PARTIAL);
                    bill.setPaymentDate(java.time.LocalDateTime.now());
                }
                
                // Persist updated bill to database
                billDAO.update(bill);
                
                request.setAttribute("success", "Payment of $" + String.format("%.2f", paymentAmount) + 
                                   " processed successfully!");
                
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid bill ID format");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/billing?action=list");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format. Please enter a valid number.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error processing payment: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("staff") != null;
    }
}
