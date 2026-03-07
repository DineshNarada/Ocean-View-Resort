# REST API Resource Structure Guide

## Overview

This guide explains the structure and organization of all REST API resources in the Ocean View Resort project.

---

## Directory Structure

```
src/main/java/com/orrs/
├── resources/                          # REST Endpoint Resources
│   ├── AuthResource.java              # Authentication endpoints
│   ├── ReservationResource.java       # Reservation management
│   ├── BillResource.java              # Billing & payment
│   ├── ReportResource.java            # Report generation
│   └── JakartaEE10Resource.java       # Demo endpoint
├── config/
│   ├── OpenAPIConfiguration.java      # OpenAPI/Swagger config
│   └── DatabaseConfig.java
├── manager/
│   ├── ReservationManager.java        # Business logic facade
│   └── AuthenticationManager.java
├── domain/
│   ├── Reservation.java
│   ├── Bill.java
│   ├── Guest.java
│   ├── RoomType.java
│   ├── Staff.java
│   └── Room.java
└── ...
```

---

## Resource Classes Explained

### 1. AuthResource.java
**Purpose:** Handles user authentication (login/logout)

**Endpoints:**
- `POST /auth/login` - Authenticate user
- `POST /auth/logout` - Invalidate session

**Inner Classes/DTOs:**
- `LoginRequest` - Username and password
- `LoginResponse` - Session token and user info
- `SuccessResponse` - Generic success message
- `ErrorResponse` - Error details with timestamp

**Key Methods:**
```java
Response login(LoginRequest credentials)       // Authenticate
Response logout(String sessionId)              // End session
```

**Features:**
- Session token generation
- Credential validation
- Error handling for missing/invalid credentials

---

### 2. ReservationResource.java
**Purpose:** Complete CRUD operations for reservations

**Endpoints:**
- `POST /reservations` - Create
- `GET /reservations` - List all
- `GET /reservations/{id}` - Get single
- `PUT /reservations/{id}` - Update
- `DELETE /reservations/{id}` - Cancel

**Inner Classes/DTOs:**
- `CreateReservationRequest` - New reservation data
- `UpdateReservationRequest` - Updated dates
- `ReservationResponse` - Reservation details

**Key Methods:**
```java
Response createReservation(CreateReservationRequest req)
Response getAllReservations()
Response getReservation(String id)
Response updateReservation(String id, UpdateReservationRequest req)
Response deleteReservation(String id)
```

**Validation:**
- Guest name validation (3-100 chars)
- Email format validation
- Phone number validation
- Check-in/check-out date validation
- Check-out must be after check-in

**Manager Connection:**
```
ReservationResource → ReservationManager → Domain Models
```

---

### 3. BillResource.java
**Purpose:** Billing and payment management

**Endpoints:**
- `GET /bills/{reservationId}` - Get bill
- `POST /bills` - Create bill
- `PUT /bills/{reservationId}/pay` - Process payment

**Inner Classes/DTOs:**
- `CreateBillRequest` - Bill creation data
- `PaymentRequest` - Payment information
- `BillResponse` - Bill details

**Key Methods:**
```java
Response getBill(String reservationId)
Response createBill(CreateBillRequest request)
Response payBill(String reservationId, PaymentRequest paymentRequest)
```

**Validation:**
- Bill amount must be positive
- Subtotal required
- Payment amount cannot exceed bill amount

**Business Logic:**
- Bill calculation (subtotal + tax - discount)
- Payment status tracking
- Amount validation

---

### 4. ReportResource.java
**Purpose:** Generate business reports

**Endpoints:**
- `POST /reports/occupancy` - Room occupancy
- `POST /reports/revenue` - Revenue analysis
- `POST /reports/guest` - Guest statistics

**Inner Classes/DTOs:**
- `ReportRequest` - Date range parameters
- `OccupancyReport` - Occupancy data
- `RevenueReport` - Revenue breakdown
- `GuestReport` - Guest statistics
- `RoomTypeRevenue` - Revenue by room type
- `GuestStats` - Individual guest data

**Key Methods:**
```java
Response generateOccupancyReport(ReportRequest request)
Response generateRevenueReport(ReportRequest request)
Response generateGuestReport(ReportRequest request)
```

**Report Calculations:**
- Occupancy: Room days booked / Total room days
- Revenue: Grouped by room type and date
- Guest: Frequency and repeat visitor tracking

**Features:**
- Date range filtering
- Aggregation and calculation
- Sorted results (e.g., top guests first)

---

### 5. OpenAPIConfiguration.java
**Purpose:** OpenAPI/Swagger API documentation configuration

**Annotations:**
- `@OpenAPIDefinition` - API metadata
- `@Info` - Title, version, description
- `@Contact` - Support contact
- `@Server` - Base URL
- `@Tag` - Endpoint categories

**Tags Defined:**
1. **Authentication** - Login/logout
2. **Reservations** - CRUD operations
3. **Bills** - Billing and payments
4. **Reports** - Report generation

**Access Documentation:**
```
http://localhost:8080/OceanViewResort/openapi
```

---

## Common Patterns Used

### 1. Error Response Pattern
All resources use consistent error responses:

```java
public static class ErrorResponse {
    private String error;
    private long timestamp;
    
    public ErrorResponse(String error) {
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
}
```

