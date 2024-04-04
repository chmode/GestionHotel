package model;


public class Admin extends Employee {
    
    public Admin() {
        super();
    }

    public Admin(int id, int empId, String name, String email, String role, String password, Double salary, String jobTitle) {
        super(id, empId, name, email, role, password, salary, jobTitle);
    }

    public Admin(String name, String email, String role, String password, Double salary, String jobTitle) {
        super(name, email, role, password, salary, jobTitle);
    }
    
    

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", email=" + email +", role=" + role
				+ ", password=" + password + "salary=" + salary + "]";
	}    
    
}
