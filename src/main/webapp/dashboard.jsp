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
        * {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        body {
            background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%);
            margin: 0;
            padding: 0;
        }
        .sidebar {
            background: linear-gradient(180deg, #0d47a1 0%, #00897b 100%);
            min-height: 100vh;
            padding: 0;
            position: fixed;
            width: 260px;
            left: 0;
            top: 0;
            box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
        }
        .sidebar .navbar-brand {
            color: white !important;
            font-weight: 800;
            font-size: 20px;
            padding: 25px;
            border-bottom: 2px solid rgba(255, 255, 255, 0.15);
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.85);
            padding: 14px 20px;
            transition: all 0.3s ease;
            border-left: 4px solid transparent;
            font-size: 15px;
            font-weight: 500;
        }
        .sidebar .nav-link:hover {
            color: white;
            background-color: rgba(255, 255, 255, 0.12);
            border-left-color: rgba(255, 255, 255, 0.8);
            transform: translateX(4px);
        }
        .sidebar .nav-link.active {
            color: white;
            background: rgba(255, 255, 255, 0.15);
            border-left-color: #4dd0e1;
            font-weight: 600;
        }
        .sidebar .nav-link i {
            margin-right: 12px;
            width: 20px;
            text-align: center;
        }
        .sidebar hr {
            background-color: rgba(255, 255, 255, 0.15);
            margin: 15px 0;
        }
        .main-content {
            margin-left: 260px;
            padding: 30px;
        }
        .navbar {
            background: white;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-bottom: 30px;
            border-radius: 10px;
            border: none;
        }
        .navbar .navbar-text {
            color: #0d47a1;
            font-weight: 600;
        }
        .status-badge {
            background: linear-gradient(135deg, #00897b, #4db8ac);
            color: white;
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        .welcome-section {
            background: linear-gradient(135deg, #0d47a1 0%, #1565c0 50%, #00897b 100%);
            color: white;
            padding: 40px;
            border-radius: 12px;
            margin-bottom: 35px;
            box-shadow: 0 8px 24px rgba(13, 71, 161, 0.15);
            position: relative;
            overflow: hidden;
        }
        .welcome-section::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -10%;
            width: 300px;
            height: 300px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 50%;
        }
        .welcome-section h2 {
            font-size: 32px;
            font-weight: 800;
            margin-bottom: 8px;
            position: relative;
            z-index: 1;
        }
        .welcome-section p {
            font-size: 16px;
            opacity: 0.95;
            position: relative;
            z-index: 1;
        }
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
            margin-bottom: 35px;
        }
        .dashboard-card {
            background: white;
            border-radius: 12px;
            padding: 28px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
            border: 2px solid transparent;
            text-align: center;
        }
        .dashboard-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
            border-color: #00897b;
        }
        .dashboard-card .card-icon {
            font-size: 48px;
            margin-bottom: 16px;
            display: inline-block;
        }
        .dashboard-card h5 {
            color: #0d47a1;
            font-weight: 700;
            margin-bottom: 8px;
            font-size: 18px;
        }
        .dashboard-card p {
            color: #757575;
            font-size: 14px;
            margin-bottom: 16px;
        }
        .btn-dashboard {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        .btn-dashboard:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(13, 71, 161, 0.3);
            color: white;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 35px;
        }
        .stat-card {
            background: white;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            border-top: 4px solid;
        }
        .stat-card.blue {
            border-top-color: #0d47a1;
        }
        .stat-card.green {
            border-top-color: #00897b;
        }
        .stat-card.teal {
            border-top-color: #00796b;
        }
        .stat-card.cyan {
            border-top-color: #004d7a;
        }
        .stat-card h6 {
            color: #999;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 12px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .stat-card h3 {
            color: #0d47a1;
            font-size: 32px;
            font-weight: 800;
            margin: 0;
        }
        @media (max-width: 768px) {
            .sidebar {
                position: fixed;
                width: 260px;
                transform: translateX(-100%);
                transition: transform 0.3s ease;
                z-index: 1000;
            }
            .sidebar.active {
                transform: translateX(0);
            }
            .main-content {
                margin-left: 0;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <!-- Sidebar Navigation -->
    <div class="sidebar">
        <div class="navbar-brand">
            <i class="bi bi-building"></i>
            <span>Ocean View</span>
        </div>
        <nav class="nav flex-column">
            <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-house-door-fill"></i> Dashboard
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/reservation?action=list">
                <i class="bi bi-calendar-check"></i> Reservations
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/billing?action=list">
                <i class="bi bi-receipt"></i> Billing
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/report?type=menu">
                <i class="bi bi-bar-chart-line"></i> Reports
            </a>
            <a class="nav-link" href="${pageContext.request.contextPath}/reservation?action=new">
                <i class="bi bi-plus-circle"></i> New Reservation
            </a>
            <hr>
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg">
            <div class="container-fluid">
                <span class="navbar-text">
                    Welcome, <strong><%= staff.getUsername() %></strong>
                </span>
                <span class="navbar-text ms-auto">
                    <span class="status-badge">
                        <i class="bi bi-dot"></i> Online
                    </span>
                </span>
            </div>
        </nav>

        <!-- Welcome Section -->
        <div class="welcome-section">
            <h2>Welcome back, <%= staff.getFullName() %>!</h2>
            <p>Manage your resort operations with ease and efficiency</p>
        </div>

        <!-- Dashboard Feature Cards -->
        <div class="feature-grid">
            <div class="dashboard-card">
                <div class="card-icon" style="color: #0d47a1;">
                    <i class="bi bi-calendar2-check"></i>
                </div>
                <h5>Reservations</h5>
                <p>Manage guest bookings</p>
                <a href="${pageContext.request.contextPath}/reservation?action=list" class="btn-dashboard">View All</a>
            </div>

            <div class="dashboard-card">
                <div class="card-icon" style="color: #00897b;">
                    <i class="bi bi-receipt"></i>
                </div>
                <h5>Billing</h5>
                <p>Process payments & invoices</p>
                <a href="${pageContext.request.contextPath}/billing?action=list" class="btn-dashboard">View All</a>
            </div>

            <div class="dashboard-card">
                <div class="card-icon" style="color: #1565c0;">
                    <i class="bi bi-bar-chart"></i>
                </div>
                <h5>Reports</h5>
                <p>View analytics & insights</p>
                <a href="${pageContext.request.contextPath}/report?type=menu" class="btn-dashboard">View All</a>
            </div>

            <div class="dashboard-card">
                <div class="card-icon" style="color: #00796b;">
                    <i class="bi bi-plus-lg"></i>
                </div>
                <h5>New Booking</h5>
                <p>Create a reservation</p>
                <a href="${pageContext.request.contextPath}/reservation?action=new" class="btn-dashboard">Create</a>
            </div>
        </div>

        <!-- Quick Statistics -->
        <div class="stats-grid">
            <div class="stat-card blue">
                <h6>Total Reservations</h6>
                <h3>45</h3>
            </div>
            <div class="stat-card green">
                <h6>Active Bookings</h6>
                <h3>23</h3>
            </div>
            <div class="stat-card teal">
                <h6>Pending Payments</h6>
                <h3>5</h3>
            </div>
            <div class="stat-card cyan">
                <h6>Total Revenue</h6>
                <h3>$8,500</h3>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
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
