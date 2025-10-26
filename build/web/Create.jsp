<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Tạo đơn xin nghỉ phép</title>
</head>
<body>
    <h2>Tạo đơn xin nghỉ phép</h2>

    <form action="${pageContext.request.contextPath}/create" method="post">
        <label>Từ ngày:</label>
        <input type="date" name="from" required><br><br>

        <label>Đến ngày:</label>
        <input type="date" name="to" required><br><br>

        <label>Lý do:</label><br>
        <textarea name="reason" rows="4" cols="40" required></textarea><br><br>

        <input type="submit" value="Gửi đơn">
    </form>

    <p style="color:green">${message}</p>

    <a href="${pageContext.request.contextPath}/home">← Quay lại trang chủ</a>
</body>
</html>
