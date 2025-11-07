package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processRequest(req, resp, user);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processRequest(req, resp, user);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String searchValue = req.getParameter("searchValue");
        Integer status = null;
        if (req.getParameter("status") != null && !req.getParameter("status").isEmpty()) {
            status = Integer.parseInt(req.getParameter("status"));
        }

        Date from = null, to = null;
        if (req.getParameter("from") != null && !req.getParameter("from").isEmpty()) {
            from = Date.valueOf(req.getParameter("from"));
        }
        if (req.getParameter("to") != null && !req.getParameter("to").isEmpty()) {
            to = Date.valueOf(req.getParameter("to"));
        }

        int pageSize = 10;
        int pageIndex = 1;
        String page_raw = req.getParameter("page");
        if (page_raw != null && !page_raw.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(page_raw);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> rfls;
        int totalRecords;

        // 🔹 Trường hợp đầu tiên truy cập (không có tìm kiếm / lọc)
        if ((searchValue == null || searchValue.isEmpty()) 
                && status == null 
                && from == null 
                && to == null) {

            rfls = db.page(pageIndex, pageSize);
            totalRecords = db.count(); // ✅ đếm tổng bản ghi, không phải tổng trang
        } 
        // 🔹 Có điều kiện lọc
        else {
            ArrayList<RequestForLeave> all = db.searchByEmployeeAndSubordinates(user.getId(), searchValue, status, from, to);
            totalRecords = all.size();

            if (all.isEmpty()) {
                req.setAttribute("message", "Không tìm thấy đơn nào phù hợp!");
                rfls = new ArrayList<>();
            } else {
                int fromIndex = (pageIndex - 1) * pageSize;
                int toIndex = Math.min(fromIndex + pageSize, all.size());
                if (fromIndex < all.size()) {
                    rfls = new ArrayList<>(all.subList(fromIndex, toIndex));
                } else {
                    rfls = new ArrayList<>();
                }
            }
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        req.setAttribute("rfls", rfls);
        req.setAttribute("pageindex", pageIndex);
        req.setAttribute("totalpage", totalPages); // ✅ khớp với JSP (totalpage)
        req.setAttribute("searchValue", searchValue);
        req.setAttribute("status", status);
        req.setAttribute("from", from);
        req.setAttribute("to", to);

        req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
    }
}
