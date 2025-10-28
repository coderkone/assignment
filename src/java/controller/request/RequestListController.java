package controller.request;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.Employee;
import model.RequestForLeave;
import dal.EmployeeDBContext;
import dal.RequestForLeaveDBContext;
import model.iam.User;

public class RequestListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Kiểm tra đăng nhập
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("currentUser");

        try {
            // ✅ Lấy Employee tương ứng với User hiện tại
            EmployeeDBContext edb = new EmployeeDBContext();
            Employee emp = edb.getByUserId(user.getId());

            // ✅ Lấy danh sách tất cả đơn (của mình + cấp dưới)
            RequestForLeaveDBContext rdb = new RequestForLeaveDBContext();
            ArrayList<RequestForLeave> allRequests = rdb.getByEmployeeAndSubodiaries(emp.getId());

            // ✅ Tách riêng đơn của mình và cấp dưới
            ArrayList<RequestForLeave> myRequests = new ArrayList<>();
            ArrayList<RequestForLeave> subRequests = new ArrayList<>();

            for (RequestForLeave r : allRequests) {
                if (r.getCreated_by().getId() == emp.getId()) {
                    myRequests.add(r);
                } else {
                    subRequests.add(r);
                }
            }

            // ✅ Gửi dữ liệu sang JSP
            req.setAttribute("myRequests", myRequests);
            req.setAttribute("subRequests", subRequests);

            // ✅ Chuyển đến trang hiển thị danh sách
            req.getRequestDispatcher("/view/request/review.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi tải danh sách đơn nghỉ phép: " + e.getMessage());
        }
    }
}
