package model;

public class User {
	protected int id;
	protected String name;
	protected String email;
	protected String role;
	protected String password;
	
	public User() {
		super();
	}
	
	public User(int id, String name, String email ,String role,String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.password = password;
	}

	public User(String name, String email ,String role, String password) {
		super();
		this.name = name;
		this.email = email;
		this.role = role;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

        @Override
        public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + ", password=" + password + "]";
        }

	
	

}
