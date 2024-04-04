package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import database.DbConnect;

public class EmployeeDao extends ClientDao{
    
    public EmployeeDao() {
    }

    public int createEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        int generatedUserId = -1;
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT id FROM employe JOIN users ON users.id = employe.user_id WHERE email=?")) {
            prs.setString(1, employee.getEmail());
            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    return -2; // User already exists
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return -1; //error
        }
        
        
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("INSERT INTO users(name,email,role,password) VALUES(?,?,?,?)",
	          PreparedStatement.RETURN_GENERATED_KEYS)) {
	        prs.setString(1, employee.getName());
	        prs.setString(2, employee.getEmail());
	        prs.setString(3, employee.getRole());
                prs.setString(4, employee.getPassword());
	        
	        /*String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
	        prs.setString(5, hashedPassword);*/

	        int affectedRows = prs.executeUpdate();
	        
	        if (affectedRows > 0) {
	            // Retrieve the generated keys (in this case, the user ID)
	            try (ResultSet generatedKeys = prs.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    generatedUserId = generatedKeys.getInt(1);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
                //return -1;
	    }
            
        if (generatedUserId != -1) {
            try (Connection connection = DbConnect.getConnection();
                 PreparedStatement prs = connection.prepareStatement("INSERT INTO employe(user_id, salaire) VALUES(?,?)")) {
                prs.setInt(1, generatedUserId);
                prs.setDouble(2, employee.getSalary());

                int affectedRows = prs.executeUpdate();

                if (affectedRows <= 0) {
                    // Rollback the user creation if the employee creation fails
                    deleteUser(employee.getEmail());
                    generatedUserId = -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Rollback the user creation if the employee creation fails
                deleteUser(employee.getEmail());
                generatedUserId = -1;
            }
        }            
            
        return generatedUserId;
    }
    
  
    public int updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT id FROM users WHERE email=?")) {
            prs.setString(1, employee.getEmail());
            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("id") != employee.getId()) {
                    return -2; // User already exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET name=?, email=?, role=?, password=? WHERE id=?")) {
            updateStmt.setString(1, employee.getName());
            updateStmt.setString(2, employee.getEmail());
            updateStmt.setString(3, employee.getRole());
            updateStmt.setString(4, employee.getPassword());
            updateStmt.setInt(5, employee.getId());

            int affectedRows = updateStmt.executeUpdate();

            if (affectedRows < 1) {
                return -1; // not updated
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmtEmp = connection.prepareStatement("UPDATE employe SET salaire=? WHERE user_id=?")) {
            updateStmtEmp.setDouble(1, employee.getSalary());
            updateStmtEmp.setInt(2, employee.getId());

            int affectedRows = updateStmtEmp.executeUpdate();

            if (affectedRows < 1) {
                return -1; // not updated
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 1; // good
    }
    
    
    public int deleteEmployee(String userEmail) throws SQLException, ClassNotFoundException {
       try (Connection connection = DbConnect.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement prs = connection.prepareStatement("DELETE FROM employe WHERE user_id = (SELECT id FROM users WHERE email=?)")) {
                prs.setString(1, userEmail);

                int affectedRows = prs.executeUpdate();

                if (affectedRows > 0) {
                    int userDeleteResult = deleteUser(userEmail);

                    if (userDeleteResult == 1) {
                        connection.commit();
                    } else if (userDeleteResult == -2) {
                        connection.rollback();
                        return -2; // User not found
                    } else {
                        connection.rollback();
                        return -1; // Error during user deletion
                    }
                } else {
                    connection.rollback();
                    return -2; // User not found in employees table
                }
                
                return 1; // Successfully deleted
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return -1; // Error during deletion
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    public Employee getEmployeeByEmail(String employeeEmail) throws SQLException, ClassNotFoundException {
        Employee employee = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM employe JOIN users ON employe.user_id = users.id WHERE users.email = ?")) {
            prs.setString(1, employeeEmail);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    employee = new Employee();
                    employee.setId(resultSet.getInt("id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setRole(resultSet.getString("role"));
                    employee.setPassword(resultSet.getString("password"));
                    employee.setSalary(resultSet.getDouble("salaire"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }    
    
    
    public List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
        List<Employee> employeeList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT employe.id AS emp_id, users.id AS user_id, name, email, role, password, salaire FROM employe JOIN users ON employe.user_id = users.id");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("user_id"));
                employee.setName(resultSet.getString("name"));
                employee.setEmail(resultSet.getString("email"));
                employee.setRole(resultSet.getString("role"));
                employee.setPassword(resultSet.getString("password"));
                employee.setEmpId(resultSet.getInt("emp_id"));
                employee.setSalary(resultSet.getDouble("salaire"));

                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }    
    
    
}
