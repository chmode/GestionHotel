package model;

public class Client extends User {

    public Client() {
        super();
    }

    public Client(int id, String name, String email, String role, String password) {
        super(id, name, email,role,password);
    }

    public Client(String name, String email, String role, String password) {
        super(name, email,role,password);
    }
    
    

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", email=" + email +", role=" + role
				+ ", password=" + password + "]";
	}
    
}
