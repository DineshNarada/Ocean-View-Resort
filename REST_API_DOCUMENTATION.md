# Ocean View Resort REST API Documentation

**Version:** 1.0.0  
**Base URL:** `http://localhost:8080/OceanViewResort/resources`  
**Content-Type:** `application/json`

---

## Table of Contents
- [Authentication](#authentication)
- [Reservations](#reservations)
- [Bills & Payments](#bills--payments)
- [Reports](#reports)
- [Error Handling](#error-handling)
- [Testing Examples](#testing-examples)

---

## Authentication

### Login
Creates a user session and returns a session token.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response (Success - 200 OK):**
```json
{
  "message": "Authentication successful",
  "sessionId": "SESSION_1699564800000_0.7543210",
  "username": "admin"
}
```

**Response (Failure - 401 Unauthorized):**
```json
{
  "error": "Invalid credentials",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Login successful
- `400 Bad Request` - Missing username or password
- `401 Unauthorized` - Invalid credentials
- `500 Internal Server Error` - Server error

---

### Logout
Invalidates the user session.

**Endpoint:** `POST /auth/logout`

**Headers:**
```
X-Session-ID: SESSION_1699564800000_0.7543210
Content-Type: application/json
```

**Response (Success - 200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

**Status Codes:**
- `200 OK` - Logout successful
- `400 Bad Request` - Session ID missing
- `500 Internal Server Error` - Server error

---

## Reservations

### Create Reservation
Creates a new reservation for a guest.

**Endpoint:** `POST /reservations`

**Request Body:**
```json
{
  "guestId": 1,
  "guestName": "John Doe",
  "guestEmail": "john.doe@example.com",
  "guestPhone": "555-0123",
  "roomTypeId": 101,
  "roomType": "Suite",
  "ratePerNight": 250.00,
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-20"
}
```

**Response (Success - 201 Created):**
```json
{
  "id": "RES1001",
  "guestName": "John Doe",
  "roomType": "Suite",
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-20",
  "status": "PENDING"
}
```

**Response (Failure - 400 Bad Request):**
```json
{
  "error": "Check-out date must be after check-in date",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `201 Created` - Reservation created successfully
- `400 Bad Request` - Invalid data or invalid date range
- `500 Internal Server Error` - Server error

**Validation Rules:**
- Guest name: Required, 3-100 characters
- Email: Valid email format
- Phone: 10-15 digits with optional + or -
- Check-in: Cannot be in the past, format YYYY-MM-DD
- Check-out: Must be after check-in, max 365 days ahead
- Room type: Must exist in system

---

### Get Reservation
Retrieves a specific reservation by ID.

**Endpoint:** `GET /reservations/{id}`

**Example:** `GET /reservations/RES1001`

**Response (Success - 200 OK):**
```json
{
  "id": "RES1001",
  "guestName": "John Doe",
  "roomType": "Suite",
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-20",
  "status": "PENDING"
}
```

**Response (Failure - 404 Not Found):**
```json
{
  "error": "Reservation not found",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Reservation found
- `400 Bad Request` - Reservation ID missing
- `404 Not Found` - Reservation not found
- `500 Internal Server Error` - Server error

---

### Get All Reservations
Retrieves a list of all reservations.

**Endpoint:** `GET /reservations`

**Response (Success - 200 OK):**
```json
[
  {
    "id": "RES1001",
    "guestName": "John Doe",
    "roomType": "Suite",
    "checkIn": "2026-03-15",
    "checkOut": "2026-03-20",
    "status": "PENDING"
  },
  {
    "id": "RES1002",
    "guestName": "Jane Smith",
    "roomType": "Double",
    "checkIn": "2026-03-18",
    "checkOut": "2026-03-22",
    "status": "CONFIRMED"
  }
]
```

**Status Codes:**
- `200 OK` - Reservations retrieved successfully
- `500 Internal Server Error` - Server error

---

### Update Reservation
Updates check-in or check-out dates for an existing reservation.

**Endpoint:** `PUT /reservations/{id}`

**Example:** `PUT /reservations/RES1001`

**Request Body:**
```json
{
  "checkIn": "2026-03-16",
  "checkOut": "2026-03-21"
}
```

**Response (Success - 200 OK):**
```json
{
  "id": "RES1001",
  "guestName": "John Doe",
  "roomType": "Suite",
  "checkIn": "2026-03-16",
  "checkOut": "2026-03-21",
  "status": "PENDING"
}
```

**Response (Failure - 404 Not Found):**
```json
{
  "error": "Reservation not found",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Reservation updated successfully
- `400 Bad Request` - Invalid data
- `404 Not Found` - Reservation not found
- `500 Internal Server Error` - Server error

---

### Cancel Reservation
Cancels an existing reservation.

**Endpoint:** `DELETE /reservations/{id}`

**Example:** `DELETE /reservations/RES1001`

**Response (Success - 200 OK):**
```json
{
  "message": "Reservation cancelled successfully"
}
```

**Response (Failure - 404 Not Found):**
```json
{
  "error": "Reservation not found",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Reservation cancelled successfully
- `400 Bad Request` - Reservation ID missing
- `404 Not Found` - Reservation not found
- `500 Internal Server Error` - Server error

---

## Bills & Payments

### Get Bill
Retrieves a bill for a specific reservation.

**Endpoint:** `GET /bills/{reservationId}`

**Example:** `GET /bills/RES1001`

**Response (Success - 200 OK):**
```json
{
  "billId": 5001,
  "reservationId": "RES1001",
  "subtotal": "1250.00",
  "tax": "125.00",
  "discount": "0.00",
  "totalAmount": "1375.00",
  "paymentStatus": "PENDING"
}
```

**Response (Failure - 404 Not Found):**
```json
{
  "error": "Bill not found for this reservation",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Bill retrieved successfully
- `400 Bad Request` - Reservation ID missing
- `404 Not Found` - Bill not found
- `500 Internal Server Error` - Server error

---

### Create Bill
Creates a bill for a reservation.

**Endpoint:** `POST /bills`

**Request Body:**
```json
{
  "reservationId": "RES1001",
  "subtotal": "1250.00",
  "tax": "125.00",
  "discount": "0.00"
}
```

**Response (Success - 201 Created):**
```json
{
  "billId": 5001,
  "reservationId": "RES1001",
  "subtotal": "1250.00",
  "tax": "125.00",
  "discount": "0.00",
  "totalAmount": "1375.00",
  "paymentStatus": "PENDING"
}
```

**Status Codes:**
- `201 Created` - Bill created successfully
- `400 Bad Request` - Invalid data
- `500 Internal Server Error` - Server error

---

### Process Payment
Marks a bill as paid.

**Endpoint:** `PUT /bills/{reservationId}/pay`

**Example:** `PUT /bills/RES1001/pay`

**Request Body:**
```json
{
  "amountPaid": "1375.00",
  "paymentMethod": "CREDIT_CARD"
}
```

**Response (Success - 200 OK):**
```json
{
  "billId": 5001,
  "reservationId": "RES1001",
  "subtotal": "1250.00",
  "tax": "125.00",
  "discount": "0.00",
  "totalAmount": "1375.00",
  "paymentStatus": "PAID"
}
```

**Response (Failure - 400 Bad Request):**
```json
{
  "error": "Payment amount is less than bill amount",
  "timestamp": 1699564800000
}
```

**Status Codes:**
- `200 OK` - Payment processed successfully
- `400 Bad Request` - Invalid payment amount or missing data
- `404 Not Found` - Bill not found
- `500 Internal Server Error` - Server error

---

## Reports

### Occupancy Report
Generates an occupancy report for a date range.

**Endpoint:** `POST /reports/occupancy`

**Request Body:**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31"
}
```

**Response (Success - 200 OK):**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31",
  "totalRooms": 50,
  "roomDaysBooked": 1240,
  "totalRoomDays": 1550,
  "occupancyPercentage": "80.00%",
  "reservationCount": 35
}
```

**Status Codes:**
- `200 OK` - Report generated successfully
- `400 Bad Request` - Invalid date range
- `500 Internal Server Error` - Server error

---

### Revenue Report
Generates a revenue report for a date range.

**Endpoint:** `POST /reports/revenue`

**Request Body:**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31"
}
```

**Response (Success - 200 OK):**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31",
  "totalRevenue": "350000.00",
  "bookingCount": 45,
  "revenueBreakdown": [
    {
      "roomType": "Suite",
      "bookingCount": 15,
      "totalRevenue": "150000.00"
    },
    {
      "roomType": "Double",
      "bookingCount": 20,
      "totalRevenue": "150000.00"
    },
    {
      "roomType": "Single",
      "bookingCount": 10,
      "totalRevenue": "50000.00"
    }
  ]
}
```

**Status Codes:**
- `200 OK` - Report generated successfully
- `400 Bad Request` - Invalid date range
- `500 Internal Server Error` - Server error

---

### Guest Report
Generates a guest statistics report.

**Endpoint:** `POST /reports/guest`

**Request Body:**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31"
}
```

**Response (Success - 200 OK):**
```json
{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31",
  "totalGuests": 42,
  "guests": [
    {
      "guestName": "John Doe",
      "visits": 5
    },
    {
      "guestName": "Jane Smith",
      "visits": 3
    },
    {
      "guestName": "Bob Johnson",
      "visits": 2
    }
  ]
}
```

**Status Codes:**
- `200 OK` - Report generated successfully
- `400 Bad Request` - Invalid date range
- `500 Internal Server Error` - Server error

---

## Error Handling

All errors follow a consistent format:

**Error Response Format:**
```json
{
  "error": "Error message describing what went wrong",
  "timestamp": 1699564800000
}
```

**Common HTTP Status Codes:**
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input or malformed request
- `401 Unauthorized` - Authentication failed
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server-side error

---

## Testing Examples

### Using cURL

#### Login
```bash
curl -X POST http://localhost:8080/OceanViewResort/resources/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

#### Create Reservation
```bash
curl -X POST http://localhost:8080/OceanViewResort/resources/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "guestId": 1,
    "guestName": "John Doe",
    "guestEmail": "john@example.com",
    "guestPhone": "555-0123",
    "roomTypeId": 101,
    "roomType": "Suite",
    "ratePerNight": 250.00,
    "checkIn": "2026-03-15",
    "checkOut": "2026-03-20"
  }'
```

#### Get All Reservations
```bash
curl -X GET http://localhost:8080/OceanViewResort/resources/reservations \
  -H "Content-Type: application/json"
```

#### Get Occupancy Report
```bash
curl -X POST http://localhost:8080/OceanViewResort/resources/reports/occupancy \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2026-03-01",
    "endDate": "2026-03-31"
  }'
```

### Using Postman

1. Import the API collection
2. Set base URL: `http://localhost:8080/OceanViewResort/resources`
3. Create environment variables for sessionId
4. Use pre-request scripts to automatically set session headers

### OpenAPI/Swagger UI

Access the Swagger/OpenAPI documentation at:
```
http://localhost:8080/OceanViewResort/openapi
```

---

## Authentication Notes

- Default credentials: `admin` / `password123`
- Session IDs are returned in the login response
- Include `X-Session-ID` header for secured endpoints (future implementation)
- Sessions are invalidated on logout

---

## API Versioning

Current API Version: **1.0.0**

Future versions will be available at `/v2/`, `/v3/`, etc.

---

## Rate Limiting

Currently, no rate limiting is implemented. This may be added in future versions.

---

## Support

For issues or questions about the API, contact: support@oceanviewresort.com
