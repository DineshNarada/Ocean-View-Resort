package com.orrs.help;

/**
 * Provides help and guidelines for staff members using the Ocean View Resort system.
 * Displays system usage instructions and feature descriptions.
 */
public class HelpSystem {

    /**
     * Displays the help documentation for the reservation system.
     */
    public static void displayHelp() {
        System.out.println("\n========== OCEAN VIEW RESORT - HELP SECTION ==========");
        System.out.println();
        System.out.println("SYSTEM OVERVIEW:");
        System.out.println("The Ocean View Resort Reservation System manages guest bookings and reservations");
        System.out.println("for our beachside hotel in Galle. This guide will help you navigate the system.");
        System.out.println();

        System.out.println("MAIN MENU OPTIONS:");
        System.out.println("1. Login");
        System.out.println("   - Secure access to the system using your staff credentials");
        System.out.println("   - Username: admin | Password: password123 (for demo)");
        System.out.println();

        System.out.println("2. Add New Reservation");
        System.out.println("   - Create a new room reservation for a guest");
        System.out.println("   - Required information:");
        System.out.println("     * Guest Name");
        System.out.println("     * Guest Address");
        System.out.println("     * Contact Number");
        System.out.println("     * Room Type (Single, Double, Suite)");
        System.out.println("     * Check-in Date (YYYY-MM-DD)");
        System.out.println("     * Check-out Date (YYYY-MM-DD)");
        System.out.println("   - A unique Reservation Number will be automatically generated");
        System.out.println();

        System.out.println("3. Display Reservation Details");
        System.out.println("   - View complete booking information for a specific reservation");
        System.out.println("   - Enter the Reservation Number to retrieve details");
        System.out.println("   - Information displayed includes:");
        System.out.println("     * Reservation Number");
        System.out.println("     * Guest Details");
        System.out.println("     * Room Type");
        System.out.println("     * Check-in and Check-out Dates");
        System.out.println("     * Duration of Stay");
        System.out.println();

        System.out.println("4. Calculate and Print Bill");
        System.out.println("   - Compute the total stay cost for a reservation");
        System.out.println("   - Bill calculation: Duration (nights) × Rate per Night");
        System.out.println("   - Select a reservation to generate and print the bill");
        System.out.println();

        System.out.println("5. View Help");
        System.out.println("   - Display this help section with system guidelines");
        System.out.println();

        System.out.println("6. Exit System");
        System.out.println("   - Safely close the application");
        System.out.println("   - All data saved to the system");
        System.out.println();

        System.out.println("ROOM RATES (Nightly):");
        System.out.println("   * Single Room: Rs. 5,000");
        System.out.println("   * Double Room: Rs. 7,500");
        System.out.println("   * Suite: Rs. 12,000");
        System.out.println();

        System.out.println("TIPS FOR STAFF:");
        System.out.println("- Always ensure guest details are entered correctly");
        System.out.println("- Check-out date must be after check-in date");
        System.out.println("- Reservation IDs are auto-generated and unique");
        System.out.println("- Keep guest information confidential");
        System.out.println("- Contact the supervisor if you encounter issues");
        System.out.println();
        System.out.println("======================================================\n");
    }

    /**
     * Displays quick tips for new staff members.
     */
    public static void displayQuickTips() {
        System.out.println("\n--- QUICK TIPS ---");
        System.out.println("1. Verify guest identity before confirming reservations");
        System.out.println("2. Double-check dates to avoid booking conflicts");
        System.out.println("3. Offer special room discounts if applicable");
        System.out.println("4. Welcome guests warmly and professionally");
        System.out.println("-----------------\n");
    }
}
