package com.orrs.controller;

import com.orrs.manager.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LogoutServlet handles user logout and session termination.
 * 
 * Clears session data and invalidates user sessions.
 * Redirects users back to the login page.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    
    private SessionManager sessionManager;
    
    @Override
    public void init() throws ServletException {
        super.init();
        sessionManager = SessionManager.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession httpSession = request.getSession(false);
        
        if (httpSession != null) {
            // Get session token for application-level invalidation
            String sessionToken = (String) httpSession.getAttribute("sessionToken");
            if (sessionToken != null) {
                sessionManager.invalidateSession(sessionToken);
            }
            
            // Invalidate the servlet session
            httpSession.invalidate();
        }
        
        // Redirect to login
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
