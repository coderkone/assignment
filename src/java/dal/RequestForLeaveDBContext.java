/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.RequestForLeave;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author sonnt
 */
public class RequestForLeaveDBContext extends DBContext<RequestForLeave> {

    public ArrayList<RequestForLeave> getByEmployeeAndSubodiaries(int eid) {
        ArrayList<RequestForLeave> rfls = new ArrayList<>();
        try {
            String sql = """
                                     WITH Org AS (
                                     \t-- get current employee - eid = @eid
                                     \tSELECT *, 0 as lvl FROM Employee e WHERE e.eid = ?
                                     \t
                                     \tUNION ALL
                                     \t-- expand to other subodinaries
                                     \tSELECT c.*,o.lvl + 1 as lvl FROM Employee c JOIN Org o ON c.supervisorid = o.eid
                                     )
                                     SELECT
                                     \t\t[rid]
                                     \t  ,[created_by]
                                     \t  ,e.ename as [created_name]
                                           ,[created_time]
                                           ,[from]
                                           ,[to]
                                           ,[reason]
                                           ,[status]
                                           ,[processed_by]
                                     \t  ,p.ename as [processed_name]
                                     FROM Org e INNER JOIN [RequestForLeave] r ON e.eid = r.created_by
                                     \t\t\tLEFT JOIN Employee p ON p.eid = r.processed_by""";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                RequestForLeave rfl = new RequestForLeave();

                rfl.setId(rs.getInt("rid"));
                rfl.setCreated_time(rs.getTimestamp("created_time"));
                rfl.setFrom(rs.getDate("from"));
                rfl.setTo(rs.getDate("to"));
                rfl.setReason(rs.getString("reason"));
                rfl.setStatus(rs.getInt("status"));

                Employee created_by = new Employee();
                created_by.setId(rs.getInt("created_by"));
                created_by.setName(rs.getString("created_name"));
                rfl.setCreated_by(created_by);

                int processed_by_id = rs.getInt("processed_by");
                if (processed_by_id != 0) {
                    Employee processed_by = new Employee();
                    processed_by.setId(rs.getInt("processed_by"));
                    processed_by.setName(rs.getString("processed_name"));
                    rfl.setProcessed_by(processed_by);
                }

                rfls.add(rfl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rfls;
    }

    @Override
    public void insert(RequestForLeave model) {
        try {
            String sql = """
            INSERT INTO [RequestForLeave]
                ([created_by], [created_time], [from], [to], [reason], [status], [processed_by])
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getCreated_by().getId());
            stm.setTimestamp(2, (Timestamp) model.getCreated_time());
            stm.setDate(3, model.getFrom());
            stm.setDate(4, model.getTo());
            stm.setString(5, model.getReason());
            stm.setInt(6, model.getStatus());

            if (model.getProcessed_by() != null) {
                stm.setInt(7, model.getProcessed_by().getId());
            } else {
                stm.setNull(7, java.sql.Types.INTEGER);
            }

            stm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public RequestForLeave getById(int rid) {
        RequestForLeave rfl = null;
        try {
            String sql = """
            SELECT r.rid, r.created_by, e.ename AS created_name,
                   r.created_time, r.[from], r.[to],
                   r.reason, r.status, r.processed_by, p.ename AS processed_name
            FROM RequestForLeave r
            JOIN Employee e ON e.eid = r.created_by
            LEFT JOIN Employee p ON p.eid = r.processed_by
            WHERE r.rid = ?
        """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, rid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                rfl = new RequestForLeave();
                rfl.setId(rs.getInt("rid"));
                rfl.setCreated_time(rs.getTimestamp("created_time"));
                rfl.setFrom(rs.getDate("from"));
                rfl.setTo(rs.getDate("to"));
                rfl.setReason(rs.getString("reason"));
                rfl.setStatus(rs.getInt("status"));

                Employee createdBy = new Employee();
                createdBy.setId(rs.getInt("created_by"));
                createdBy.setName(rs.getString("created_name"));
                rfl.setCreated_by(createdBy);

                int pid = rs.getInt("processed_by");
                if (pid != 0) {
                    Employee processedBy = new Employee();
                    processedBy.setId(pid);
                    processedBy.setName(rs.getString("processed_name"));
                    rfl.setProcessed_by(processedBy);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rfl;
    }

    @Override
    public void delete(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void updateStatus(int rid, int status, int processedBy, String reason) {
        try {
            String sql = """
            UPDATE RequestForLeave
            SET status = ?, processed_by = ?, reason = ?
            WHERE rid = ?
        """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, status);
            stm.setInt(2, processedBy);
            stm.setString(3, reason);
            stm.setInt(4, rid);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public ArrayList<RequestForLeave> searchByEmployeeAndSubordinates(
            int userId, String searchValue, Integer status, Date from, Date to) {
        ArrayList<RequestForLeave> list = new ArrayList<>();
        String sql = """
        WITH RecursiveEmp AS (
            SELECT e.eid
            FROM Employee e
            JOIN Enrollment en ON e.eid = en.eid
            WHERE en.uid = ?
            UNION ALL
            SELECT e2.eid
            FROM Employee e2
            INNER JOIN RecursiveEmp re ON e2.supervisorid = re.eid
        )
        SELECT r.rid, r.reason, r.status, r.[from], r.[to],
               c.eid AS created_by, c.ename AS creator_name,
               p.eid AS processed_by, p.ename AS processor_name
        FROM RequestForLeave r
        JOIN Employee c ON r.created_by = c.eid
        LEFT JOIN Employee p ON r.processed_by = p.eid
        WHERE r.created_by IN (SELECT eid FROM RecursiveEmp)
          AND (
              ? IS NULL 
              OR c.ename LIKE ? 
              OR CAST(c.eid AS NVARCHAR) = ?
          )
          AND (? IS NULL OR r.status = ?)
          AND (? IS NULL OR r.[from] >= ?)
          AND (? IS NULL OR r.[to] <= ?)
        ORDER BY r.[from] DESC
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userId);

            stm.setString(2, searchValue);
            stm.setString(3, searchValue == null ? null : "%" + searchValue + "%");
            stm.setString(4, searchValue);

            stm.setObject(5, status);
            stm.setObject(6, status);

            stm.setDate(7, from);
            stm.setDate(8, from);

            stm.setDate(9, to);
            stm.setDate(10, to);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                RequestForLeave r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setReason(rs.getString("reason"));
                r.setStatus(rs.getInt("status"));
                r.setFrom(rs.getDate("from"));
                r.setTo(rs.getDate("to"));

                Employee creator = new Employee();
                creator.setId(rs.getInt("created_by"));
                creator.setName(rs.getString("creator_name"));
                r.setCreated_by(creator);

                Employee processor = new Employee();
                processor.setId(rs.getInt("processed_by"));
                processor.setName(rs.getString("processor_name"));
                r.setProcessed_by(processor);

                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<RequestForLeave> page(int pageindex, int pagesize) {
        ArrayList<RequestForLeave> list = new ArrayList<>();
        try {
            String sql = """
            SELECT r.rid, r.reason, r.status, r.[from], r.[to],
                   c.eid AS created_by, c.ename AS creator_name,
                   p.eid AS processed_by, p.ename AS processor_name
            FROM RequestForLeave r
            JOIN Employee c ON r.created_by = c.eid
            LEFT JOIN Employee p ON r.processed_by = p.eid
            ORDER BY r.rid
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, (pageindex - 1) * pagesize);
            stm.setInt(2, pagesize);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                RequestForLeave r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setReason(rs.getString("reason"));
                r.setStatus(rs.getInt("status"));
                r.setFrom(rs.getDate("from"));
                r.setTo(rs.getDate("to"));

                Employee createdBy = new Employee();
                createdBy.setId(rs.getInt("created_by"));
                createdBy.setName(rs.getString("creator_name"));
                r.setCreated_by(createdBy);

                Employee processedBy = new Employee();
                processedBy.setId(rs.getInt("processed_by"));
                processedBy.setName(rs.getString("processor_name"));
                r.setProcessed_by(processedBy);

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public RequestForLeave get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<RequestForLeave> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
