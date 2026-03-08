package com.orrs.resources;

import com.orrs.domain.Reservation;
import com.orrs.manager.ReservationManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Resource for report generation
 * Endpoints: POST /resources/reports/occupancy, POST /resources/reports/revenue
 */
@Path("reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {
    
    private static ReservationManager reservationManager = new ReservationManager();
    
    /**
     * Generate occupancy report
     * POST /api/reports/occupancy
     */
    @POST
    @Path("occupancy")
    public Response generateOccupancyReport(ReportRequest request) {
        try {
            if (request == null || !isValidDateRange(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid date range"))
                    .build();
            }
            
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            
            List<Reservation> reservations = reservationManager.getAllReservations()
                .stream()
                .filter(res -> isOverlapping(res, startDate, endDate))
                .collect(Collectors.toList());
            
            long daysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            int totalRoomDays = (int) (daysInRange * 50); // Assuming 50 rooms available
            int bookedRoomDays = (int) reservations.stream()
                .mapToLong(res -> ChronoUnit.DAYS.between(res.getCheckIn(), res.getCheckOut()) + 1)
                .sum();
            
            double occupancyPercentage = (totalRoomDays > 0) ? (double) bookedRoomDays / totalRoomDays * 100 : 0;
            
            OccupancyReport report = new OccupancyReport(
                startDate.toString(),
                endDate.toString(),
                50, // Total rooms
                bookedRoomDays,
                totalRoomDays,
                String.format("%.2f", occupancyPercentage) + "%",
                reservations.size()
            );
            
            return Response.ok(report).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error generating occupancy report: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Generate revenue report
     * POST /api/reports/revenue
     */
    @POST
    @Path("revenue")
    public Response generateRevenueReport(ReportRequest request) {
        try {
            if (request == null || !isValidDateRange(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid date range"))
                    .build();
            }
            
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            
            List<Reservation> reservations = reservationManager.getAllReservations()
                .stream()
                .filter(res -> isOverlapping(res, startDate, endDate))
                .collect(Collectors.toList());
            
            Map<String, RoomTypeRevenue> revenueByType = new HashMap<>();
            BigDecimal totalRevenue = BigDecimal.ZERO;
            
            for (Reservation res : reservations) {
                String roomType = res.getRoomType() != null ? res.getRoomType().getRoomType() : "Unknown";
                BigDecimal dailyRate = res.getRoomType() != null ? 
                    new BigDecimal(res.getRoomType().getRate()) : BigDecimal.ZERO;
                
                long nights = ChronoUnit.DAYS.between(res.getCheckIn(), res.getCheckOut());
                BigDecimal revenue = dailyRate.multiply(new BigDecimal(nights));
                
                revenueByType.putIfAbsent(roomType, new RoomTypeRevenue(roomType));
                RoomTypeRevenue rtr = revenueByType.get(roomType);
                rtr.addBooking(revenue);
                totalRevenue = totalRevenue.add(revenue);
            }
            
            RevenueReport report = new RevenueReport(
                startDate.toString(),
                endDate.toString(),
                totalRevenue.toString(),
                reservations.size(),
                new ArrayList<>(revenueByType.values())
            );
            
            return Response.ok(report).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error generating revenue report: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get guest report
     * POST /api/reports/guest
     */
    @POST
    @Path("guest")
    public Response generateGuestReport(ReportRequest request) {
        try {
            if (request == null || !isValidDateRange(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid date range"))
                    .build();
            }
            
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            
            Map<String, Integer> guestCounts = new HashMap<>();
            List<Reservation> reservations = reservationManager.getAllReservations()
                .stream()
                .filter(res -> isOverlapping(res, startDate, endDate))
                .collect(Collectors.toList());
            
            for (Reservation res : reservations) {
                if (res.getGuest() != null) {
                    String guestName = res.getGuest().getName();
                    guestCounts.put(guestName, guestCounts.getOrDefault(guestName, 0) + 1);
                }
            }
            
            List<GuestStats> guestStats = guestCounts.entrySet().stream()
                .map(entry -> new GuestStats(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(GuestStats::getVisits).reversed())
                .collect(Collectors.toList());
            
            GuestReport report = new GuestReport(
                startDate.toString(),
                endDate.toString(),
                guestStats.size(),
                guestStats
            );
            
            return Response.ok(report).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Error generating guest report: " + e.getMessage()))
                .build();
        }
    }
    
    private boolean isValidDateRange(ReportRequest request) {
        return request.getStartDate() != null && !request.getStartDate().isEmpty()
            && request.getEndDate() != null && !request.getEndDate().isEmpty();
    }
    
    private boolean isOverlapping(Reservation res, LocalDate startDate, LocalDate endDate) {
        if (res.getCheckIn() == null || res.getCheckOut() == null) {
            return false;
        }
        return !res.getCheckOut().isBefore(startDate) && !res.getCheckIn().isAfter(endDate);
    }
    
    // ==================== DTOs ====================
    
    public static class ReportRequest {
        private String startDate;
        private String endDate;
        
        public ReportRequest() {}
        
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }
    
    public static class OccupancyReport {
        private String startDate;
        private String endDate;
        private int totalRooms;
        private int roomDaysBooked;
        private int totalRoomDays;
        private String occupancyPercentage;
        private int reservationCount;
        
        public OccupancyReport(String startDate, String endDate, int totalRooms, 
                              int roomDaysBooked, int totalRoomDays, 
                              String occupancyPercentage, int reservationCount) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalRooms = totalRooms;
            this.roomDaysBooked = roomDaysBooked;
            this.totalRoomDays = totalRoomDays;
            this.occupancyPercentage = occupancyPercentage;
            this.reservationCount = reservationCount;
        }
        
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public int getTotalRooms() { return totalRooms; }
        public int getRoomDaysBooked() { return roomDaysBooked; }
        public int getTotalRoomDays() { return totalRoomDays; }
        public String getOccupancyPercentage() { return occupancyPercentage; }
        public int getReservationCount() { return reservationCount; }
    }
    
    public static class RevenueReport {
        private String startDate;
        private String endDate;
        private String totalRevenue;
        private int bookingCount;
        private List<RoomTypeRevenue> revenueBreakdown;
        
        public RevenueReport(String startDate, String endDate, String totalRevenue, 
                           int bookingCount, List<RoomTypeRevenue> revenueBreakdown) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalRevenue = totalRevenue;
            this.bookingCount = bookingCount;
            this.revenueBreakdown = revenueBreakdown;
        }
        
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getTotalRevenue() { return totalRevenue; }
        public int getBookingCount() { return bookingCount; }
        public List<RoomTypeRevenue> getRevenueBreakdown() { return revenueBreakdown; }
    }
    
    public static class RoomTypeRevenue {
        private String roomType;
        private int bookingCount;
        private String totalRevenue;
        
        public RoomTypeRevenue(String roomType) {
            this.roomType = roomType;
            this.bookingCount = 0;
            this.totalRevenue = "0";
        }
        
        public void addBooking(BigDecimal revenue) {
            this.bookingCount++;
            this.totalRevenue = new BigDecimal(totalRevenue).add(revenue).toString();
        }
        
        public String getRoomType() { return roomType; }
        public int getBookingCount() { return bookingCount; }
        public String getTotalRevenue() { return totalRevenue; }
    }
    
    public static class GuestReport {
        private String startDate;
        private String endDate;
        private int totalGuests;
        private List<GuestStats> guests;
        
        public GuestReport(String startDate, String endDate, int totalGuests, List<GuestStats> guests) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalGuests = totalGuests;
            this.guests = guests;
        }
        
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public int getTotalGuests() { return totalGuests; }
        public List<GuestStats> getGuests() { return guests; }
    }
    
    public static class GuestStats {
        private String guestName;
        private int visits;
        
        public GuestStats(String guestName, int visits) {
            this.guestName = guestName;
            this.visits = visits;
        }
        
        public String getGuestName() { return guestName; }
        public int getVisits() { return visits; }
    }
    
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
