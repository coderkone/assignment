<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Thông tin phiên làm việc</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f6f9;
                margin: 0;
                padding: 20px;
            }

            .session-box {
                max-width: 600px;
                margin: 0 auto 30px auto;
                background-color: #e3f2fd;
                padding: 16px 24px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
                font-size: 16px;
                color: #333;

            }

            .session-box p {
                margin: 10px 0;
            }

            .not-logged {
                text-align: center;
                color: #d93025;
                font-weight: 500;
                font-size: 16px;
            }
        </style>
    </head>
    <body>
        <c:if test="${sessionScope.auth ne null}">
            <div class="session-box">
                <p><strong>Session of:</strong> ${sessionScope.auth.displayname}</p>
                <p><strong>Employee:</strong> ${sessionScope.auth.employee.id} – ${sessionScope.auth.employee.name}</p>
            </div>
        </c:if>

        <c:if test="${sessionScope.auth eq null}">
            <p class="not-logged">Bạn chưa đăng nhập!</p>
        </c:if>
    </body>
</html>
