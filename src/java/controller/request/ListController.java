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

        // 1. Lấy và xử lý các tham số lọc (search, status, from, to)

        String searchValue = req.getParameter("searchValue");

        String status_raw = req.getParameter("status");
        Integer status = null;

        if (status_raw != null && !status_raw.trim().isEmpty()) {
            try {
                status = Integer.parseInt(status_raw);
            } catch (Exception e) {
                status = null;
            }
        }

        Date from = null, to = null;

        if (req.getParameter("from") != null && !req.getParameter("from").isEmpty()) {
            from = Date.valueOf(req.getParameter("from"));
        }
        if (req.getParameter("to") != null && !req.getParameter("to").isEmpty()) {
            to = Date.valueOf(req.getParameter("to"));
        }

        // 2. Lấy và xử lý tham số phân trang
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

        // 3. Khai báo Context và biến
        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> rfls;
        int totalRecords;

        // --------------------------------------------------------------------------------------
        // **KHẮC PHỤC BẢO MẬT:** LUÔN SỬ DỤNG HÀM LỌC THEO PHÂN CẤP (searchByEmployeeAndSubordinates)
        // Việc này đảm bảo người dùng chỉ xem được đơn của mình và cấp dưới.
        // --------------------------------------------------------------------------------------
        
        // Lấy TẤT CẢ đơn đã lọc theo người tạo (người dùng hiện tại VÀ cấp dưới)
        ArrayList<RequestForLeave> all
                = db.searchByEmployeeAndSubordinates(user.getId(), searchValue, status, from, to);

        totalRecords = all.size();

        if (all.isEmpty()) {
            rfls = new ArrayList<>();
        } else {
            // 4. Phân trang thủ công trên danh sách đã lọc
            int fromIndex = (pageIndex - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, all.size());

            if (fromIndex < all.size()) {
                rfls = new ArrayList<>(all.subList(fromIndex, toIndex));
            } else {
                rfls = new ArrayList<>();
            }
        }

        // 5. Thiết lập dữ liệu và Forward
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        req.setAttribute("rfls", rfls);
        req.setAttribute("pageindex", pageIndex);
        req.setAttribute("totalpage", totalPages);
        req.setAttribute("totalrecords", totalRecords); // Gửi cả tổng số bản ghi

        req.setAttribute("searchValue", searchValue);
        req.setAttribute("status", status);
        req.setAttribute("from", from);
        req.setAttribute("to", to);

        req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
    }
}