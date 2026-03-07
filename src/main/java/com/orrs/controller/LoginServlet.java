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
 * Processes login requests, validates credentials, and creates sessions.
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
            // Authenticate user
            boolean authenticated = authManager.authenticate(username, password);
            
            if (authenticated) {
                // Create staff object
                Staff staff = new Staff();
                staff.setUsername(username);
                staff.setFullName("Staff Member - " + username);
                
                // Create servlet session
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute("staff", staff);
                httpSession.setAttribute("username", username);
                httpSession.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Create application session
                String sessionToken = sessionManager.createSession(staff);
                httpSession.setAttribute("sessionToken", sessionToken);
                
                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Authentication failed
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred during login");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
