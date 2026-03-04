<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.AgendaData" %>
<%@page import="java.sql.Date" %>
<%@page import="controller.division.DateUtil" %>
<%@page import="model.Employee" %>
<%@page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>📋 Màn Hình Agenda Lao Động</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .agenda-container { margin: 20px; }
        .agenda-table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed; 
        }
        .agenda-table th, .agenda-table td {
            border: 1px solid #ddd;
            padding: 8px 4px; 
            text-align: center;
        }
        .header-employee {
            text-align: left !important;
            width: 150px; 
        }
        .status-P {
            background-color: #e6ffe6; /* Xanh nhạt: Đi làm */
            color: #008000;
            font-size: 0.85em; 
        }
        .status-L {
            background-color: #ffe6e6; /* Đỏ nhạt: Nghỉ phép */
            color: #cc0000;
            font-size: 0.85em;
        }
        .date-input { padding: 5px; margin-right: 10px; }
        .filter-form {
            background-color: #f5f5f5;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            display: flex;
            gap: 15px;
            align-items: center;
        }
    </style>
    <script>
        function onDateChange() {
            document.querySelector('form').submit();
        }
    </script>
</head>
<body>
    <div class="agenda-container">
        <h1>📋 Tổng Quan Tình Hình Lao Động</h1>

        <% 
            AgendaData agendaData = (AgendaData) request.getAttribute("agendaData");
            Date currentStartDate = (Date) request.getAttribute("startDate");
            Date currentEndDate = (Date) request.getAttribute("endDate");
            
            if (agendaData == null) {
                out.println("<p style=\"color: red;\">Lỗi: Không thể lấy dữ liệu agenda. Vui lòng thử lại.</p>");
                return;
            }
        %>

        <form action="${pageContext.request.contextPath}/division/agenda" method="get" class="filter-form">
            <label for="from" style="font-weight: bold;">Từ ngày:</label>
            <input type="date" id="from" name="from" value="<%= currentStartDate != null ? currentStartDate.toString() : "" %>" class="date-input" onchange="onDateChange()" required>
            
            <label for="to" style="font-weight: bold;">Đến ngày:</label>
            <input type="date" id="to" name="to" value="<%= currentEndDate != null ? currentEndDate.toString() : "" %>" class="date-input" onchange="onDateChange()" required>
        </form>

        <c:if test="${agendaData.employees.isEmpty()}">
            <p>Không có nhân viên nào trong phòng ban này hoặc không có dữ liệu để hiển thị.</p>
        </c:if>
        <c:if test="${!agendaData.employees.isEmpty()}">
            <table class="agenda-table">
                <thead>
                    <tr>
                        <th class="header-employee">Nhân viên</th>
                        <c:forEach var="date" items="${agendaData.allDates}">
                            <c:set var="formattedDate" value="<%= DateUtil.formatDate_dd_MM((Date)pageContext.getAttribute(\"date\")) %>"/>
                            <th>${formattedDate}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="employee" items="${agendaData.employees}">
                        <tr>
                            <td class="header-employee">${employee.name}</td>
                            <c:forEach var="date" items="${agendaData.allDates}">
                                <% 
                                    try {
                                        Employee currentEmp = (Employee)pageContext.getAttribute("employee");
                                        Date currentDate = (Date)pageContext.getAttribute("date");
                                        AgendaData agendaDataFromRequest = (AgendaData)request.getAttribute("agendaData");
                                        
                                        String status = "P"; // Mặc định là Đi làm (Present)
                                        
                                        if (currentEmp != null && currentDate != null && agendaDataFromRequest != null) {
                                            Map<Date, String> dailyMap = agendaDataFromRequest.getEmployeeAgendaMap().get(currentEmp.getId());
                                            
                                            if (dailyMap != null && dailyMap.containsKey(currentDate)) {
                                                status = dailyMap.get(currentDate); // "L" (Leave)
                                            }
                                        }
                                        
                                        String cssClass = (status.equals("L")) ? "status-L" : "status-P";
                                        String displayText = (status.equals("L")) ? "Nghỉ phép" : "Đi làm";
                                        
                                        out.print("<td class=\"" + cssClass + "\">" + displayText + "</td>");
                                    } catch (Exception e) {
                                        out.print("<td class=\"status-P\">Lỗi</td>");
                                        System.out.println("Error in agenda cell: " + e.getMessage());
                                    }
                                %>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>