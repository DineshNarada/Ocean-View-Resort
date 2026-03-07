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
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 30px;
            margin-bottom: 30px;
        }
        .form-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        .form-title {
            color: #667eea;
            font-weight: 700;
            margin-bottom: 30px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
        }
        .form-section {
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #e0e0e0;
        }
        .section-title {
            color: #333;
            font-weight: 600;
            margin-bottom: 15px;
            font-size: 16px;
        }
        .form-label {
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
        }
        .form-control {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 10px 15px;
        }
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .error-list {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .error-list li {
            color: #721c24;
        }
        .btn-submit {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s ease;
        }
        .btn-submit:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
            transform: translateY(-2px);
        }
        .btn-cancel {
            background-color: #6c757d;
            color: white;
            padding: 12px 30px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            margin-left: 10px;
        }
        .btn-cancel:hover {
            background-color: #5a6268;
        }
        .required {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-card">
            <h1 class="form-title">📋 New Reservation</h1>

            <!-- Display validation errors if any -->
            <% 
                java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
                if (errors != null && !errors.isEmpty()) {
            %>
                <div class="error-list">
                    <strong>Please correct the following errors:</strong>
                    <ul>
                        <% for (String error : errors) { %>
                            <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
            <% } %>

            <form method="POST" action="${pageContext.request.contextPath}/reservation" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="reservationId" value="">

                <!-- Guest Information Section -->
                <div class="form-section">
                    <span class="section-title">👤 Guest Information</span>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestName" class="form-label">Full Name <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestName" 
                                       name="guestName" 
                                       placeholder="Enter guest's full name"
                                       minlength="3"
                                       maxlength="100"
                                       required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestEmail" class="form-label">Email <span class="required">*</span></label>
                                <input type="email" 
                                       class="form-control" 
                                       id="guestEmail" 
                                       name="guestEmail"
                                       placeholder="guest@example.com"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestPhone" class="form-label">Phone <span class="required">*</span></label>
                                <input type="tel" 
                                       class="form-control" 
                                       id="guestPhone" 
                                       name="guestPhone"
                                       placeholder="+1 (555) 000-0000"
                                       pattern="[0-9\+\-\s\(\)]{10,15}"
                                       required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestAddress" class="form-label">Address <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestAddress" 
                                       name="guestAddress"
                                       placeholder="Street address"
                                       minlength="10"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestCity" class="form-label">City <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestCity" 
                                       name="guestCity"
                                       placeholder="City name"
                                       required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestCountry" class="form-label">Country <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestCountry" 
                                       name="guestCountry"
                                       placeholder="Country name"
                                       required>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Reservation Details Section -->
                <div class="form-section">
                    <span class="section-title">🏨 Reservation Details</span>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="checkIn" class="form-label">Check-in Date <span class="required">*</span></label>
                                <input type="date" 
                                       class="form-control" 
                                       id="checkIn" 
                                       name="checkIn"
                                       required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="checkOut" class="form-label">Check-out Date <span class="required">*</span></label>
                                <input type="date" 
                                       class="form-control" 
                                       id="checkOut" 
                                       name="checkOut"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="roomTypeId" class="form-label">Room Type <span class="required">*</span></label>
                        <select class="form-control" id="roomTypeId" name="roomTypeId" required>
                            <option value="">-- Select a room type --</option>
                            <% if (roomTypes != null) {
                                for (RoomType rt : roomTypes) { %>
                                    <option value="<%= rt.getRoomTypeId() %>">
                                        <%= rt.getTypeName() %> - $<%= rt.getRatePerNight() %>/night
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="notes" class="form-label">Special Requests (Optional)</label>
                        <textarea class="form-control" 
                                  id="notes" 
                                  name="notes" 
                                  rows="4"
                                  placeholder="Any special requests or notes for the guest?"
                                  maxlength="500"></textarea>
                        <small class="text-muted">Max 500 characters</small>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="mt-4">
                    <button type="submit" class="btn-submit">💾 Save Reservation</button>
                    <button type="reset" class="btn-cancel">Clear Form</button>
                    <a href="${pageContext.request.contextPath}/reservation?action=list" class="btn-cancel">← Back</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Set minimum date to today
        document.getElementById('checkIn').min = new Date().toISOString().split('T')[0];
        document.getElementById('checkOut').min = new Date().toISOString().split('T')[0];

        // Update check-out minimum when check-in changes
        document.getElementById('checkIn').addEventListener('change', function() {
            const checkOut = document.getElementById('checkOut');
            if (this.value) {
                checkOut.min = this.value;
            }
        });

        function validateForm() {
            const checkIn = new Date(document.getElementById('checkIn').value);
            const checkOut = new Date(document.getElementById('checkOut').value);

            if (checkOut <= checkIn) {
                alert('Check-out date must be after check-in date');
                return false;
            }

            const daysStay = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));
            if (daysStay > 365) {
                alert('Maximum stay is 365 days');
                return false;
            }

            // Validate phone number format
            const phone = document.getElementById('guestPhone').value;
            const phoneRegex = /^[0-9\+\-\s\(\)]{10,15}$/;
            if (!phoneRegex.test(phone)) {
                alert('Please enter a valid phone number');
                return false;
            }

            return true;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
