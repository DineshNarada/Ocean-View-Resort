<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.orrs.domain.Reservation, com.orrs.domain.Room, com.orrs.dao.DAOFactory, com.orrs.dao.IRoomDAO, java.util.List"%>
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
        * { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        body { background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%); }
        .container { margin-top: 40px; margin-bottom: 40px; }
        .page-header {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-left: 5px solid #00897b;
        }
        .page-title {
            color: #0d47a1;
            font-size: 28px;
            font-weight: 800;
        }
        .btn-new {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 11px 22px;
            border-radius: 6px;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .btn-new:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(13, 71, 161, 0.2);
            color: white;
        }
        .table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            overflow: hidden;
        }
        .table { margin: 0; }
        .table thead {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
        }
        .table th {
            font-weight: 700;
            padding: 16px;
            border: none;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .table td {
            padding: 14px 16px;
            border-bottom: 1px solid #e0f2f1;
        }
        .table tbody tr {
            transition: all 0.2s ease;
        }
        .table tbody tr:hover {
            background-color: #f0f9ff;
        }
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
        }
        .status-active {
            background: linear-gradient(135deg, #1b5e20, #2e7d32);
            color: white;
        }
        .status-completed {
            background: linear-gradient(135deg, #0277bd, #0288d1);
            color: white;
        }
        .status-cancelled {
            background: linear-gradient(135deg, #c62828, #e53935);
            color: white;
        }
        .action-buttons {
            display: flex;
            gap: 6px;
        }
        .btn-sm {
            padding: 6px 11px;
            font-size: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s ease;
            font-weight: 600;
        }
        .btn-edit {
            background: linear-gradient(135deg, #0277bd, #0288d1);
            color: white;
        }
        .btn-edit:hover {
            transform: translateY(-2px);
            box-shadow: 0 3px 8px rgba(2, 119, 189, 0.3);
        }
        .btn-delete {
            background: linear-gradient(135deg, #c62828, #e53935);
            color: white;
        }
        .btn-delete:hover {
            transform: translateY(-2px);
            box-shadow: 0 3px 8px rgba(198, 40, 40, 0.3);
        }
        .no-data {
            text-align: center;
            padding: 50px;
            color: #b0bec5;
        }
        .no-data i {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.5;
            color: #b0bec5;
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
                                <td>
                                    <%
                                        try {
                                            IRoomDAO roomDAO = DAOFactory.getRoomDAO();
                                            Room room = roomDAO.readById(res.getRoomId());
                                            out.print(room != null ? room.getRoomNumber() : "N/A");
                                        } catch (Exception e) {
                                            out.print("N/A");
                                        }
                                    %>
                                </td>
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
