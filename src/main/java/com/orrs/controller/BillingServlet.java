package com.orrs.controller;

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
 * Processes:
 * - Display bills for reservations
 * - Calculate bill amounts
 * - View payment history
 */
@WebServlet(name = "BillingServlet", urlPatterns = {"/billing"})
public class BillingServlet extends HttpServlet {
    
    private ReservationManager reservationManager;
    private List<Bill> sampleBills;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reservationManager = new ReservationManager();
        initializeSampleBills();
    }
    
    private void initializeSampleBills() {
        sampleBills = new ArrayList<>();
        // Initialize with sample bills for demonstration
        Bill bill1 = new Bill("RES1001", 3, 100.0);
        Bill bill2 = new Bill("RES1002", 3, 150.0);
        
        sampleBills.add(bill1);
        sampleBills.add(bill2);
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
            // Display all bills
            try {
                // Generate bills from all reservations
                List<Bill> bills = new ArrayList<>();
                for (Reservation res : reservationManager.getAllReservations()) {
                    Bill bill = reservationManager.calculateBill(res.getId());
                    if (bill != null) {
                        bills.add(bill);
                    }
                }
                
                // Add sample bills if no real bills exist
                if (bills.isEmpty()) {
                    bills.addAll(sampleBills);
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
    
    private void handlePayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String reservationIdStr = request.getParameter("billId");
            String paymentAmountStr = request.getParameter("paymentAmount");
            
            // Validate input
            if (reservationIdStr == null || reservationIdStr.isEmpty()) {
                request.setAttribute("error", "Bill ID is required");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            if (paymentAmountStr == null || paymentAmountStr.isEmpty()) {
                request.setAttribute("error", "Payment amount is required");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            double paymentAmount = Double.parseDouble(paymentAmountStr);
            
            if (paymentAmount <= 0) {
                request.setAttribute("error", "Payment amount must be greater than 0");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            // Find and validate bill
            boolean found = false;
            for (Bill bill : sampleBills) {
                if (bill.getReservationId().equals(reservationIdStr)) {
                    if (paymentAmount > bill.getAmount()) {
                        request.setAttribute("error", "Payment amount cannot exceed bill amount of $" + String.format("%.2f", bill.getAmount()));
                        request.getRequestDispatcher("/error.jsp").forward(request, response);
                        return;
                    }
                    
                    // Payment processed successfully
                    request.setAttribute("success", "Payment of $" + String.format("%.2f", paymentAmount) + " processed successfully!");
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                request.setAttribute("error", "Bill not found");
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
