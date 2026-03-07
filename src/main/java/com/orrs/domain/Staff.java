package com.orrs.domain;

import java.time.LocalDateTime;

/**
 * Represents a staff member in the Ocean View Resort system.
 * Contains authentication credentials and staff role information.
 */
public class Staff {
    private int staffId;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private StaffRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public Staff() {
    }

    /**
     * Constructs a Staff with the provided details.
     *
     * @param username the staff member's username
     * @param passwordHash the hashed password
     * @param email the staff member's email address
     * @param fullName the staff member's full name
     * @param role the staff member's role
     */
    public Staff(String username, String passwordHash, String email, String fullName, StaffRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Enum for staff roles in the system
     */
    public enum StaffRole {
        ADMIN, STAFF, MANAGER
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
