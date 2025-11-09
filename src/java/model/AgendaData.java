package model;


import model.Employee;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class AgendaData {
    private List<Employee> employees;
    private List<Date> allDates;
    // Map<EmployeeId, Map<Date, Status>>: Lưu trữ trạng thái nghỉ phép
    private Map<Integer, Map<Date, String>> employeeAgendaMap; 

    public AgendaData(List<Employee> employees, List<Date> allDates, Map<Integer, Map<Date, String>> employeeAgendaMap) {
        this.employees = employees;
        this.allDates = allDates;
        this.employeeAgendaMap = employeeAgendaMap;
    }

    public List<Employee> getEmployees() { return employees; }
    public List<Date> getAllDates() { return allDates; }
    public Map<Integer, Map<Date, String>> getEmployeeAgendaMap() { return employeeAgendaMap; }
}