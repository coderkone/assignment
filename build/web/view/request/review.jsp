<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Duyệt đơn xin nghỉ phép</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #1a73e8;
            color: white;
            text-align: center;
            padding: 16px 0;
            font-size: 22px;
            font-weight: 600;
        }

        .container {
            max-width: 800px;
            margin: 40px auto;
            background: white;
            border-radius: 12px;
            padding: 40px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        h2 {
            color: #1a73e8;
            text-align: center;
            margin-bottom: 30px;
            font-size: 22px;
        }

        .info {
            background-color: #e3f2fd;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 25px;
            font-size: 16px;
        }

        .info p {
            margin: 10px 0;
            color: #333;
        }

        .reason-box {
            background-color: #fff;
            padding: 16px;
            border-radius: 8px;
            border: 1px solid #d0d0d0;
            font-size: 16px;
            color: #444;
            min-height: 100px;
        }

        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 25px;
        }

        .action-buttons button {
            background-color: #1a73e8;
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.3s;
            min-width: 120px;
        }

        .action-buttons button:hover {
            background-color: #155fc1;
        }

        .nav-buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 35px;
        }

        .btn-link {
            background-color: #1a73e8;
            color: #fff;
            text-decoration: none;
            text-align: center;
            padding: 12px 30px;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            display: inline-block;
            min-width: 200px;
            transition: background 0.3s;
        }

        .btn-link:hover {
            background-color: #155fc1;
        }

        .not-found {
            text-align: center;
            color: #d93025;
            font-weight: 500;
            font-size: 18px;
            margin-top: 40px;
        }
    </style>
</head>
<body>
    <header>Duyệt đơn xin nghỉ phép</header>

    <div class="container">
        <c:if test="${not empty rfl}">
            <div class="info">
                <p><strong>Người tạo:</strong> ${rfl.created_by.name}</p>
                <p><strong>Từ ngày:</strong> ${rfl.from}</p>
                <p><strong>Đến ngày:</strong> ${rfl.to}</p>
                <p><strong>Lý do:</strong></p>
                <div class="reason-box">${rfl.reason}</div>
            </div>

            <form action="${pageContext.request.contextPath}/request/review" method="post">
                <input type="hidden" name="rid" value="${rfl.id}" />
                <input type="hidden" name="reason" value="${rfl.reason}" />

                <div class="action-buttons">
                    <button type="submit" name="status" value="2" style="background-color:#d93025;">Từ chối</button>
                    <button type="submit" name="status" value="1" style="background-color:#34a853;">Duyệt</button>
                </div>
            </form>
        </c:if>

        <c:if test="${empty rfl}">
            <p class="not-found">Không tìm thấy đơn nghỉ phép này.</p>
        </c:if>

        <div class="nav-buttons">
            <a href="${pageContext.request.contextPath}/request/list" class="btn-link">&larr; Quay lại danh sách</a>
            <a href="${pageContext.request.contextPath}/home" class="btn-link">Quay lại trang chủ →</a>
        </div>
    </div>
</body>
</html>
