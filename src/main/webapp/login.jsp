<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        * {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        body {
            background: linear-gradient(135deg, #0d47a1 0%, #00897b 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .login-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 45px;
            width: 100%;
            max-width: 420px;
            position: relative;
            overflow: hidden;
        }
        .login-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #0d47a1, #00897b);
        }
        .login-header {
            text-align: center;
            margin-bottom: 35px;
        }
        .login-header .logo-icon {
            font-size: 48px;
            color: #00897b;
            margin-bottom: 15px;
        }
        .login-header h1 {
            color: #0d47a1;
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 8px;
        }
        .login-header p {
            color: #666;
            font-size: 14px;
        }
        .form-group {
            margin-bottom: 22px;
        }
        .form-label {
            color: #0d47a1;
            font-weight: 600;
            margin-bottom: 9px;
            display: block;
            font-size: 14px;
        }
        .form-control {
            border: 2px solid #e0f2f1;
            border-radius: 8px;
            padding: 12px 16px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #f8fffe;
        }
        .form-control::placeholder {
            color: #b0bec5;
        }
        .form-control:focus {
            border-color: #00897b;
            box-shadow: 0 0 0 3px rgba(0, 137, 123, 0.1);
            background-color: #fff;
        }
        .form-control:hover:not(:focus) {
            border-color: #4db8ac;
        }
        .btn-login {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #0d47a1 0%, #1565c0 50%, #00897b 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 15px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(13, 71, 161, 0.3);
        }
        .btn-login:active {
            transform: translateY(0);
        }
        .error-message {
            color: #c62828;
            font-size: 14px;
            padding: 14px;
            background-color: #ffebee;
            border: 2px solid #ef5350;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .success-message {
            color: #1b5e20;
            font-size: 14px;
            padding: 14px;
            background-color: #e8f5e9;
            border: 2px solid #66bb6a;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .demo-credentials {
            text-align: center;
            margin-top: 22px;
            padding-top: 22px;
            border-top: 1px solid #e0e0e0;
            font-size: 13px;
            color: #757575;
        }
        .demo-credentials p {
            margin: 4px 0;
        }
        .demo-credentials strong {
            color: #00897b;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <div class="logo-icon">
                <i class="bi bi-building"></i>
            </div>
            <h1>Ocean View Resort</h1>
            <p>Staff Portal Login</p>
        </div>

        <!-- Display error message if present -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <i class="bi bi-exclamation-circle"></i>
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- Display success message if present -->
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-message">
                <i class="bi bi-check-circle"></i>
                <%= request.getAttribute("success") %>
            </div>
        <% } %>

        <form method="POST" action="${pageContext.request.contextPath}/login" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username" class="form-label"><i class="bi bi-person"></i> Username</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       class="form-control" 
                       placeholder="Enter your username"
                       required>
            </div>

            <div class="form-group">
                <label for="password" class="form-label"><i class="bi bi-lock"></i> Password</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       class="form-control" 
                       placeholder="Enter your password"
                       required>
            </div>

            <button type="submit" class="btn-login">
                <i class="bi bi-box-arrow-in-right"></i> LOGIN
            </button>
        </form>

        <div class="demo-credentials">
            <p><strong>Demo Credentials</strong></p>
            <p>User: <strong>admin</strong></p>
            <p>Pass: <strong>password123</strong></p>
        </div>
    </div>

    <script>
        function validateForm() {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (username === '') {
                alert('Please enter your username');
                return false;
            }

            if (password === '') {
                alert('Please enter your password');
                return false;
            }

            if (username.length < 5) {
                alert('Username must be at least 5 characters');
                return false;
            }

            if (password.length < 6) {
                alert('Password must be at least 6 characters');
                return false;
            }

            return true;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
