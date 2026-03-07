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
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .error-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 500px;
            text-align: center;
        }
        .error-icon {
            font-size: 80px;
            color: #dc3545;
            margin-bottom: 20px;
        }
        .error-title {
            color: #333;
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 15px;
        }
        .error-message {
            color: #666;
            font-size: 16px;
            margin-bottom: 20px;
            line-height: 1.6;
        }
        .error-details {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
            color: #721c24;
            text-align: left;
        }
        .btn-action {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 5px;
            text-decoration: none;
            cursor: pointer;
            font-weight: 600;
            transition: transform 0.2s ease;
            display: inline-block;
            margin: 5px;
        }
        .btn-action:hover {
            background: linear-gradient(135deg, #5568d3 0%, #6a3f96 100%);
            transform: translateY(-2px);
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
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
