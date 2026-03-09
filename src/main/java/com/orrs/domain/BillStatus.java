package com.orrs.domain;

/**
 * Enum representing the payment status of a bill
 */
public enum BillStatus {
    PENDING("Pending"),
    PAID("Paid");

    private final String displayName;

    BillStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
