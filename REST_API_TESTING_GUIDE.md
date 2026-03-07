# REST API - Quick Testing Guide (NetBeans)

## Running the Project in NetBeans

### Prerequisites
- JDK 17 or higher
- NetBeans IDE (8.2 or higher)
- GlassFish 7 server configured in NetBeans
- MySQL database running (if using persistence layer)

### Step 1: Build the Project
1. Right-click on **OceanViewResort** project
2. Select **Clean and Build** (Shift+F11)
3. Wait for build completion (check Output tab)

### Step 2: Run the Project
1. Right-click on **OceanViewResort** project
2. Select **Run** (F6)
3. NetBeans will start GlassFish and deploy the application
4. Application will open in browser at: `http://localhost:8080/OceanViewResort/`

### Step 3: Access REST API
Once the server is running, access REST endpoints at:
```
http://localhost:8080/OceanViewResort/resources/[endpoint]
```

---

## Testing Endpoints

### Option 1: Using Browser (GET requests only)
1. Open browser and navigate to:
   - Get all reservations: `http://localhost:8080/OceanViewResort/resources/reservations`
   - Get occupancy report: Use a REST client (see below)

### Option 2: Using Thunder Client (Built-in NetBeans Extension)
1. Open Thunder Client extension
2. Create a new request
3. Set method (GET, POST, PUT, DELETE)
4. Set URL: `http://localhost:8080/OceanViewResort/resources/auth/login`
5. Set body (for POST/PUT): 
   ```json
   {
     "username": "admin",
     "password": "password123"
   }
   ```
6. Click **Send**

### Option 3: Using REST Client Extension (VS Code)
1. Install "REST Client" extension in VS Code
2. Create `api-test.http` file in project root:
   ```
   ### Login
   POST http://localhost:8080/OceanViewResort/resources/auth/login
   Content-Type: application/json

   {
     "username": "admin",
     "password": "password123"
   }

   ### Get All Reservations
   GET http://localhost:8080/OceanViewResort/resources/reservations
   Content-Type: application/json

   ### Create Reservation
   POST http://localhost:8080/OceanViewResort/resources/reservations
   Content-Type: application/json

   {
     "guestId": 1,
     "guestName": "John Doe",
     "guestEmail": "john@example.com",
     "guestPhone": "555-0123",
     "roomTypeId": 101,
     "roomType": "Suite",
     "ratePerNight": 250.00,
     "checkIn": "2026-03-15",
     "checkOut": "2026-03-20"
   }

   ### Get Occupancy Report
   POST http://localhost:8080/OceanViewResort/resources/reports/occupancy
   Content-Type: application/json

   {
     "startDate": "2026-03-01",
     "endDate": "2026-03-31"
   }

   ### Get Revenue Report
   POST http://localhost:8080/OceanViewResort/resources/reports/revenue
   Content-Type: application/json

   {
     "startDate": "2026-03-01",
     "endDate": "2026-03-31"
   }

   ### Get Guest Report
   POST http://localhost:8080/OceanViewResort/resources/reports/guest
   Content-Type: application/json

   {
     "startDate": "2026-03-01",
     "endDate": "2026-03-31"
   }
   ```
3. Click "Send Request" on each request

### Option 4: Using cURL (Command Line)
Open terminal/command prompt and run:

```bash
# Login
curl -X POST http://localhost:8080/OceanViewResort/resources/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"password123\"}"

# Get all reservations
curl -X GET http://localhost:8080/OceanViewResort/resources/reservations \
  -H "Content-Type: application/json"

# Create a reservation
curl -X POST http://localhost:8080/OceanViewResort/resources/reservations \
  -H "Content-Type: application/json" \
  -d "{
    \"guestId\": 1,
    \"guestName\": \"John Doe\",
    \"guestEmail\": \"john@example.com\",
    \"guestPhone\": \"555-0123\",
    \"roomTypeId\": 101,
    \"roomType\": \"Suite\",
    \"ratePerNight\": 250.00,
    \"checkIn\": \"2026-03-15\",
    \"checkOut\": \"2026-03-20\"
  }"
```

---

## Viewing API Documentation

### OpenAPI/Swagger UI
Once the application is running:
1. Navigate to: `http://localhost:8080/OceanViewResort/openapi`
2. Or access Swagger UI if available: `http://localhost:8080/OceanViewResort/swagger-ui.html`

