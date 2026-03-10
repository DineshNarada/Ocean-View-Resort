package com.orrs.resources;

import com.orrs.domain.Staff;
import com.orrs.manager.AuthenticationManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Resource for authentication (login/logout)
 * Endpoints: POST /resources/auth/login, POST /resources/auth/logout
 * 
 * REFACTORED: Now uses database-driven authentication.
 * AuthenticationManager.authenticate() returns Staff object or null
 * instead of boolean.
 */
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    private static AuthenticationManager authManager = new AuthenticationManager();
    private static final String SESSION_HEADER = "X-Session-ID";
    
    /**
     * Login endpoint
     * POST /api/auth/login
     * 
     * REFACTORED: Now queries database for staff authentication
     * 
     * @param credentials JSON with username and password
     * @return Response with session token if successful
     */
    @POST
    @Path("login")
    public Response login(LoginRequest credentials) {
        try {
            if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("username and password are required"))
                    .build();
            }
            
            // REFACTORED: Authenticate against database via AuthenticationManager
            // Returns Staff object if successful, null otherwise
            Staff authenticatedStaff = authManager.authenticate(
                credentials.getUsername(), 
                credentials.getPassword()
            );
            
            if (authenticatedStaff != null) {
                // Authentication successful - create session with full staff details
                String sessionId = generateSessionId();
                LoginResponse response = new LoginResponse(
                    "Authentication successful",
                    sessionId,
                    authenticatedStaff.getUsername(),
                    authenticatedStaff.getStaffId(),
                    authenticatedStaff.getFullName(),
                    authenticatedStaff.getRole() != null ? authenticatedStaff.getRole().toString() : "STAFF"
                );
                return Response.ok(response).build();
            } else {
                // Authentication failed - invalid credentials or inactive user
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Invalid credentials or account is inactive"))
                    .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Internal server error: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Logout endpoint
     * POST /api/auth/logout
     * 
     * REFACTORED: Now part of database-driven authentication system
     * 
     * @param sessionId Session ID from X-Session-ID header
     * @return Response confirming logout
     */
    @POST
    @Path("logout")
    public Response logout(@HeaderParam(SESSION_HEADER) String sessionId) {
        try {
            if (sessionId == null || sessionId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Session ID is required"))
                    .build();
            }
            
            // Invalidate session (in production, remove from session manager)
            // This complements the database-driven authentication by clearing
            // any session tokens or cached authentication state
            return Response.ok(new SuccessResponse("Logged out successfully"))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Internal server error: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Generate a unique session ID
     */
    private String generateSessionId() {
        return "SESSION_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    // ==================== Inner Classes ====================
    
    /**
     * Login request DTO
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    /**
     * Login response DTO
     * REFACTORED: Now includes full staff details from database
     */
    public static class LoginResponse {
        private String message;
        private String sessionId;
        private String username;
        private int staffId;
        private String fullName;
        private String role;
        
        public LoginResponse() {}
        
        public LoginResponse(String message, String sessionId, String username) {
            this.message = message;
            this.sessionId = sessionId;
            this.username = username;
        }
        
        // Constructor with full staff details
        public LoginResponse(String message, String sessionId, String username, 
                           int staffId, String fullName, String role) {
            this.message = message;
            this.sessionId = sessionId;
            this.username = username;
            this.staffId = staffId;
            this.fullName = fullName;
            this.role = role;
        }
        
        public String getMessage() { return message; }
        public String getSessionId() { return sessionId; }
        public String getUsername() { return username; }
        public int getStaffId() { return staffId; }
        public String getFullName() { return fullName; }
        public String getRole() { return role; }
    }
    
    /**
     * Success response DTO
     */
    public static class SuccessResponse {
        private String message;
        
        public SuccessResponse() {}
        public SuccessResponse(String message) { this.message = message; }
        
        public String getMessage() { return message; }
    }
    
    /**
     * Error response DTO
     */
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
