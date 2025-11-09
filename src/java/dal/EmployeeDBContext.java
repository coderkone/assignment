package dal;

import java.sql.*;
import model.Employee;
import dal.DBContext;
import java.util.ArrayList;
import java.util.List;
import model.BaseModel;

public class EmployeeDBContext extends DBContext {

    public Employee getByUserId(int uid) {
        String sql = """
            SELECT e.eid, e.ename, e.did, e.supervisorid
            FROM Employee e
            JOIN Enrollment en ON en.eid = e.eid
            WHERE en.uid = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                // bạn có thể gán thêm division hoặc supervisor nếu cần
                return e;
            }
        } catch (SQLException ex) {
            System.out.println("Error at EmployeeDBContext.getByUserId(): " + ex.getMessage());
        }
        return null;
    }

    // Trong EmployeeDBContext.java (giả định lớp này kế thừa DBContext<Employee>)
    // Trong EmployeeDBContext.java
// Bổ sung Phương thức 1: Lấy ID phòng ban của Trưởng phòng
    public int getDepartmentIdByEmployeeId(int employeeId) {
        String sql = "SELECT did FROM Employee WHERE eid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("did");
            }
        } catch (SQLException ex) {
            // Log lỗi
        }
        return -1;
    }

// Bổ sung Phương thức 2: Lấy danh sách nhân viên trong cùng phòng
    public ArrayList<Employee> getEmployeesByDepartmentId(int departmentId) {
        ArrayList<Employee> employees = new ArrayList<>();
        // Giả định bảng Employee có cột did
        String sql = "SELECT eid, ename FROM Employee WHERE did = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                employees.add(e);
            }
        } catch (SQLException ex) {
            // Log lỗi
        }
        return employees;
    }

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
}
