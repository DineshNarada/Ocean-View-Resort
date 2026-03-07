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
        * {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        body {
            background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%);
        }
        .container {
            margin-top: 40px;
            margin-bottom: 40px;
        }
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
        .form-section {
            margin-bottom: 35px;
            padding-bottom: 25px;
            border-bottom: 2px solid #f0f4f8;
        }
        .form-section:last-of-type {
            border-bottom: none;
        }
        .section-title {
            color: #0d47a1;
            font-weight: 700;
            margin-bottom: 20px;
            font-size: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .section-title i {
            color: #00897b;
            font-size: 20px;
        }
        .form-label {
            font-weight: 600;
            color: #1a237e;
            margin-bottom: 10px;
            font-size: 14px;
        }
        .form-control,
        .form-select {
            border: 2px solid #e0f2f1;
            border-radius: 8px;
            padding: 12px 16px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #f8fffe;
        }
        .form-control::placeholder {
            color: #b0bec5;
        }
        .form-control:focus,
        .form-select:focus {
            border-color: #00897b;
            box-shadow: 0 0 0 4px rgba(0, 137, 123, 0.1);
            background-color: #fff;
        }
        .form-control:hover:not(:focus),
        .form-select:hover:not(:focus) {
            border-color: #4db8ac;
        }
        .error-list {
            background-color: #ffebee;
            border: 2px solid #ef5350;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 25px;
        }
        .error-list strong {
            color: #c62828;
            display: block;
            margin-bottom: 8px;
        }
        .error-list li {
            color: #c62828;
            margin: 4px 0;
            font-size: 14px;
        }
        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 35px;
        }
        .btn-submit {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 14px 32px;
            border-radius: 8px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 15px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .btn-submit:hover {
            background: linear-gradient(135deg, #0a3d91 0%, #006b5b 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(13, 71, 161, 0.2);
        }
        .btn-cancel {
            background-color: #e0e0e0;
            color: #424242;
            padding: 14px 32px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
        }
        .btn-cancel:hover {
            background-color: #d0d0d0;
            color: #212121;
        }
        .required {
            color: #c62828;
            font-weight: 700;
        }
        .helper-text {
            color: #b0bec5;
            font-size: 13px;
            margin-top: 6px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-card">
            <h1 class="form-title">
                <i class="bi bi-calendar-plus"></i> New Reservation
            </h1>

            <!-- Display validation errors if any -->
            <% 
                java.util.List<String> errors = (java.util.List<String>) request.getAttribute("errors");
                if (errors != null && !errors.isEmpty()) {
            %>
                <div class="error-list">
                    <strong>⚠️ Please correct the following errors:</strong>
                    <ul style="margin: 0; padding-left: 20px;">
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
                    <div class="section-title">
                        <i class="bi bi-person-check"></i> Guest Information
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestName" class="form-label">Full Name <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestName" 
                                       name="guestName" 
                                       placeholder="John Doe"
                                       minlength="3"
                                       maxlength="100"
                                       required>
                                <div class="helper-text">Minimum 3 characters</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestEmail" class="form-label">Email Address <span class="required">*</span></label>
                                <input type="email" 
                                       class="form-control" 
                                       id="guestEmail" 
                                       name="guestEmail"
                                       placeholder="john.doe@example.com"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestPhone" class="form-label">Phone Number <span class="required">*</span></label>
                                <input type="tel" 
                                       class="form-control" 
                                       id="guestPhone" 
                                       name="guestPhone"
                                       placeholder="+1 (555) 123-4567"
                                       pattern="[0-9\+\-\s\(\)]{10,15}"
                                       required>
                                <div class="helper-text">10-15 characters including +, -, spaces, parentheses</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="guestAddress" class="form-label">Street Address <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="guestAddress" 
                                       name="guestAddress"
                                       placeholder="123 Main Street"
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
                                       placeholder="Los Angeles"
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
                                       placeholder="United States"
                                       required>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Reservation Details Section -->
                <div class="form-section">
                    <div class="section-title">
                        <i class="bi bi-building"></i> Reservation Details
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="checkIn" class="form-label">Check-in Date <span class="required">*</span></label>
                                <input type="date" 
                                       class="form-control" 
                                       id="checkIn" 
                                       name="checkIn"
                                       required>
                                <div class="helper-text">Cannot be in the past</div>
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
                                <div class="helper-text">Must be after check-in date</div>
                            </div>
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
                        <textarea class="form-control" 
                                  id="notes" 
                                  name="notes" 
                                  rows="3"
                                  placeholder="Any special requests? (e.g., High floor preferred, late check-in needed, etc.)"
                                  maxlength="500"></textarea>
                        <div class="helper-text"><span id="charCount">0</span>/500 characters</div>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="form-actions">
                    <button type="submit" class="btn-submit">
                        <i class="bi bi-check-lg"></i> Save Reservation
                    </button>
                    <button type="reset" class="btn-cancel">
                        <i class="bi bi-arrow-counterclockwise"></i> Clear Form
                    </button>
                    <a href="${pageContext.request.contextPath}/reservation?action=list" class="btn-cancel">
                        <i class="bi bi-arrow-left"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Set minimum date to today
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('checkIn').min = today;
        document.getElementById('checkOut').min = today;

        // Character counter for notes
        document.getElementById('notes').addEventListener('input', function() {
            document.getElementById('charCount').textContent = this.value.length;
        });

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
                alert('❌ Check-out date must be after check-in date');
                return false;
            }

            const daysStay = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));
            if (daysStay > 365) {
                alert('❌ Maximum stay is 365 days');
                return false;
            }

            // Validate phone number format
            const phone = document.getElementById('guestPhone').value;
            const phoneRegex = /^[0-9\+\-\s\(\)]{10,15}$/;
            if (!phoneRegex.test(phone)) {
                alert('❌ Please enter a valid phone number (10-15 characters)');
                return false;
            }

            return true;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
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
