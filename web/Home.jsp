<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Home - Leave Management System</title>
</head>
<body>
    <h2>Xin chào, ${sessionScope.user.displayname}</h2>

    <h3>Chuc nang ban có the truy cap:</h3>
    <ul>
        <c:forEach items="${features}" var="f">
            <li><a href="${f.url}">${f.url}</a></li>
        </c:forEach>
    </ul>

    <hr>
    <p><a href="logout">dang xuat</a></p>
</body>
</html>
