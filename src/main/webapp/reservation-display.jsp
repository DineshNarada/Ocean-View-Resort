<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.orrs.domain.Reservation, java.util.List"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservations - Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 30px;
            margin-bottom: 30px;
        }
        .page-header {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .page-title {
            color: #667eea;
            font-size: 24px;
            font-weight: 700;
        }
        .btn-new {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.2s ease;
        }
        .btn-new:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
            transform: translateY(-2px);
            color: white;
        }
        .table-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .table {
            margin: 0;
        }
        .table thead {
            background-color: #667eea;
            color: white;
        }
        .table th {
            font-weight: 600;
            padding: 15px;
            border: none;
        }
        .table td {
            padding: 12px 15px;
            border-bottom: 1px solid #e0e0e0;
        }
        .table tbody tr:hover {
            background-color: #f8f9fa;
        }
        .status-badge {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
        }
        .status-active {
            background-color: #d4edda;
            color: #155724;
        }
        .status-completed {
            background-color: #d1ecf1;
            color: #0c5460;
        }
        .status-cancelled {
            background-color: #f8d7da;
            color: #721c24;
        }
        .action-buttons {
            display: flex;
            gap: 5px;
        }
        .btn-sm {
            padding: 5px 10px;
            font-size: 12px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        .btn-edit {
            background-color: #17a2b8;
            color: white;
        }
        .btn-edit:hover {
            background-color: #138496;
        }
        .btn-delete {
            background-color: #dc3545;
            color: white;
        }
        .btn-delete:hover {
            background-color: #c82333;
        }
        .no-data {
            text-align: center;
            padding: 40px;
            color: #999;
        }
        .no-data i {
            font-size: 48px;
            margin-bottom: 20px;
            opacity: 0.5;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Page Header -->
        <div class="page-header">
            <h1 class="page-title">📅 Reservations</h1>
            <a href="${pageContext.request.contextPath}/reservation?action=new" class="btn-new">
                <i class="bi bi-plus-lg"></i> New Reservation
            </a>
        </div>

        <!-- Reservations Table -->
        <div class="table-container">
            <% if (reservations != null && !reservations.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Reservation ID</th>
                            <th>Guest Name</th>
                            <th>Phone</th>
                            <th>Check-in</th>
                            <th>Check-out</th>
                            <th>Room Type</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reservation res : reservations) { %>
                            <tr>
                                <td><strong><%= res.getReservationId() %></strong></td>
                                <td><%= res.getGuest().getName() %></td>
                                <td><%= res.getGuest().getPhone() %></td>
                                <td><%= res.getCheckInDate() %></td>
                                <td><%= res.getCheckOutDate() %></td>
                                <td><%= res.getRoomTypeId() %></td>
                                <td>
                                    <span class="status-badge status-active">
                                        <%= res.getStatus() %>
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/reservation?action=edit&id=<%= res.getReservationId() %>" 
                                           class="btn-sm btn-edit">
                                            <i class="bi bi-pencil"></i> Edit
                                        </a>
                                        <button onclick="if(confirm('Cancel this reservation?')) 
                                                        location.href='${pageContext.request.contextPath}/reservation?action=cancel&id=<%= res.getReservationId() %>'" 
                                                class="btn-sm btn-delete">
                                            <i class="bi bi-trash"></i> Cancel
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="no-data">
                    <i class="bi bi-inbox"></i>
                    <p>No reservations found</p>
                    <a href="${pageContext.request.contextPath}/reservation?action=new" class="btn-new">
                        Create your first reservation
                    </a>
                </div>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
