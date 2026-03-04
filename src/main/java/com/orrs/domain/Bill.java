package com.orrs.domain;

/**
 * Represents a bill generated for a reservation.
 * Responsible for calculating and storing the total cost of a stay.
 */
public class Bill {
    private String reservationId;
    private double amount;
    private int numberOfNights;
    private double ratePerNight;

    /**
     * Constructs a Bill for a reservation.
     *
     * @param reservationId the ID of the associated reservation
     * @param numberOfNights the number of nights stayed
     * @param ratePerNight the cost per night
     */
    public Bill(String reservationId, int numberOfNights, double ratePerNight) {
        this.reservationId = reservationId;
        this.numberOfNights = numberOfNights;
        this.ratePerNight = ratePerNight;
        this.amount = numberOfNights * ratePerNight;
    }

    /**
     * Prints the bill details to the console.
     */
    public void print() {
        System.out.println("========== BILL ==========");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Number of Nights: " + numberOfNights);
        System.out.println("Rate per Night: Rs. " + String.format("%.2f", ratePerNight));
        System.out.println("Total Amount: Rs. " + String.format("%.2f", amount));
        System.out.println("==========================");
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getRatePerNight() {
        return ratePerNight;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "reservationId='" + reservationId + '\'' +
                ", amount=" + amount +
                ", numberOfNights=" + numberOfNights +
                ", ratePerNight=" + ratePerNight +
                '}';
    }
}
