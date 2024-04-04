package dao;

import java.sql.Connection;
import database.DbConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;


public class UserDao {
    public UserDao(){
        
    }
    
	public int createUser(User user) throws SQLException, ClassNotFoundException {
	    int generatedUserId = -1; 
	    
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM users WHERE email=?")){
                prs.setString(1,user.getEmail());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()) {
                        return -2; // User already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }
            
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("INSERT INTO users(name,email,role,password) VALUES(?,?,?,?)",
	          PreparedStatement.RETURN_GENERATED_KEYS)) {
	        prs.setString(1, user.getName());
	        prs.setString(2, user.getEmail());
	        prs.setString(3, user.getRole());
	        prs.setString(4, user.getPassword());
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
	    
	    return generatedUserId;
	}  
        
        
        
    public int updateUser(User user) throws SQLException, ClassNotFoundException {
        
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM users WHERE email=?")){
                prs.setString(1,user.getEmail());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()&& resultSet.getInt("id") != user.getId()) {
                        return -2; // User already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }
            
            
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(
                     "UPDATE users SET name=?, email=?, role=?, password=? WHERE id=?")) {
            updateStmt.setString(1, user.getName());
            updateStmt.setString(2, user.getEmail());
            updateStmt.setString(3, user.getRole());
            //updateStmt.setString(4, user.getRole());
            updateStmt.setString(4, user.getPassword()); 
            updateStmt.setInt(5, user.getId());

            int affectedRows = updateStmt.executeUpdate();

            if(affectedRows < 1){
                return -1; //not updated
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1; //good
    }        

    
    public int deleteUser(String userEmail) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement("SELECT id FROM users WHERE email=?")) {
            selectStmt.setString(1, userEmail);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (!resultSet.next()) {
                    return -2; // User not exist
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // not deleted
        }

        try (Connection deleteConnection = DbConnect.getConnection();
             PreparedStatement deleteStmt = deleteConnection.prepareStatement("DELETE FROM users WHERE email=?")) {
            deleteStmt.setString(1, userEmail);

            int affectedRows = deleteStmt.executeUpdate();

            if (affectedRows < 1) {
                return -1; // Not deleted
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // not deleted
        }

        return 1; //good
    }
    
    
    public User getUser(String userEmail) throws SQLException, ClassNotFoundException {
        User user = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM users WHERE email=?")) {
            prs.setString(1, userEmail);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setRole(resultSet.getString("role"));
                    user.setPassword(resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }    
    
    
    public List<User> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setPassword(resultSet.getString("password"));

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }    
    
        
}
