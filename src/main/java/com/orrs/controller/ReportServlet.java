package com.orrs.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * ReportServlet handles report generation and display.
 * 
 * Provides:
 * - Occupancy reports
 * - Revenue reports
 * - Guest reports
 * - Payment reports
 * - Reservation status reports
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/report"})
public class ReportServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String type = request.getParameter("type");
        
        if (type == null || "menu".equals(type)) {
            // Display report selection menu
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
        } else if ("occupancy".equals(type)) {
            handleOccupancyReport(request, response);
        } else if ("revenue".equals(type)) {
            handleRevenueReport(request, response);
        } else if ("guest".equals(type)) {
            handleGuestReport(request, response);
        } else if ("payment".equals(type)) {
            handlePaymentReport(request, response);
        } else if ("status".equals(type)) {
            handleStatusReport(request, response);
        } else {
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
        }
    }
    
    private void handleOccupancyReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) 
                ? LocalDate.parse(startDateStr)
                : LocalDate.now().minusMonths(1);
            
            LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty())
                ? LocalDate.parse(endDateStr)
                : LocalDate.now();
            
            request.setAttribute("reportType", "Occupancy");
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error generating occupancy report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void handleRevenueReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String yearMonthStr = request.getParameter("yearMonth");
            
            YearMonth yearMonth = (yearMonthStr != null && !yearMonthStr.isEmpty())
                ? YearMonth.parse(yearMonthStr)
                : YearMonth.now().minusMonths(1);
            
            request.setAttribute("reportType", "Revenue");
            request.setAttribute("yearMonth", yearMonth);
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error generating revenue report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void handleGuestReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("reportType", "Guest");
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error generating guest report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void handlePaymentReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("reportType", "Payment");
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error generating payment report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void handleStatusReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("reportType", "Status");
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error generating status report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("staff") != null;
    }
}
