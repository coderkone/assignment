package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.EmployeeDBContext;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Date;
import model.Employee;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Hiển thị form tạo đơn nghỉ
        req.getRequestDispatcher("/view/request/Create.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            // Lấy dữ liệu từ form
            String fromStr = req.getParameter("from");
            String toStr = req.getParameter("to");
            String reason = req.getParameter("reason");

            // Kiểm tra hợp lệ dữ liệu đầu vào
            if (fromStr == null || toStr == null || reason == null
                    || fromStr.isEmpty() || toStr.isEmpty() || reason.trim().isEmpty()) {
                req.setAttribute("message", "Vui lòng nhập đầy đủ thông tin!");
                req.getRequestDispatcher("/view/request/Create.jsp").forward(req, resp);
                return;
            }

            Date from = Date.valueOf(fromStr);
            Date to = Date.valueOf(toStr);

            // Lấy nhân viên tương ứng với người dùng đăng nhập
            EmployeeDBContext edb = new EmployeeDBContext();
            Employee emp = edb.getByUserId(user.getId());

            // Tạo đối tượng RequestForLeave
            RequestForLeave r = new RequestForLeave();
            r.setCreated_by(emp);
            r.setFrom(from);
            r.setTo(to);
            r.setReason(reason);
            r.setStatus(0); // 0 = In progress

            // Lưu vào database
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            db.insert(r);

            // ✅ Sau khi lưu thành công → chuyển hướng sang trang danh sách
            resp.sendRedirect(req.getContextPath() + "/request/list");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "Có lỗi xảy ra khi tạo đơn nghỉ phép!");
            req.getRequestDispatcher("/view/request/Create.jsp").forward(req, resp);
        }
    }
}
