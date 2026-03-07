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
        * { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        body { background: linear-gradient(135deg, #f0f9ff 0%, #ecfdf5 100%); }
        .container { margin-top: 40px; margin-bottom: 40px; }
        .page-header {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-bottom: 30px;
            border-left: 5px solid #00897b;
        }
        .page-title {
            color: #0d47a1;
            font-size: 28px;
            font-weight: 800;
        }
        .bill-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            padding: 25px;
            margin-bottom: 24px;
            border-left: 5px solid #00897b;
            transition: all 0.3s ease;
        }
        .bill-card:hover { transform: translateY(-4px); box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12); }
        .bill-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 18px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e0f2f1;
        }
        .bill-id {
            font-size: 18px;
            font-weight: 700;
            color: #0d47a1;
        }
        .paid-badge {
            background: linear-gradient(135deg, #1b5e20, #2e7d32);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
        }
        .pending-badge {
            background: linear-gradient(135deg, #f57c00, #fb8c00);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
        }
        .bill-details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        .detail-item { margin-bottom: 12px; }
        .detail-label {
            color: #757575;
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-weight: 600;
        }
        .detail-value {
            color: #0d47a1;
            font-size: 18px;
            font-weight: 700;
        }
        .amount-display {
            background: linear-gradient(135deg, #e1f5fe 0%, #e0f2f1 100%);
            border-left: 5px solid #00897b;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 18px;
        }
        .amount-label {
            color: #00897b;
            font-size: 13px;
            text-transform: uppercase;
            font-weight: 700;
        }
        .amount-value {
            color: #0d47a1;
            font-size: 32px;
            font-weight: 800;
        }
        .btn-action {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 10px 16px;
            border-radius: 6px;
            font-size: 13px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-action:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(13, 71, 161, 0.3); }
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
