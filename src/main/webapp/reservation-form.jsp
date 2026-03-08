<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.orrs.domain.RoomType, java.util.List"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<RoomType> roomTypes = (List<RoomType>) request.getAttribute("roomTypes");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservation Form - Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        * { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        body { background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%); min-height: 100vh; }
        .container { margin-top: 40px; margin-bottom: 40px; }
        .form-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
            padding: 40px;
            border: 2px solid #e0f2f1;
        }
        .form-title {
            color: #0d47a1;
            font-weight: 800;
            margin-bottom: 30px;
            border-bottom: 3px solid #00897b;
            padding-bottom: 20px;
            font-size: 28px;
        }
        .form-section { margin-bottom: 35px; padding-bottom: 25px; border-bottom: 2px solid #f0f4f8; }
        .form-section:last-of-type { border-bottom: none; }
        .section-title {
            color: #0d47a1;
            font-weight: 700;
            margin-bottom: 20px;
            font-size: 18px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .section-title i { color: #00897b; font-size: 20px; }
        .form-label { font-weight: 600; color: #1a237e; margin-bottom: 10px; font-size: 14px; }
        .form-control, .form-select {
            border: 2px solid #e0f2f1;
            border-radius: 8px;
            padding: 12px 16px;
            background-color: #f8fffe;
        }
        .form-control:focus, .form-select:focus {
            border-color: #00897b;
            box-shadow: 0 0 0 4px rgba(0, 137, 123, 0.1);
            background-color: #fff;
        }
        .error-list {
            background-color: #ffebee;
            border: 2px solid #ef5350;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 25px;
        }
        .btn-submit {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 14px 32px;
            border-radius: 8px;
            font-weight: 700;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .btn-submit:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(13, 71, 161, 0.2); color: white; }
        .btn-cancel {
            background-color: #e0e0e0;
            color: #424242;
            padding: 14px 32px;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        .required { color: #c62828; }
        .helper-text { color: #78909c; font-size: 12px; margin-top: 6px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-card">
            <h1 class="form-title">
                <i class="bi bi-calendar-plus"></i> New Reservation
            </h1>

            <% 
                java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
                if (errors != null && !errors.isEmpty()) {
            %>
                <div class="error-list">
                    <strong>⚠️ Please correct the following errors:</strong>
                    <ul class="mb-0">
                        <% for (String error : errors) { %>
                            <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
            <% } %>

            <form method="POST" action="${pageContext.request.contextPath}/reservation" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="save">
                
                <div class="form-section">
                    <div class="section-title">
                        <i class="bi bi-person-bounding-box"></i> Guest Information
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="guestName" class="form-label">Full Name <span class="required">*</span></label>
                            <input type="text" class="form-control" id="guestName" name="guestName" placeholder="John Doe" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="guestEmail" class="form-label">Email Address <span class="required">*</span></label>
                            <input type="email" class="form-control" id="guestEmail" name="guestEmail" placeholder="john@example.com" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="guestPhone" class="form-label">Phone Number <span class="required">*</span></label>
                            <input type="tel" class="form-control" id="guestPhone" name="guestPhone" placeholder="+1 555-0123" required>
                            <div class="helper-text">Format: 10-15 digits</div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="guestAddress" class="form-label">Street Address <span class="required">*</span></label>
                            <input type="text" class="form-control" id="guestAddress" name="guestAddress" placeholder="123 Ocean Way" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="guestCity" class="form-label">City <span class="required">*</span></label>
                            <input type="text" class="form-control" id="guestCity" name="guestCity" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="guestCountry" class="form-label">Country <span class="required">*</span></label>
                            <input type="text" class="form-control" id="guestCountry" name="guestCountry" required>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <div class="section-title">
                        <i class="bi bi-door-open"></i> Stay Details
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="checkIn" class="form-label">Check-in Date <span class="required">*</span></label>
                            <input type="date" class="form-control" id="checkIn" name="checkIn" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="checkOut" class="form-label">Check-out Date <span class="required">*</span></label>
                            <input type="date" class="form-control" id="checkOut" name="checkOut" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="roomTypeId" class="form-label">Room Type <span class="required">*</span></label>
                        <select class="form-select" id="roomTypeId" name="roomTypeId" required>
                            <option value="">-- Select a room type --</option>
                            <% if (roomTypes != null) {
                                for (RoomType rt : roomTypes) { %>
                                    <option value="<%= rt.getRoomTypeId() %>">
                                        <%= rt.getTypeName() %> - $<%= rt.getRatePerNight() %> per night
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="notes" class="form-label">Special Requests (Optional)</label>
                        <textarea class="form-control" id="notes" name="notes" rows="3" maxlength="500" placeholder="e.g. Early check-in, dietary requirements..."></textarea>
                        <div class="helper-text"><span id="charCount">0</span>/500 characters</div>
                    </div>
                </div>

                <div class="d-flex gap-3 mt-4">
                    <button type="submit" class="btn-submit">
                        <i class="bi bi-shield-check"></i> Confirm Reservation
                    </button>
                    <button type="reset" class="btn-cancel">
                        <i class="bi bi-eraser"></i> Clear
                    </button>
                    <a href="${pageContext.request.contextPath}/reservation?action=list" class="btn-cancel">
                        <i class="bi bi-x-circle"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Set min dates to today
        const today = new Date().toISOString().split('T')[0];
        const checkInInput = document.getElementById('checkIn');
        const checkOutInput = document.getElementById('checkOut');
        
        checkInInput.min = today;
        checkOutInput.min = today;

        // Dynamic check-out minimum
        checkInInput.addEventListener('change', function() {
            checkOutInput.min = this.value;
        });

        // Character counter
        document.getElementById('notes').addEventListener('input', function() {
            document.getElementById('charCount').textContent = this.value.length;
        });

        // Client-side validation
        function validateForm() {
            const checkIn = new Date(checkInInput.value);
            const checkOut = new Date(checkOutInput.value);

            if (checkOut <= checkIn) {
                alert('❌ Error: Check-out date must be at least one day after check-in.');
                return false;
            }

            const phone = document.getElementById('guestPhone').value;
            const phoneRegex = /^[0-9\+\-\s\(\)]{10,15}$/;
            if (!phoneRegex.test(phone)) {
                alert('❌ Error: Please enter a valid phone number (10-15 characters).');
                return false;
            }

            return true;
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>