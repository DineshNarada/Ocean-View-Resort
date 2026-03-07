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
        * { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        body { background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%); }
        .container { margin-top: 40px; margin-bottom: 40px; }
        .page-header {
            background: white;
            padding: 28px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-bottom: 35px;
            border-left: 5px solid #00897b;
        }
        .page-title {
            color: #0d47a1;
            font-size: 28px;
            font-weight: 800;
        }
        .report-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
            margin-bottom: 35px;
        }
        .report-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            padding: 28px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
            border: 2px solid transparent;
        }
        .report-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
            border-color: #00897b;
        }
        .report-icon {
            font-size: 48px;
            margin-bottom: 16px;
            display: inline-block;
        }
        .report-title {
            color: #0d47a1;
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 10px;
        }
        .report-description {
            color: #757575;
            font-size: 14px;
            margin-bottom: 18px;
        }
        .btn-report {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 11px 22px;
            border-radius: 6px;
            text-decoration: none;
            transition: all 0.3s ease;
            font-weight: 700;
            font-size: 14px;
        }
        .btn-report:hover {
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(13, 71, 161, 0.2);
        }
        .report-content {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            padding: 35px;
            margin-top: 25px;
        }
        .report-heading {
            color: #0d47a1;
            font-size: 24px;
            font-weight: 800;
            margin-bottom: 25px;
            border-bottom: 3px solid #00897b;
            padding-bottom: 18px;
        }
        .filter-section {
            background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%);
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 25px;
            border: 2px solid #e0f2f1;
        }
        .filter-label {
            font-weight: 700;
            margin-bottom: 10px;
            color: #0d47a1;
            font-size: 14px;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .data-table th {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            padding: 14px;
            text-align: left;
            font-weight: 700;
            font-size: 14px;
        }
        .data-table td {
            padding: 12px 14px;
            border-bottom: 1px solid #e0f2f1;
        }
        .data-table tbody tr:hover {
            background: linear-gradient(90deg, #f0f9ff 0%, #ecfdf5 100%);
        }
        .form-control { border: 2px solid #e0f2f1; border-radius: 6px; }
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
