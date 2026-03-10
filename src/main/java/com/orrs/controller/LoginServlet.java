package com.orrs.controller;

import com.orrs.domain.Staff;
import com.orrs.manager.AuthenticationManager;
import com.orrs.manager.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet handles user authentication and session creation.
 * 
 * REFACTORED: Now implements dynamic, database-driven authentication
 * - Validates credentials against Staff table in database
 * - Retrieves full Staff object with ID and role information
 * - Creates session with authenticated staff from database
 * 
 * DATA FLOW:
 * 1. Request arrives with username/password (from login.jsp)
 * 2. AuthenticationManager queries StaffDAO for staff by username
 * 3. StaffDAO retrieves Staff from database with all properties
 * 4. Password hash is verified against database hash
 * 5. On success: Full Staff object stored in session
 * 
 * SEPARATION OF CONCERNS:
 * - LoginServlet: HTTP request/response handling
 * - AuthenticationManager: Authentication logic
 * - StaffDAO: Database queries
 * - Staff domain: Data model
 * 
 * Routes authenticated users to the dashboard.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private AuthenticationManager authManager;
    private SessionManager sessionManager;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authManager = new AuthenticationManager();
        sessionManager = SessionManager.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if already logged in
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null && httpSession.getAttribute("staff") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Display login form
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // ============================================================
            // REFACTORED: Dynamic Database-Driven Authentication
            // ============================================================
            // Authenticate user against database via AuthenticationManager
            // Returns Staff object with ID and full details from database
            Staff authenticatedStaff = authManager.authenticate(username, password);
            
            if (authenticatedStaff != null) {
                // ============================================================
                // SUCCESS: User authenticated - Create session with db staff
                // ============================================================
                // Create servlet session with authenticated Staff from database
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute("staff", authenticatedStaff);
                httpSession.setAttribute("username", authenticatedStaff.getUsername());
                httpSession.setAttribute("staffId", authenticatedStaff.getStaffId());
                httpSession.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Create application session with database staff
                String sessionToken = sessionManager.createSession(authenticatedStaff);
                httpSession.setAttribute("sessionToken", sessionToken);
                
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // ============================================================
                // FAILURE: Invalid credentials (not found or wrong password)
                // ============================================================
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred during login: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
