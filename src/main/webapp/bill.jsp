<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.orrs.domain.Bill, java.util.List"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Bill> bills = (List<Bill>) request.getAttribute("bills");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Billing - Ocean View Resort</title>
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
        }
        .page-title {
            color: #667eea;
            font-size: 24px;
            font-weight: 700;
        }
        .bill-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
        }
        .bill-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #e0e0e0;
        }
        .bill-id {
            font-size: 16px;
            font-weight: 600;
            color: #333;
        }
        .paid-badge {
            background-color: #d4edda;
            color: #155724;
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
        }
        .pending-badge {
            background-color: #fff3cd;
            color: #856404;
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
        }
        .bill-details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        .detail-item {
            margin-bottom: 10px;
        }
        .detail-label {
            color: #999;
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .detail-value {
            color: #333;
            font-size: 16px;
            font-weight: 600;
        }
        .amount-display {
            background-color: #f0f7ff;
            border-left: 4px solid #667eea;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 15px;
        }
        .amount-label {
            color: #999;
            font-size: 12px;
        }
        .amount-value {
            color: #667eea;
            font-size: 24px;
            font-weight: 700;
        }
        .btn-action {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 5px;
            font-size: 12px;
            cursor: pointer;
            transition: transform 0.2s ease;
        }
        .btn-action:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
            transform: translateY(-2px);
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
            <h1 class="page-title">💳 Billing & Payments</h1>
        </div>

        <!-- Bills Display -->
        <% if (bills != null && !bills.isEmpty()) { %>
            <% for (Bill bill : bills) { %>
                <div class="bill-card">
                    <div class="bill-header">
                        <div>
                            <div class="bill-id">Bill #<%= bill.getId() %></div>
                            <small style="color: #999;">Reservation: <%= bill.getReservationId() %></small>
                        </div>
                        <div>
                            <% if (bill.isPaid()) { %>
                                <span class="paid-badge">✓ PAID</span>
                            <% } else { %>
                                <span class="pending-badge">◐ PENDING</span>
                            <% } %>
                        </div>
                    </div>

                    <div class="bill-details">
                        <div>
                            <div class="detail-item">
                                <div class="detail-label">Number of Nights</div>
                                <div class="detail-value"><%= bill.getNights() %> nights</div>
                            </div>
                            <div class="detail-item">
                                <div class="detail-label">Rate per Night</div>
                                <div class="detail-value">$<%= String.format("%.2f", bill.getRatePerNight()) %></div>
                            </div>
                        </div>
                        <div>
                            <div class="detail-item">
                                <div class="detail-label">Created</div>
                                <div class="detail-value"><%= bill.getCreatedDate() != null ? bill.getCreatedDate().toLocalDate() : "N/A" %></div>
                            </div>
                            <div class="detail-item">
                                <div class="detail-label">Amount Paid</div>
                                <div class="detail-value">$<%= String.format("%.2f", bill.getPaidAmount() != null ? bill.getPaidAmount() : 0) %></div>
                            </div>
                        </div>
                    </div>

                    <div class="amount-display">
                        <div class="amount-label">Total Amount</div>
                        <div class="amount-value">$<%= String.format("%.2f", bill.getAmount()) %></div>
                    </div>

                    <% if (!bill.isPaid()) { %>
                        <form method="POST" action="${pageContext.request.contextPath}/billing" style="margin-bottom: 10px;">
                            <input type="hidden" name="action" value="process_payment">
                            <input type="hidden" name="billId" value="<%= bill.getId() %>">
                            
                            <div class="row">
                                <div class="col-md-8">
                                    <input type="number" 
                                           name="paymentAmount" 
                                           class="form-control form-control-sm" 
                                           placeholder="Enter payment amount" 
                                           step="0.01" 
                                           min="0" 
                                           max="<%= bill.getAmount() %>"
                                           required>
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn-action" style="width: 100%;">
                                        💰 Process Payment
                                    </button>
                                </div>
                            </div>
                        </form>
                    <% } %>
                </div>
            <% } %>
        <% } else { %>
            <div class="no-data">
                <i class="bi bi-inbox"></i>
                <p>No bills found</p>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
