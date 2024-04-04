package model;


public class Receptionist extends Employee{
    
    public Receptionist() {
        super();
    }

    public Receptionist(int id, int empId, String name, String email, String role, String password, Double salary, String jobTitle) {
        super(id, empId, name, email, role, password, salary, jobTitle);
    }

    public Receptionist(String name, String email, String role, String password, Double salary, String jobTitle) {
        super(name, email, role, password, salary, jobTitle);
    }
    
    

    @Override
    public String toString() {
    return "Receptionist [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role
            + ", password=" + password + ", salary=" + salary + "]";
        }

    
}
