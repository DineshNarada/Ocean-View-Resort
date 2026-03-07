package com.orrs.domain;

import java.time.LocalDateTime;

/**
 * Represents a guest in the Ocean View Resort system.
 * Contains personal details for the occupant of a reservation.
 */
public class Guest {
    private int guestId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public Guest() {
    }

    /**
     * Constructs a Guest with ID and basic details.
     *
     * @param guestId the guest's ID
     * @param name the guest's full name
     * @param email the guest's email address
     * @param phone the guest's phone number
     */
    public Guest(int guestId, String name, String email, String phone) {
        this.guestId = guestId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructs a Guest with the provided details.
     *
     * @param name the guest's full name
     * @param email the guest's email address
     * @param phone the guest's phone number
     * @param address the guest's address
     * @param city the guest's city
     * @param country the guest's country
     */
    public Guest(String name, String email, String phone, String address, String city, String country) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    /**
     * Constructs a Guest with simplified details for testing.
     *
     * @param name the guest's full name
     * @param address the guest's address
     * @param contact the guest's contact (phone number)
     */
    public Guest(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.phone = contact;  // Use phone field for contact
    }

    // Getters
    public int getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getContact() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setContact(String contact) {
        this.phone = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Guest{name='" + name + "', address='" + address + "', contact='" + phone + "'}";
    }

    public String toStringDetailed() {
        return "Guest{" +
                "guestId=" + guestId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
