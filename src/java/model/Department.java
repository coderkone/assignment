package model;

// Kế thừa BaseModel
public class Department extends BaseModel {
    private int id; // Tương ứng với cột 'did' trong bảng Employee
    private String name;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}