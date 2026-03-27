package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/review")
public class ReviewController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String ridParam = req.getParameter("rid");

        // Nếu không có rid → vẫn vào giao diện trống
        if (ridParam == null || ridParam.isEmpty()) {
            req.getRequestDispatcher("/view/request/review.jsp").forward(req, resp);
            return;
        }

        try {
            int rid = Integer.parseInt(ridParam);
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            RequestForLeave rfl = db.getById(rid);

            if (rfl == null) {
                req.setAttribute("error", "Không tìm thấy đơn nghỉ phép này.");
            } else {
                req.setAttribute("rfl", rfl);
            }

            req.getRequestDispatcher("/view/request/review.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Mã đơn không hợp lệ.");
            req.getRequestDispatcher("/view/request/review.jsp").forward(req, resp);
        }
    }
    
    

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("rid"));
        int status = Integer.parseInt(req.getParameter("status"));
        String reason = req.getParameter("reason");

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        db.updateStatus(rid, status, user.getId(), reason);

        resp.sendRedirect(req.getContextPath() + "/request/list");
    }
}
