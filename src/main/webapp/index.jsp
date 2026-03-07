<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        // User is already logged in, redirect to dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
        // User is not logged in, redirect to login
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
