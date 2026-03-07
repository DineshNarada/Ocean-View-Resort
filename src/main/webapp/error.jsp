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
    <title>Error - Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        * { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        body {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .error-container {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
            padding: 45px;
            max-width: 520px;
            text-align: center;
            border-top: 5px solid #c62828;
        }
        .error-icon {
            font-size: 80px;
            color: #c62828;
            margin-bottom: 20px;
        }
        .error-title {
            color: #0d47a1;
            font-size: 28px;
            font-weight: 800;
            margin-bottom: 15px;
        }
        .error-message {
            color: #424242;
            font-size: 16px;
            margin-bottom: 22px;
            line-height: 1.6;
        }
        .error-details {
            background: linear-gradient(135deg, #ffebee 0%, #ffe0e6 100%);
            border: 2px solid #ef5350;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 24px;
            color: #c62828;
            text-align: left;
        }
        .error-details strong {
            display: block;
            margin-bottom: 8px;
        }
        .btn-action {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 6px;
            text-decoration: none;
            cursor: pointer;
            font-weight: 700;
            transition: all 0.3s ease;
            display: inline-block;
            margin: 6px;
        }
        .btn-action:hover {
            transform: translateY(-2px);
            color: white;
            box-shadow: 0 6px 16px rgba(13, 71, 161, 0.2);
        }
        .btn-secondary {
            background: #e0e0e0;
            color: #424242;
        }
        .btn-secondary:hover {
            background: #d0d0d0;
            color: #212121;
        }
        hr { border-color: #e0e0e0; }
        small a { color: #00897b; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="bi bi-exclamation-circle"></i>
        </div>
        
        <h1 class="error-title">Oops! An Error Occurred</h1>
        
        <div class="error-message">
            We encountered an unexpected problem while processing your request.
        </div>

        <!-- Display error message if present -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-details">
                <strong>Error Details:</strong>
                <p><%= request.getAttribute("error") %></p>
            </div>
        <% } else { %>
            <div class="error-details">
                <strong>Error Details:</strong>
                <p>The requested page or action could not be completed.</p>
            </div>
        <% } %>

        <div>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn-action">
                <i class="bi bi-house-door"></i> Go to Dashboard
            </a>
            <a href="javascript:history.back()" class="btn-action btn-secondary">
                <i class="bi bi-arrow-left"></i> Go Back
            </a>
        </div>

        <hr style="margin-top: 30px; margin-bottom: 20px;">
        
        <small style="color: #999;">
            If this problem persists, please contact system administrator.
            <br>
            <a href="${pageContext.request.contextPath}/logout" style="color: #667eea; text-decoration: none;">
                Or log out and try again
            </a>
        </small>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
