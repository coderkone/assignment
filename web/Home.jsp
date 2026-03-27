<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang chủ - Leave Management System</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-light">

<!-- Navbar -->
<nav class="navbar navbar-expand-lg bg-white border-bottom shadow-sm fixed-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary" href="#">
            <i class="fa-solid fa-briefcase me-2"></i>Leave Management
        </a>
        <div class="ms-auto">
            <a href="logout" class="btn btn-outline-danger btn-sm">
                <i class="fa-solid fa-right-from-bracket me-1"></i> Đăng xuất
            </a>
        </div>
    </div>
</nav>

<!-- Main content -->
<div class="container" style="margin-top: 100px;">
    <!-- Welcome Section -->
    <div class="text-center mb-5">
        <h2 class="fw-semibold mb-2">
            Xin chào <span class="text-primary">${sessionScope.user.displayname}</span> 👋
        </h2>
        <p class="text-muted mb-0">
            Chào mừng bạn đến với hệ thống quản lý nghỉ phép.<br>
            Hãy chọn chức năng bạn muốn sử dụng bên dưới.
        </p>
    </div>

    <!-- Feature Cards -->
    <div class="row g-4 justify-content-center">
        <c:forEach items="${features}" var="f">
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <div class="card border-0 shadow-sm rounded-4 h-100">
                    <div class="card-body d-flex flex-column text-center p-4">
                        <div class="mb-3">
                            <i class="fa-solid fa-circle-check fa-2x text-primary"></i>
                        </div>
                        <h6 class="fw-semibold mb-3">
                            <a href="${pageContext.request.contextPath}${f.url}" class="text-decoration-none text-dark">
                                ${f.fname}
                            </a>
                        </h6>
                        <a href="${pageContext.request.contextPath}${f.url}" class="btn btn-primary btn-sm mt-auto">
                            <i class="fa-solid fa-arrow-right me-1"></i> Truy cập
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Footer -->
    <footer class="text-center text-muted mt-5 mb-4 small">
        © <%= java.time.Year.now() %> Leave Management System.
    </footer>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

</html>
