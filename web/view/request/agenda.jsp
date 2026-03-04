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
    </style>
</head>
<body>
    <div class="agenda-container">
        <h1>📋 Tổng Quan Tình Hình Lao Động</h1>

        <% 
            AgendaData agendaData = (AgendaData) request.getAttribute("agendaData");
            Date currentStartDate = (Date) request.getAttribute("startDate");
            Date currentEndDate = (Date) request.getAttribute("endDate");
        %>

        <form action="division/agenda" method="get" style="margin-bottom: 20px;">
            <label for="from">Từ ngày:</label>
            <input type="date" id="from" name="from" value="<%= currentStartDate.toString() %>" class="date-input" required>
            <label for="to">Đến ngày:</label>
            <input type="date" id="to" name="to" value="<%= currentEndDate.toString() %>" class="date-input" required>
            <button type="submit" style="padding: 5px 15px;">Xem Agenda</button>
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
                                    Employee currentEmp = (Employee)pageContext.getAttribute("employee");
                                    Date currentDate = (Date)pageContext.getAttribute("date");
                                    AgendaData currentData = (AgendaData)pageContext.getAttribute("agendaData");
                                    
                                    String status = "P"; // Mặc định là Đi làm (Present)
                                    Map<Date, String> dailyMap = currentData.getEmployeeAgendaMap().get(currentEmp.getId());
                                    
                                    if (dailyMap != null && dailyMap.containsKey(currentDate)) {
                                        status = dailyMap.get(currentDate); // "L" (Leave)
                                    }
                                    
                                    String cssClass = (status.equals("L")) ? "status-L" : "status-P";
                                    String displayText = (status.equals("L")) ? "Nghỉ phép" : "Đi làm";
                                    
                                    out.print("<td class=\"" + cssClass + "\">" + displayText + "</td>");
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