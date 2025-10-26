package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.EmployeeDBContext;
import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import model.BaseModel;
import model.Employee;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.getRequestDispatcher("Create.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            // Lấy dữ liệu từ form
            String fromStr = req.getParameter("from");
            String toStr = req.getParameter("to");
            String reason = req.getParameter("reason");

            Date from = Date.valueOf(fromStr);
            Date to = Date.valueOf(toStr);

            // ✅ Lấy Employee tương ứng với User hiện tại
            EmployeeDBContext edb = new EmployeeDBContext();
            Employee emp = edb.getByUserId(user.getId());

            // ✅ Tạo request mới
            RequestForLeave r = new RequestForLeave();
            r.setCreated_by(emp);
            r.setFrom(from);
            r.setTo(to);
            r.setReason(reason);
            r.setStatus(0); // 0 = Inprogress

           DBContext db = new DBContext() {
                @Override
                public ArrayList list() {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                @Override
                public BaseModel get(int id) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                @Override
                public void insert(BaseModel model) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                @Override
                public void update(BaseModel model) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }

                @Override
                public void delete(BaseModel model) {
                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }
            };
            db.insert(r);

            req.setAttribute("message", "Tạo đơn nghỉ phép thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "Có lỗi xảy ra khi tạo đơn nghỉ phép!");
        }

        req.getRequestDispatcher("Create.jsp").forward(req, resp);
    }
}
