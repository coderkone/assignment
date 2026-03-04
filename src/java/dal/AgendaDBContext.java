package dal;

import model.RequestForLeave;
import java.sql.*;
import java.util.ArrayList;
import model.Employee;

public class AgendaDBContext extends DBContext<RequestForLeave> {

    /**
     * Lấy các Đơn nghỉ phép ĐÃ DUYỆT (status=1) của TẤT CẢ nhân viên.
     */
    public ArrayList<RequestForLeave> getApprovedRequests(Date startDate, Date endDate) {
        ArrayList<RequestForLeave> requests = new ArrayList<>();

        // Đã bỏ điều kiện WHERE e.did = ?
        String sql = """
        SELECT r.id, r.created_by_eid, r.[from], r.[to], r.[status]
        FROM RequestForLeave r 
        JOIN Employee e ON r.created_by_eid = e.eid
        WHERE r.[status] = 1 
        AND r.[from] <= ? 
        AND r.[to] >= ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            // Chỉ cần truyền startDate và endDate
            stm.setDate(1, endDate);
            stm.setDate(2, startDate);

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
    @Override
    public ArrayList<RequestForLeave> list() {
        return null;
    }

    @Override
    public RequestForLeave get(int id) {
        return null;
    }

    @Override
    public void insert(RequestForLeave model) {
    }

    @Override
    public void update(RequestForLeave model) {
    }

    @Override
    public void delete(RequestForLeave model) {
    }
}
