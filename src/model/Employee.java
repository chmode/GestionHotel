package model;

public class Employee extends User {
    protected int empId;
    protected Double salary;
    
        public Employee() {
        super();
    }

    public Employee(int id, int empId, String name, String email, String role, String password, Double salary, String jobTitle) {
        super(id, name, email,role,password);
        this.empId = empId;
        this.salary = salary;
    }

    public Employee(String name, String email, String role, String password, Double salary, String jobTitle) {
        super(name, email,role,password);
        this.salary = salary;        
    }
    
    
    public int getEmpId(){
        return this.empId;
    }    
    public Double getSalary(){
        return this.salary;
    }
    
    public void setEmpId(int empId){
        this.empId = empId;
    }    
    public void setSalary(Double salary){
        this.salary = salary;
    }
    
    @Override
    public String toString(){
        return "Employee [id=" + id + ",empId=" + empId + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + ", salary=" + salary + "]";
    }    
}
