<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.orrs.domain.Staff"%>
<%
    Staff staff = (Staff) session.getAttribute("staff");
    if (staff == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 0;
            position: fixed;
            width: 250px;
            left: 0;
            top: 0;
        }
        .sidebar .navbar-brand {
            color: white !important;
            font-weight: 700;
            font-size: 18px;
            padding: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 12px 20px;
            transition: all 0.3s ease;
            border-left: 3px solid transparent;
        }
        .sidebar .nav-link:hover {
            color: white;
            background-color: rgba(255, 255, 255, 0.1);
            border-left-color: white;
        }
        .sidebar .nav-link.active {
            color: white;
            background-color: rgba(255, 255, 255, 0.2);
            border-left-color: white;
        }
        .sidebar .nav-link i {
            margin-right: 10px;
        }
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        .navbar {
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        .navbar .navbar-text {
            color: #333;
        }
        .card {
            border: none;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            transition: transform 0.2s ease;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .card-icon {
            font-size: 40px;
            margin-bottom: 15px;
        }
        .dashboard-card {
            text-align: center;
            padding: 30px;
        }
        .dashboard-card h5 {
            color: #333;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .dashboard-card p {
            color: #999;
            font-size: 14px;
        }
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 40px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .welcome-section h2 {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 10px;
        }
        .welcome-section p {
            font-size: 16px;
            opacity: 0.9;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
        }
        .btn-primary:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
        }
        @media (max-width: 768px) {
            .sidebar {
                position: relative;
                width: 100%;
                min-height: auto;
            }
            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Sidebar Navigation -->
    <div class="sidebar">
        <div class="navbar-brand">🏨 OVR System</div>
        <nav class="nav flex-column">
            <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-house-door"></i> Dashboard
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/reservation?action=list">
                <i class="bi bi-calendar-check"></i> Reservations
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/billing?action=list">
                <i class="bi bi-receipt"></i> Billing
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/report?type=menu">
                <i class="bi bi-graph-up"></i> Reports
            </a>
            <hr style="background-color: rgba(255, 255, 255, 0.2); margin: 20px 0;">
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-light">
            <div class="container-fluid">
                <span class="navbar-text">Welcome, <strong><%= staff.getUsername() %></strong></span>
                <span class="navbar-text ms-auto">
                    <small>Online Status: <span class="badge bg-success">Active</span></small>
                </span>
            </div>
        </nav>

        <!-- Welcome Section -->
        <div class="welcome-section">
            <h2>Welcome back, <%= staff.getFullName() %>!</h2>
            <p>You're logged in to the Ocean View Resort Management System</p>
        </div>

        <!-- Dashboard Cards -->
        <div class="feature-grid">
            <div class="card dashboard-card">
                <div class="card-icon" style="color: #667eea;">
                    <i class="bi bi-calendar-check"></i>
                </div>
                <h5>Reservations</h5>
                <p>Manage guest reservations</p>
                <a href="${pageContext.request.contextPath}/reservation?action=list" class="btn btn-primary btn-sm">View</a>
            </div>

            <div class="card dashboard-card">
                <div class="card-icon" style="color: #764ba2;">
                    <i class="bi bi-receipt"></i>
                </div>
                <h5>Billing</h5>
                <p>Process payments and bills</p>
                <a href="${pageContext.request.contextPath}/billing?action=list" class="btn btn-primary btn-sm">View</a>
            </div>

            <div class="card dashboard-card">
                <div class="card-icon" style="color: #f59e0b;">
                    <i class="bi bi-graph-up"></i>
                </div>
                <h5>Reports</h5>
                <p>View business analytics</p>
                <a href="${pageContext.request.contextPath}/report?type=menu" class="btn btn-primary btn-sm">View</a>
            </div>

            <div class="card dashboard-card">
                <div class="card-icon" style="color: #10b981;">
                    <i class="bi bi-plus-circle"></i>
                </div>
                <h5>New Reservation</h5>
                <p>Create a new booking</p>
                <a href="${pageContext.request.contextPath}/reservation?action=new" class="btn btn-primary btn-sm">Create</a>
            </div>
        </div>

        <!-- Quick Stats -->
        <div class="row mt-5">
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">Total Reservations</h6>
                        <h3 class="card-title">45</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">Active Bookings</h6>
                        <h3 class="card-title">23</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">Pending Payments</h6>
                        <h3 class="card-title">5</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h6 class="card-subtitle mb-2 text-muted">Total Revenue</h6>
                        <h3 class="card-title">$8,500</h3>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
