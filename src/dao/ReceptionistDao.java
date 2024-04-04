package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import database.DbConnect;
import model.Employee;
import model.Receptionist;

public class ReceptionistDao extends EmployeeDao{
    
    public ReceptionistDao(){
        
    }

    public int createEmployee(Receptionist rec) throws SQLException, ClassNotFoundException {
        int generatedUserId = -1;
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT users.id FROM employe JOIN users ON users.id = employe.user_id WHERE email=? AND role='receptionists'")) {
            prs.setString(1, rec.getEmail());
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
             PreparedStatement prs = connection.prepareStatement("INSERT INTO users(name,email,password,role) VALUES(?,?,?,'receptionists')",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            prs.setString(1, rec.getName());
            prs.setString(2, rec.getEmail());
            prs.setString(3, rec.getPassword());

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
                prs.setDouble(2, rec.getSalary());

                int affectedRows = prs.executeUpdate();

                if (affectedRows <= 0) {
                    // Rollback the user creation if the employee creation fails
                    deleteUser(rec.getEmail());
                    generatedUserId = -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Rollback the user creation if the employee creation fails
                deleteUser(rec.getEmail());
                generatedUserId = -1;
            }
        }

        return generatedUserId;
    }


    public int updateEmployee(Receptionist rec) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT id FROM users WHERE email=? AND role='receptionists'")) {
            prs.setString(1, rec.getEmail());
            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("id") != rec.getId()) {
                    return -2; // User already exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement("UPDATE users SET name=?, email=? WHERE id=? ")) {
            updateStmt.setString(1, rec.getName());
            updateStmt.setString(2, rec.getEmail());
            updateStmt.setInt(3, rec.getId());

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
            updateStmtEmp.setDouble(1, rec.getSalary());
            updateStmtEmp.setInt(2, rec.getId());

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
            //connection.setAutoCommit(false);

            try (PreparedStatement prs = connection.prepareStatement("DELETE FROM employe WHERE user_id = (SELECT id FROM users WHERE email=? AND role='receptionists')")) {
                prs.setString(1, userEmail);

                int affectedRows = prs.executeUpdate();

                if (affectedRows > 0) {
                    int userDeleteResult = deleteUser(userEmail);

                    if (userDeleteResult == 1) {
                        //connection.commit();
                        return 1;
                    } else if (userDeleteResult == -2) {
                        //connection.rollback();
                        return -2; // User not found
                    } else {
                        //connection.rollback();
                        return -1; // Error during user deletion
                    }
                } else {
                   // connection.rollback();
                    return -2; // User not found in employees table
                }

               // return 1; // Successfully deleted
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



    public Receptionist getEmployeeByEmail(String employeeEmail) throws SQLException, ClassNotFoundException {
        Receptionist rec = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT users.id as userId,users.name,users.email,users.password,users.role, employe.* FROM employe JOIN users ON employe.user_id = users.id WHERE users.email =? AND role='receptionists';")) {
            prs.setString(1, employeeEmail);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    rec = new Receptionist();
                    rec.setId(resultSet.getInt("userId"));
                    rec.setName(resultSet.getString("name"));
                    rec.setEmail(resultSet.getString("email"));
                    rec.setRole(resultSet.getString("role"));
                    rec.setPassword(resultSet.getString("password"));
                    rec.setSalary(resultSet.getDouble("salaire"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rec;
    }


    public List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
        List<Employee> ReceptionistList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT employe.id AS emp_id, users.id AS user_id, name, email, role, password, salaire FROM employe JOIN users ON employe.user_id = users.id WHERE role='receptionists'");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Receptionist rec = new Receptionist();
                rec.setId(resultSet.getInt("user_id"));
                rec.setName(resultSet.getString("name"));
                rec.setEmail(resultSet.getString("email"));
                rec.setRole(resultSet.getString("role"));
                rec.setPassword(resultSet.getString("password"));
                rec.setEmpId(resultSet.getInt("emp_id"));
                rec.setSalary(resultSet.getDouble("salaire"));

                ReceptionistList.add(rec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ReceptionistList;
    }


    public ObservableList<Receptionist> getAllEmployeesObservable() throws SQLException, ClassNotFoundException {
        ObservableList<Receptionist> receptionistList = FXCollections.observableArrayList();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT employe.id AS emp_id, users.id AS user_id, name, email, role, password, salaire FROM employe JOIN users ON employe.user_id = users.id WHERE role='receptionists'");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Receptionist rec = new Receptionist();
                rec.setId(resultSet.getInt("user_id"));
                rec.setName(resultSet.getString("name"));
                rec.setEmail(resultSet.getString("email"));
                rec.setRole(resultSet.getString("role"));
                rec.setPassword(resultSet.getString("password"));
                rec.setEmpId(resultSet.getInt("emp_id"));
                rec.setSalary(resultSet.getDouble("salaire"));

                receptionistList.add(rec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receptionistList;
    }

}
