package com.orrs.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a bill generated for a reservation.
 * Responsible for tracking costs, taxes, discounts, and payment status.
 */
public class Bill {
    private int billId;
    private int reservationId;
    private String reservationIdStr;  // For test support
    private int numberOfNights;  // For test support
    private double ratePerNight;  // For test support
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal totalAmount;  // Generated column: subtotal + tax - discount
    private BillStatus paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public Bill() {
        this.paymentStatus = BillStatus.PENDING;
    }

    /**
     * Constructs a Bill for a reservation.
     *
     * @param reservationId the ID of the associated reservation
     * @param subtotal the subtotal amount
     * @param tax the tax amount
     * @param discount the discount amount
     */
    public Bill(int reservationId, BigDecimal subtotal, BigDecimal tax, BigDecimal discount) {
        this.reservationId = reservationId;
        this.subtotal = subtotal;
        this.tax = tax != null ? tax : BigDecimal.ZERO;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.paymentStatus = BillStatus.PENDING;
        calculateTotalAmount();
    }

    /**
     * Constructs a Bill with reservation ID (String), number of nights, and rate per night.
     * Used for simplified test scenarios.
     *
     * @param reservationId the string ID of the associated reservation
     * @param numberOfNights the number of nights for the reservation
     * @param ratePerNight the rate per night
     */
    public Bill(String reservationId, int numberOfNights, double ratePerNight) {
        this.reservationIdStr = reservationId;
        this.numberOfNights = numberOfNights;
        this.ratePerNight = ratePerNight;
        this.paymentStatus = BillStatus.PENDING;
        this.subtotal = BigDecimal.valueOf(numberOfNights * ratePerNight);
        this.tax = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.totalAmount = this.subtotal;
    }

    /**
     * Calculates total amount as subtotal + tax - discount
     */
    private void calculateTotalAmount() {
        if (subtotal != null) {
            this.totalAmount = subtotal.add(tax != null ? tax : BigDecimal.ZERO)
                    .subtract(discount != null ? discount : BigDecimal.ZERO);
        }
    }

    /**
     * Prints the bill details to the console.
     */
    public void print() {
        System.out.println("========== BILL ==========");
        System.out.println("Bill ID: " + billId);
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Subtotal: Rs. " + String.format("%.2f", subtotal));
        System.out.println("Tax: Rs. " + String.format("%.2f", tax));
        System.out.println("Discount: Rs. " + String.format("%.2f", discount));
        System.out.println("Total Amount: Rs. " + String.format("%.2f", totalAmount));
        System.out.println("Payment Status: " + paymentStatus);
        System.out.println("==========================");
    }

    // Getters
    public int getBillId() {
        return billId;
    }

    public Object getReservationId() {
        if (reservationIdStr != null) {
            return reservationIdStr;
        }
        return reservationId;
    }

    public String getReservationIdStr() {
        return reservationIdStr;
    }

    public int getReservationIdAsInt() {
        return reservationId;
    }

    public double getAmount() {
        if (totalAmount != null) {
            return totalAmount.doubleValue();
        }
        return numberOfNights * ratePerNight;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getRatePerNight() {
        return ratePerNight;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BillStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        calculateTotalAmount();
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax != null ? tax : BigDecimal.ZERO;
        calculateTotalAmount();
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        calculateTotalAmount();
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentStatus(BillStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentStatus(String statusStr) {
        this.paymentStatus = BillStatus.valueOf(statusStr);
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Marks the bill as paid and sets the payment date to current time.
     */
    public void markAsPaid() {
        this.paymentStatus = BillStatus.PAID;
        this.paymentDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (reservationIdStr != null) {
            return "Bill{reservationId='" + reservationIdStr + "', amount=" + getAmount() + ", numberOfNights=" + numberOfNights + ", ratePerNight=" + ratePerNight + "}";
        }
        return "Bill{" +
                "billId=" + billId +
                ", reservationId=" + reservationId +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", paymentStatus=" + paymentStatus +
                ", paymentDate=" + paymentDate +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Enumeration for bill payment status
     */
    public enum BillStatus {
        PENDING, PAID, PARTIAL
    }
}
