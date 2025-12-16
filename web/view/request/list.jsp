<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Danh sách đơn nghỉ phép</title>
        <style>
            * {
                box-sizing: border-box;
            }

            body {
                font-family: "Segoe UI", Arial, sans-serif;
                background-color: #f3f6fa;
                margin: 0;
                padding: 0;
            }

            header {
                background-color: #1a73e8;
                color: white;
                padding: 16px;
                text-align: center;
                font-size: 22px;
                font-weight: bold;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            }

            .container {
                max-width: 1100px;
                margin: 40px auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.08);
                padding: 30px 40px;
            }

            h2 {
                text-align: center;
                color: #333;
                margin-bottom: 25px;
            }

            form.search-bar {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                justify-content: center;
                margin-bottom: 25px;
            }

            form.search-bar input, form.search-bar select {
                padding: 10px 14px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 15px;
            }

            form.search-bar input[type="date"] {
                width: 180px;
            }

            form.search-bar input[type="text"] {
                width: 200px;
            }

            form.search-bar button {
                background-color: #1a73e8;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 6px;
                cursor: pointer;
                transition: background 0.25s;
                font-weight: 600;
            }

            form.search-bar button:hover {
                background-color: #155fc1;
            }



            table {
                width: 100%;
                border-collapse: collapse;
                overflow: hidden;
                border-radius: 10px;
            }

            th {
                background-color: #1a73e8;
                color: #fff;
                text-align: left;
                padding: 14px 16px;
                font-size: 15px;
            }

            td {
                padding: 12px 16px;
                border-bottom: 1px solid #e0e0e0;
                color: #333;
            }

            tr:hover {
                background-color: #f8faff;
            }

            .status-processing {
                color: #f9a825;
                font-weight: bold;
            }

            .status-approved {
                color: #43a047;
                font-weight: bold;
            }

            .status-rejected {
                color: #e53935;
                font-weight: bold;
            }

            .action-links a {
                text-decoration: none;
                margin-right: 10px;
                font-weight: 500;
                color: #1a73e8;
                padding: 6px 10px;
                border-radius: 5px;
                transition: 0.25s;
                border: 1px solid transparent;
            }

            .action-links a:hover {
                background-color: #e8f0fe;
                border-color: #1a73e8;
            }

            .btn-home {
                display: block;
                width: fit-content;
                margin: 30px auto 0;
                background-color: #1a73e8;
                color: white;
                text-align: center;
                padding: 12px 28px;
                border-radius: 8px;
                font-weight: 600;
                text-decoration: none;
                transition: background 0.3s;
            }

            .btn-home:hover {
                background-color: #155fc1;
            }

            .info-bar {
                text-align: center;
                margin-bottom: 20px;
                font-style: italic;
                color: #555;
            }

            .message {
                text-align: center;
                font-style: italic;
                color: #e53935;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <header>Danh sách đơn nghỉ phép</header>

        <div class="container">
            <div class="info-bar">
                <jsp:include page="../util/greeting.jsp"></jsp:include>
                </div>

                <!-- 🔍 Thanh tìm kiếm -->
                <!-- 🔍 Thanh tìm kiếm -->
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/request/list">
                <input 
                    type="text" 
                    name="searchValue" 
                    placeholder="Nhập mã hoặc tên nhân viên" 
                    value="${param.searchValue != null ? param.searchValue : ''}">

                <select name="status">
                    <option value="">-- Tất cả trạng thái --</option>
                    <option value="0" ${param.status == '0' ? 'selected' : ''}>Đang xử lý</option>
                    <option value="1" ${param.status == '1' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="2" ${param.status == '2' ? 'selected' : ''}>Từ chối</option>
                </select>

                <input type="date" name="from" value="${param.from}">
                <input type="date" name="to" value="${param.to}">

                <button type="submit">Tìm kiếm</button>
            </form>





            <c:if test="${not empty message}">
                <div style="text-align:center; color:red; font-weight:bold; margin-bottom:15px;">
                    ${message}
                </div>
            </c:if>



            <!-- 🧮 Bảng kết quả -->
            <c:if test="${not empty rfls}">

                <table>
                    <thead>
                        <tr>
                            <th>Người tạo</th>
                            <th>Lý do</th>
                            <th>Từ ngày</th>
                            <th>Đến ngày</th>
                            <th>Trạng thái</th>
                            <th>Xử lý</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${rfls}" var="r">
                            <tr>
                                <td>${r.created_by.name}</td>
                                <td>${r.reason}</td>
                                <td>${r.from}</td>
                                <td>${r.to}</td>
                                <td>
                                    <span class="${r.status eq 0 ? 'status-processing' : r.status eq 1 ? 'status-approved' : 'status-rejected'}">
                                        ${r.status eq 0 ? "Đang xử lý" : r.status eq 1 ? "Đã duyệt" : "Từ chối"}
                                    </span>
                                </td>
                                <td class="action-links">
                                    <c:choose>
                                        <c:when test="${r.status eq 0}">
                                            <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Duyệt</a>
                                            <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Từ chối</a>
                                        </c:when>
                                        <c:when test="${r.processed_by ne null}">
                                            ${r.processed_by.name}
                                            <c:if test="${r.status eq 1}">
                                                <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Từ chối</a>
                                            </c:if>
                                            <c:if test="${r.status eq 2}">
                                                <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Duyệt lại</a>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Duyệt</a>
                                            <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">Từ chối</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <form action="${requestScope.action}" method="${requestScope.method}">
                Page :<input type="text" name ="page" value="${requestScope.pageindex}"/>
                /${requestScope.totalpage}
                <input type="submit" value="Go"/>


            </form>
            <a href="${pageContext.request.contextPath}/home" class="btn-home">Quay lại trang chủ</a>
        </div>
    </body>
</html>
