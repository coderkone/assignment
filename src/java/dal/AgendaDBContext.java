package dal;

import model.RequestForLeave;
import java.sql.*;
import java.util.ArrayList;
import model.Employee;

public class AgendaDBContext extends DBContext<RequestForLeave> {

    // Bổ sung Phương thức 3: Lấy các đơn nghỉ phép ĐÃ DUYỆT
    public ArrayList<RequestForLeave> getApprovedRequests(int departmentId, Date startDate, Date endDate) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();
        
        // Điều kiện giao nhau của hai khoảng: r.[from] <= endDate VÀ r.[to] >= startDate
        String sql = """
            SELECT r.id, r.created_by_eid, r.[from], r.[to], r.[status]
            FROM RequestForLeave r 
            JOIN Employee e ON r.created_by_eid = e.eid
            WHERE e.did = ? 
            AND r.[status] = 1 -- Giả định 1 là Đã duyệt
            AND r.[from] <= ? 
            AND r.[to] >= ?
        """;
        
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, departmentId);
            stm.setDate(2, endDate);
            stm.setDate(3, startDate);
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                RequestForLeave rfl = new RequestForLeave();
                Employee employee = new Employee();
                employee.setId(rs.getInt("created_by_eid")); 
                rfl.setCreated_by(employee);
                rfl.setFrom(rs.getDate("from"));
                rfl.setTo(rs.getDate("to"));
                rfl.setStatus(rs.getInt("status"));
                requests.add(rfl);
            }
        } catch (SQLException ex) {
            // Log lỗi
        }
        return requests;
    }
    
    // ... (Cần override các hàm abstract khác)
    @Override public ArrayList<RequestForLeave> list() { return null; }
    @Override public RequestForLeave get(int id) { return null; }
    @Override public void insert(RequestForLeave model) { }
    @Override public void update(RequestForLeave model) { }
    @Override public void delete(RequestForLeave model) { }
}