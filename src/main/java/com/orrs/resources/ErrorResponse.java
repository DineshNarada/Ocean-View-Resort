package com.orrs.resources;

/**
 * Standard error response for REST API
 */
public class ErrorResponse {
    private String error;
    private long timestamp;
    
    public ErrorResponse() {}
    
    public ErrorResponse(String error) {
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getError() { 
        return error; 
    }
    
    public void setError(String error) { 
        this.error = error; 
    }
    
    public long getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(long timestamp) { 
        this.timestamp = timestamp; 
    }
}