package com.orrs.manager;

import com.orrs.domain.Staff;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SessionManager implements the Singleton design pattern.
 * Manages user sessions for the Ocean View Resort application.
 * 
 * This class is responsible for:
 * - Creating and managing user sessions
 * - Tracking session timeouts
 * - Managing session data
 * - Providing session validation
 * 
 * Thread-safe singleton implementation using eager initialization.
 */
public class SessionManager {
    
    private static final SessionManager INSTANCE = new SessionManager();
    private static final long SESSION_TIMEOUT_MINUTES = 30;
    
    private Map<String, Session> activeSessions;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private SessionManager() {
        this.activeSessions = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of SessionManager.
     * 
     * @return the unique SessionManager instance
     */
    public static SessionManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Creates a new session for a staff member.
     * 
     * @param staff the authenticated staff member
     * @return a session token (ID)
     */
    public String createSession(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, staff, SESSION_TIMEOUT_MINUTES);
        activeSessions.put(sessionId, session);
        
        return sessionId;
    }
    
    /**
     * Validates if a session is still active or expired.
     * 
     * @param sessionId the session ID to validate
     * @return true if session is valid, false if expired or not found
     */
    public boolean isSessionValid(String sessionId) {
        Session session = activeSessions.get(sessionId);
        if (session == null) {
            return false;
        }
        
        if (session.isExpired()) {
            activeSessions.remove(sessionId);
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the staff member associated with a session.
     * 
     * @param sessionId the session ID
     * @return the Staff object, or null if session not found/expired
     */
    public Staff getStaff(String sessionId) {
        if (!isSessionValid(sessionId)) {
            return null;
        }
        
        Session session = activeSessions.get(sessionId);
        session.updateLastActivity(); // Extend session timeout
        return session.getStaff();
    }
    
    /**
     * Invalidates (closes) a session.
     * 
     * @param sessionId the session ID to close
     * @return true if session was closed, false if not found
     */
    public boolean invalidateSession(String sessionId) {
        return activeSessions.remove(sessionId) != null;
    }
    
    /**
     * Invalidates all active sessions (e.g., for system maintenance).
     */
    public void invalidateAllSessions() {
        activeSessions.clear();
    }
    
    /**
     * Gets the number of active sessions.
     * 
     * @return the count of active sessions
     */
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
    
    /**
     * Cleans up expired sessions.
     */
    public void cleanupExpiredSessions() {
        activeSessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    /**
     * Inner class representing a user session.
     */
    private static class Session {
        private Staff staff;
        private LocalDateTime createdAt;
        private LocalDateTime lastActivityAt;
        private long timeoutMinutes;
        
        public Session(String sessionId, Staff staff, long timeoutMinutes) {
            this.staff = staff;
            this.timeoutMinutes = timeoutMinutes;
            this.createdAt = LocalDateTime.now();
            this.lastActivityAt = this.createdAt;
        }
        
        public boolean isExpired() {
            LocalDateTime expirationTime = lastActivityAt.plusMinutes(timeoutMinutes);
            return LocalDateTime.now().isAfter(expirationTime);
        }
        
        public void updateLastActivity() {
            this.lastActivityAt = LocalDateTime.now();
        }
        
        public Staff getStaff() {
            return staff;
        }
    }
}
