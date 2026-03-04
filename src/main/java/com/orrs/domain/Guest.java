package com.orrs.domain;

/**
 * Represents a guest in the Ocean View Resort system.
 * Contains personal details for the occupant of a reservation.
 */
public class Guest {
    private String name;
    private String address;
    private String contact;

    /**
     * Constructs a Guest with the provided details.
     *
     * @param name the guest's full name
     * @param address the guest's address
     * @param contact the guest's contact number
     */
    public Guest(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