The API specification file is at: `REST_API_DOCUMENTATION.md`

---

## Troubleshooting

### Issue: "Resource not found" (404)
- **Cause:** Incorrect URL path or server not running
- **Solution:** 
  1. Check server is running (look for GlassFish in Output tab)
  2. Verify URL path matches: `/OceanViewResort/resources/[endpoint]`
  3. Check endpoint case sensitivity

### Issue: "Bad Request" (400)
- **Cause:** Invalid JSON or missing required fields
- **Solution:**
  1. Verify JSON format is correct (use JSONLint to validate)
  2. Check all required fields are included
  3. Verify date format: YYYY-MM-DD

### Issue: "Unauthorized" (401)
- **Cause:** Wrong credentials
- **Solution:** Use default credentials:
  - Username: `admin`
  - Password: `password123`

### Issue: "Internal Server Error" (500)
- **Cause:** Server-side error
- **Solution:**
  1. Check NetBeans Output tab for error details
  2. Look at GlassFish logs in `C:\Program Files\glassfish-*\glassfish\domains\domain1\logs\server.log`
  3. Verify database connection if using persistence layer

### Issue: "Cannot build project"
- **Cause:** Maven dependencies not downloaded
- **Solution:**
  1. Right-click project → Properties
  2. Go to Sources tab, check Java Source level is 17+
  3. Run Maven build from terminal: `mvn clean install`
  4. Download dependencies: `mvn dependency:resolve`

---

## Production Deployment

For production deployment to GlassFish server:

1. Build the WAR file:
   ```bash
   mvn clean package
   ```

2. Deploy to GlassFish:
   - Create directory: `C:\glassfish\domains\domain1\autodeploy\`
   - Copy `OceanViewResort.war` to autodeploy folder
   - Or use GlassFish Admin Console: `http://localhost:4848`

3. Configure real database connection in `persistence.xml`

4. Update `REST_API_DOCUMENTATION.md` base URL for production

---

## API Test Checklist

- [ ] Login endpoint returns session token
- [ ] Create reservation with valid data
- [ ] Get single reservation by ID
- [ ] Get all reservations list
- [ ] Update reservation dates
- [ ] Delete/cancel reservation
- [ ] Create bill for reservation
- [ ] Get bill by reservation ID
- [ ] Process payment on bill
- [ ] Generate occupancy report
- [ ] Generate revenue report
- [ ] Generate guest report
- [ ] Logout invalidates session

---

## Next Steps

1. **Add Integration Tests:** Create REST API integration tests using REST Assured
2. **Add Swagger UI:** Deploy Swagger UI for interactive API documentation
3. **Add Authentication:** Implement JWT tokens instead of simple session IDs
4. **Add Database:** Connect to real MySQL database instead of in-memory storage
5. **Add Caching:** Implement Redis caching for frequently accessed data
6. **Add API Gateway:** Deploy API Gateway for rate limiting and monitoring

---

## Documentation Files

- **REST_API_DOCUMENTATION.md** - Complete API reference with examples
- **src/main/java/com/orrs/config/OpenAPIConfiguration.java** - OpenAPI annotations
- **src/main/java/com/orrs/resources/** - REST resource implementations

---

## Files Modified/Created

### New Files Created:
- `src/main/java/com/orrs/resources/AuthResource.java`
- `src/main/java/com/orrs/resources/ReservationResource.java`
- `src/main/java/com/orrs/resources/BillResource.java`
- `src/main/java/com/orrs/resources/ReportResource.java`
- `src/main/java/com/orrs/config/OpenAPIConfiguration.java`
- `REST_API_DOCUMENTATION.md`

### Files Modified:
- `src/main/java/com/orrs/manager/ReservationManager.java` - Added bill management methods
- `pom.xml` - Added OpenAPI dependency
- `core/Task-2(GUID).md` - Marked Phase 5 as complete

---

## Support Resources

- Jakarta EE Documentation: https://jakarta.ee/
- JAX-RS Specification: https://jakarta.ee/specifications/restful-web-services/
- MicroProfile OpenAPI: https://github.com/eclipse/microprofile-open-api
- GlassFish Documentation: https://glassfish.org/
