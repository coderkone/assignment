<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,model.Request,model.UserAccount" %>
<%
UserAccount u=(UserAccount)session.getAttribute("currentUser");
if(u==null){response.sendRedirect(request.getContextPath()+"/login");return;}
List<Request> my=(List<Request>)request.getAttribute("myRequests");
List<Request> sub=(List<Request>)request.getAttribute("subRequests");
%>
<html>
    <head><title>Danh sách đơn</title></head>
    <body>
        <h2>Xin chào: <%=u.getFullName()%></h2>
        <a href="${pageContext.request.contextPath}/request/create">Tạo đơn mới</a> |
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a> |
        <a href="${pageContext.request.contextPath}/agenda">Agenda</a>
        <h3>Đơn của tôi</h3>
        <table border="1">
            <tr><th>Title</th><th>From</th><th>To</th><th>Status</th></tr>
                    <% if(my!=null) for(Request r:my){ %>
            <tr>
                <td><%=r.getTitle()%></td>
                <td><%=r.getStartDate()%></td>
                <td><%=r.getEndDate()%></td>
                <td><%=r.getStatus()%></td>
            </tr>
            <% } %>
        </table>

        <% if(sub!=null){ %>
        <h3>Đơn của cấp dưới</h3>
        <form method="post" action="${pageContext.request.contextPath}/request/approve">
            <table border="1">
                <tr><th>Title</th><th>From</th><th>To</th><th>Status</th><th>Action</th></tr>
                        <% for(Request r:sub){ %>
                <tr>
                    <td><%=r.getTitle()%></td>
                    <td><%=r.getStartDate()%></td>
                    <td><%=r.getEndDate()%></td>
                    <td><%=r.getStatus()%></td>
                    <td>
                        <input type="radio" name="action_<%=r.getRequestId()%>" value="approve"/>Approve
                        <input type="radio" name="action_<%=r.getRequestId()%>" value="reject"/>Reject
                    </td>
                </tr>
                <% } %>
            </table>
            <% for(Request r:sub){ %>
            <input type="hidden" name="request_id" value="<%=r.getRequestId()%>"/>
            <input type="submit" name="submit_<%=r.getRequestId()%>" value="Xử lý <%=r.getRequestId()%>"/>
            <% } %>
        </form>
        <% } %>
    </body>
</html>
