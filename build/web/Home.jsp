<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Home - Leave Management System</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome (icons) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            background: #f8fafc;
            color: #1e293b;
        }
        .navbar-brand {
            font-weight: 700;
            letter-spacing: 0.3px;
        }
        .card {
            border: none;
            border-radius: 14px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
            transition: all 0.2s ease-in-out;
        }
        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
        }
        .feature-link {
            text-decoration: none;
            color: #0d6efd;
            font-weight: 500;
        }
        .feature-link:hover {
            text-decoration: underline;
            color: #084298;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand text-primary" href="#">
            <i class="fa-solid fa-briefcase me-2"></i>Leave Management
        </a>
        <div class="d-flex">
            <a href="logout" class="btn btn-outline-danger btn-sm">
                <i class="fa-solid fa-right-from-bracket me-1"></i> Đăng xuất
            </a>
        </div>
    </div>
</nav>

<!-- Main content -->
<div class="container">
    <div class="text-center mb-5">
        <h2 class="fw-bold">Xin chào, <span class="text-primary">${sessionScope.user.displayname}</span> 👋</h2>
        <p class="text-muted">Dưới đây là các chức năng bạn có thể truy cập:</p>
    </div>

    <div class="row g-3 justify-content-center">
        <c:forEach items="${features}" var="f">
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <div class="card h-100 text-center p-3">
                    <div class="card-body">
                        <div class="mb-3">
                            <i class="fa-solid fa-circle-check text-success fa-2x"></i>
                        </div>
                        <h6 class="card-title mb-3">
                            <a href="${pageContext.request.contextPath}${f.url}" class="feature-link">
                                ${f.fname}
                            </a>
                        </h6>
                        <a href="${pageContext.request.contextPath}${f.url}" class="btn btn-sm btn-primary">
                            <i class="fa-solid fa-arrow-right me-1"></i> Truy cập
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <footer class="text-center mt-5 mb-3 text-muted">
        © <%= java.time.Year.now() %> Leave
