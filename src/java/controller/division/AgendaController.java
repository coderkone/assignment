package controller;

import controller.division.DateUtil;
import dal.AgendaDBContext;
import dal.EmployeeDBContext;
import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import model.RequestForLeave;
import model.iam.Role;
import model.iam.User;
import model.AgendaData;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AgendaData;

// Đảm bảo mapping là /division/agenda
@WebServlet("/division/agenda")
public class AgendaController extends HttpServlet {
    
    /**
     * Kiểm tra xem người dùng có Role 'Manager' hay không.
     * Trả về: 
     * true: Có quyền Manager.
     * false: Không có quyền Manager (hoặc lỗi Session).
     */
    private boolean hasManagerRole(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        
        if (currentUser == null) {
            return false; // Chưa đăng nhập hoặc Session hết hạn
        }

        // BƯỚC 2: KIỂM TRA ROLE MANAGER
        RoleDBContext roleDB = new RoleDBContext();
        // Giả định User Model có hàm getId() trả về uid
        ArrayList<Role> roles = roleDB.getByUserId(currentUser.getId()); 
        
        for (Role role : roles) {
            if (role.getName().equalsIgnoreCase("IT Head")) { 
                return true;    
            }
        }
        
        return false; // Không có Role Manager
    }

    /** Phương thức lấy dữ liệu Agenda Data tổng hợp từ DB cho TẤT CẢ nhân viên. */
    private AgendaData getAgendaDataForAllEmployees(Date startDate, Date endDate) {
        EmployeeDBContext empDB = new EmployeeDBContext();
        AgendaDBContext agendaDB = new AgendaDBContext();

        // Lấy TẤT CẢ nhân viên (Sử dụng hàm list() đã bổ sung)
        ArrayList<Employee> employees = empDB.list(); 
        
        // Lấy TẤT CẢ đơn nghỉ phép đã duyệt (Sử dụng hàm đã sửa đổi)
        ArrayList<RequestForLeave> requests = agendaDB.getApprovedRequests(startDate, endDate); 
        
        if (employees == null) employees = new ArrayList<>(); 
        
        Map<Integer, Map<Date, String>> employeeAgendaMap = new HashMap<>();

        // Logic xử lý khoảng ngày nghỉ phép thành trạng thái TỪNG NGÀY
        for (RequestForLeave rfl : requests) {
            Date requestFrom = rfl.getFrom();
            Date requestTo = rfl.getTo();
            
            int employeeId = rfl.getCreated_by() != null ? rfl.getCreated_by().getId() : -1; 
            if(employeeId == -1) continue; 
            
            List<Date> datesInRequest = DateUtil.getDatesBetween(requestFrom, requestTo);
            
            for (Date date : datesInRequest) {
                if (!date.before(startDate) && !date.after(endDate)) {
                    employeeAgendaMap
                            .computeIfAbsent(employeeId, k -> new HashMap<>())
                            .put(date, "L");
                }
            }
        }

        List<Date> allDates = DateUtil.getDatesBetween(startDate, endDate);
        
        return new AgendaData(employees, allDates, employeeAgendaMap);
    }
    
    // --------------------------------------------------------------------------

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Kiểm tra Quyền truy cập
        if (!hasManagerRole(request)) {
            // Xử lý lỗi 403 (Forbidden)
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "LỖI 403: Bạn không có quyền xem Agenda. Yêu cầu Role IT Head.");
            return;
        }
        
        // 2. Thiết lập khoảng ngày xem
        Date today = new Date(System.currentTimeMillis());
        
        Date startDate = request.getParameter("from") != null ? 
                         Date.valueOf(request.getParameter("from")) : 
                         Date.valueOf(today.toLocalDate().minusDays(3)); 
        
        Date endDate = request.getParameter("to") != null ? 
                       Date.valueOf(request.getParameter("to")) : 
                       Date.valueOf(today.toLocalDate().plusDays(3)); 

        // 3. Lấy dữ liệu tổng hợp
        AgendaData data = getAgendaDataForAllEmployees(startDate, endDate);
        
        // 4. Gửi dữ liệu về View
        request.setAttribute("agendaData", data);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        
        request.getRequestDispatcher("/view/agenda.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}