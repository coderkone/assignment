package controller;

import controller.division.DateUtil;
import dal.AgendaDBContext;
import dal.EmployeeDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import model.RequestForLeave;
import model.iam.User;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.AgendaData;
;

public class AgendaController extends HttpServlet {
    
    private int getEmployeeDepartmentId(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        
        if (currentUser != null && currentUser.getEmployee() != null) {
            int employeeId = currentUser.getEmployee().getId();
            EmployeeDBContext empDB = new EmployeeDBContext();
            return empDB.getDepartmentIdByEmployeeId(employeeId);
        }
        return -1;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int departmentId = getEmployeeDepartmentId(request); 
        
        if (departmentId == -1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập Agenda này hoặc chưa xác định phòng ban.");
            return;
        }
        
        // Thiết lập Ngày Mặc định (Ví dụ: 7 ngày quanh ngày hiện tại)
        Date today = new Date(System.currentTimeMillis());
        Date startDate = request.getParameter("from") != null ? Date.valueOf(request.getParameter("from")) : Date.valueOf(today.toLocalDate().minusDays(3)); 
        Date endDate = request.getParameter("to") != null ? Date.valueOf(request.getParameter("to")) : Date.valueOf(today.toLocalDate().plusDays(3)); 

        AgendaData data = getAgendaData(departmentId, startDate, endDate);
        
        request.setAttribute("agendaData", data);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        
        request.getRequestDispatcher("/view/agenda.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private AgendaData getAgendaData(int departmentId, Date startDate, Date endDate) {
        EmployeeDBContext empDB = new EmployeeDBContext();
        AgendaDBContext agendaDB = new AgendaDBContext();

        ArrayList<Employee> employees = empDB.getEmployeesByDepartmentId(departmentId);
        ArrayList<RequestForLeave> requests = agendaDB.getApprovedRequests(departmentId, startDate, endDate);

        // Map<EmployeeId, Map<Date, Status>>
        Map<Integer, Map<Date, String>> employeeAgendaMap = new HashMap<>();

        // Logic xử lý khoảng ngày nghỉ phép thành từng ngày
        for (RequestForLeave rfl : requests) {
            Date requestFrom = rfl.getFrom();
            Date requestTo = rfl.getTo();
            int employeeId = rfl.getCreated_by().getId(); 
            
            // Duyệt qua TẤT CẢ các ngày trong khoảng nghỉ phép của đơn
            List<Date> datesInRequest = DateUtil.getDatesBetween(requestFrom, requestTo);
            
            for (Date date : datesInRequest) {
                // Chỉ lưu vào Map nếu ngày nghỉ phép nằm trong khoảng Agenda đang xem
                if (!date.before(startDate) && !date.after(endDate)) {
                    employeeAgendaMap
                            .computeIfAbsent(employeeId, k -> new HashMap<>())
                            .put(date, "L"); // L = Leave (Nghỉ phép)
                }
            }
        }

        List<Date> allDates = DateUtil.getDatesBetween(startDate, endDate);
        
        return new AgendaData(employees, allDates, employeeAgendaMap);
    }
}