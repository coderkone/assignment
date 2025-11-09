package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.EmployeeDBContext;
import dal.RequestForLeaveDBContext;
import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
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

    // Logic mẫu trong CreateController.java (doPost method)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Thu thập dữ liệu từ JSP
        String from_raw = request.getParameter("from");
        String to_raw = request.getParameter("to");
        String reason = request.getParameter("reason"); // Tên tham số là 'subject'

        HttpSession session = request.getSession();
        User authenticatedUser = (User) session.getAttribute("auth");

        // Kiểm tra bảo vệ (Guard Check)
        if (authenticatedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. 🎯 SỬ DỤNG HÀM getByUserId() CỦA BẠN 🎯
        // Khởi tạo DBContext và gọi hàm
        EmployeeDBContext empDB = new EmployeeDBContext();
        Employee createdBy = empDB.getByUserId(authenticatedUser.getId());
        try {
            // 3. Chuẩn bị đối tượng RequestForLeave
            RequestForLeave rfl = new RequestForLeave();
            rfl.setCreated_by(createdBy);
            rfl.setCreated_time(new Timestamp(System.currentTimeMillis()));
            rfl.setFrom(java.sql.Date.valueOf(from_raw));
            rfl.setTo(java.sql.Date.valueOf(to_raw));
            rfl.setReason(reason);
            rfl.setStatus(0); // Mặc định là 'Đang xử lý' (0)
            // rfl.setProcessed_by(null); // Không cần set nếu nó là null

            // 4. Gọi DB Context để lưu dữ liệu
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            db.insert(rfl);

            response.sendRedirect(request.getContextPath() +"/request/list");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi tạo đơn: " + e.getMessage());
            return;
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
