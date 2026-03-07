<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports - Ocean View Resort</title>
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
            margin-bottom: 30px;
        }
        .page-title {
            color: #667eea;
            font-size: 24px;
            font-weight: 700;
        }
        .report-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .report-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 25px;
            text-align: center;
            transition: transform 0.2s ease;
            cursor: pointer;
        }
        .report-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
        }
        .report-icon {
            font-size: 40px;
            margin-bottom: 15px;
        }
        .report-title {
            color: #333;
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .report-description {
            color: #999;
            font-size: 14px;
            margin-bottom: 15px;
        }
        .btn-report {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 5px;
            text-decoration: none;
            transition: all 0.2s ease;
        }
        .btn-report:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
            color: white;
        }
        .report-content {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin-top: 20px;
        }
        .report-heading {
            color: #667eea;
            font-size: 22px;
            font-weight: 700;
            margin-bottom: 20px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
        }
        .filter-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .filter-label {
            font-weight: 600;
            margin-bottom: 10px;
            color: #333;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        .data-table th {
            background-color: #667eea;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: 600;
        }
        .data-table td {
            padding: 10px 12px;
            border-bottom: 1px solid #e0e0e0;
        }
        .data-table tbody tr:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Page Header -->
        <div class="page-header">
            <h1 class="page-title">📊 Business Reports</h1>
        </div>

        <!-- Check if we're displaying a specific report -->
        <% String reportType = (String) request.getAttribute("reportType"); %>

        <% if (reportType == null) { %>
            <!-- Report Selection Menu -->
            <div class="report-grid">
                <div class="report-card">
                    <div class="report-icon">📈</div>
                    <div class="report-title">Occupancy Report</div>
                    <div class="report-description">Room utilization and capacity analysis</div>
                    <a href="${pageContext.request.contextPath}/report?type=occupancy" class="btn-report">View Report</a>
                </div>

                <div class="report-card">
                    <div class="report-icon">💰</div>
                    <div class="report-title">Revenue Report</div>
                    <div class="report-description">Income and financial performance metrics</div>
                    <a href="${pageContext.request.contextPath}/report?type=revenue" class="btn-report">View Report</a>
                </div>

                <div class="report-card">
                    <div class="report-icon">👥</div>
                    <div class="report-title">Guest Report</div>
                    <div class="report-description">Customer demographics and analytics</div>
                    <a href="${pageContext.request.contextPath}/report?type=guest" class="btn-report">View Report</a>
                </div>

                <div class="report-card">
                    <div class="report-icon">💳</div>
                    <div class="report-title">Payment Report</div>
                    <div class="report-description">Payment status and cash flow analysis</div>
                    <a href="${pageContext.request.contextPath}/report?type=payment" class="btn-report">View Report</a>
                </div>

                <div class="report-card">
                    <div class="report-icon">📋</div>
                    <div class="report-title">Reservation Status</div>
                    <div class="report-description">Booking status and trends</div>
                    <a href="${pageContext.request.contextPath}/report?type=status" class="btn-report">View Report</a>
                </div>

                <div class="report-card">
                    <div class="report-icon">👨‍💼</div>
                    <div class="report-title">Staff Activity</div>
                    <div class="report-description">Staff performance and productivity metrics</div>
                    <a href="${pageContext.request.contextPath}/report?type=staff" class="btn-report">View Report</a>
                </div>
            </div>
        <% } else { %>
            <!-- Report Display Section -->
            <div class="report-content">
                <h2 class="report-heading"><%= reportType %> Report</h2>

                <% if ("Occupancy".equals(reportType)) { %>
                    <div class="filter-section">
                        <form method="get" action="${pageContext.request.contextPath}/report">
                            <input type="hidden" name="type" value="occupancy">
                            <div class="row">
                                <div class="col-md-4">
                                    <label class="filter-label">Start Date:</label>
                                    <input type="date" name="startDate" class="form-control form-control-sm">
                                </div>
                                <div class="col-md-4">
                                    <label class="filter-label">End Date:</label>
                                    <input type="date" name="endDate" class="form-control form-control-sm">
                                </div>
                                <div class="col-md-4">
                                    <label class="filter-label">&nbsp;</label>
                                    <button type="submit" class="btn-report btn-block" style="width: 100%;">Generate Report</button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <p class="text-muted">Room occupancy rates for the selected period. Higher occupancy rates indicate better facility utilization.</p>

                <% } else if ("Revenue".equals(reportType)) { %>
                    <div class="filter-section">
                        <form method="get" action="${pageContext.request.contextPath}/report">
                            <input type="hidden" name="type" value="revenue">
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="filter-label">Select Month:</label>
                                    <input type="month" name="yearMonth" class="form-control form-control-sm">
                                </div>
                                <div class="col-md-6">
                                    <label class="filter-label">&nbsp;</label>
                                    <button type="submit" class="btn-report" style="width: 100%;">Generate Report</button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <p class="text-muted">Total revenue generated from room bookings and services for the selected month.</p>

                <% } else if ("Guest".equals(reportType)) { %>
                    <p class="text-muted">Guest demographics, visit frequency, and preferences analysis.</p>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Guest Name</th>
                                <th>Total Visits</th>
                                <th>Last Visit</th>
                                <th>Preferred Room</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>John Smith</td>
                                <td>5</td>
                                <td>2026-03-01</td>
                                <td>Suite</td>
                            </tr>
                            <tr>
                                <td>Jane Doe</td>
                                <td>3</td>
                                <td>2026-02-15</td>
                                <td>Double</td>
                            </tr>
                        </tbody>
                    </table>

                <% } else if ("Payment".equals(reportType)) { %>
                    <p class="text-muted">Payment status tracking and outstanding amounts analysis.</p>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Status</th>
                                <th>Count</th>
                                <th>Total Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Paid</td>
                                <td>82</td>
                                <td>$18,450</td>
                            </tr>
                            <tr>
                                <td>Pending</td>
                                <td>5</td>
                                <td>$1,200</td>
                            </tr>
                            <tr>
                                <td>Overdue</td>
                                <td>3</td>
                                <td>$600</td>
                            </tr>
                        </tbody>
                    </table>

                <% } else if ("Status".equals(reportType)) { %>
                    <p class="text-muted">Reservation status distribution and trends analysis.</p>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Status</th>
                                <th>Count</th>
                                <th>Percentage</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Active</td>
                                <td>25</td>
                                <td>28%</td>
                            </tr>
                            <tr>
                                <td>Completed</td>
                                <td>60</td>
                                <td>67%</td>
                            </tr>
                            <tr>
                                <td>Cancelled</td>
                                <td>5</td>
                                <td>5%</td>
                            </tr>
                        </tbody>
                    </table>

                <% } %>

                <div style="margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/report?type=menu" class="btn-report">← Back to Reports</a>
                </div>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