### 2. Success Response Pattern
All successful operations follow this pattern:

```java
return Response.ok(responseObject).build();
// or for creation
return Response.status(Response.Status.CREATED).entity(responseObject).build();
```

### 3. DTO Pattern
Each endpoint has DTOs for request and response:

```
Request DTO (Input) → Resource Method → Response DTO (Output)
```

### 4. Validation Pattern
All resources validate input before processing:

```java
if (!isValidRequest(request)) {
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(new ErrorResponse("Invalid data"))
        .build();
}
```

---

## HTTP Methods & Status Codes

### GET (Retrieve)
```java
GET /reservations           → 200 OK + List
GET /reservations/{id}      → 200 OK + Object OR 404 Not Found
```

### POST (Create)
```java
POST /reservations          → 201 Created + Object OR 400 Bad Request
```

### PUT (Update)
```java
PUT /reservations/{id}      → 200 OK + Object OR 404 Not Found
```

### DELETE (Remove)
```java
DELETE /reservations/{id}   → 200 OK + Message OR 404 Not Found
```

---

## Data Flow Example: Create Reservation

```
1. HTTP Request (POST /reservations)
   ↓
2. CreateReservationRequest DTO
   ↓
3. ReservationResource.createReservation()
   ├─ Validate input data
   ├─ Check dates
   └─ Create domain objects
   ↓
4. ReservationManager.addReservation()
   ├─ Generate ID
   ├─ Create Reservation object
   └─ Add to in-memory list
   ↓
5. ReservationResponse DTO
   ↓
6. HTTP Response (201 Created)
   + JSON representation of Reservation
```

---

## Integration Points

### With ReservationManager
- All endpoints call ReservationManager methods
- Manager maintains in-memory data storage
- Ready for database integration

### With Domain Models
- All DTOs map to domain models
- No business logic in resources
- Resources are thin controllers

### With Authentication
- AuthResource handles login/logout
- Session IDs generated on login
- Future: Add authentication filter

---

## Adding New Endpoints

To add a new endpoint to an existing resource:

1. **Create DTO classes** (if needed)
2. **Add method in Resource class**
3. **Implement business logic**
4. **Add validation**
5. **Add error handling**
6. **Update documentation**

Example:
```java
@GET
@Path("reports/staff/{staffId}")
public Response getStaffReport(@PathParam("staffId") int staffId) {
    try {
        // Implementation here
        return Response.ok(report).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ErrorResponse("Error: " + e.getMessage()))
            .build();
    }
}
```

---

## Testing Resources

### Manual Testing
- Use REST Client VS Code extension
- Or use cURL commands
- See REST_API_TESTING_GUIDE.md

### Automated Testing
Can add integration tests:
```java
@Test
public void testCreateReservation() {
    // Test logic
}
```

### Using Postman
1. Create new requests for each endpoint
2. Set headers: `Content-Type: application/json`
3. Add sample request bodies
4. Save as collection for team use

---

## Best Practices Implemented

✅ **RESTful Design**
- Proper HTTP methods
- Resource-oriented URLs
- Stateless operations

✅ **Error Handling**
- Consistent error format
- Appropriate status codes
- Descriptive messages

✅ **Input Validation**
- Validate all inputs
- Return 400 for bad data
- Check business rules

✅ **Documentation**
- JavaDoc comments
- OpenAPI annotations
- Example usage docs

✅ **Code Organization**
- DTOs in resource classes
- Single responsibility
- DRY principle applied

---

## Troubleshooting

### 404 Not Found
**Cause:** Wrong URL path  
**Solution:** Check @Path annotations and verify URL structure

### 400 Bad Request
**Cause:** Invalid JSON or missing required fields  
**Solution:** Check request format in documentation

### 500 Internal Server Error
**Cause:** Unexpected error in processing  
**Solution:** Check NetBeans Output tab for stack trace

### 415 Unsupported Media Type
**Cause:** Wrong Content-Type header  
**Solution:** Ensure header is `Content-Type: application/json`

---

## Performance Considerations

- **Current:** In-memory storage (ArrayList)
- **Scalability:** Suitable for 100-1000 concurrent users
- **Latency:** < 100ms per request typically
- **Upgrade Path:** Replace with database for production

---

## Security Notes

⚠️ Current implementation is for demonstration.

For production:
1. Implement JWT authentication
2. Add HTTPS/TLS
3. Add rate limiting
4. Add input sanitization
5. Add CORS configuration
6. Add logging/auditing

---

## Maintenance

### Regular Tasks
- Monitor error logs
- Check performance metrics
- Update dependencies
- Security patches
- Database optimization

### Version Updates
- Update Jakarta EE versions
- Update dependencies in pom.xml
- Test all endpoints after updates
- Update documentation

---

## References

- [Jakarta EE Documentation](https://jakarta.ee/)
- [JAX-RS Guide](https://jakarta.ee/specifications/restful-web-services/)
- [REST API Best Practices](https://restfulapi.net/)
- [OpenAPI Specification](https://spec.openapis.org/)

---

**Last Updated:** March 7, 2026  
**Document Purpose:** Guide for maintaining and extending REST API  
**Target Audience:** Developers maintaining the Ocean View Resort project
