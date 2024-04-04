package dao;

import database.DbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {
    

    public LoginDao(){
    }
    
    public boolean adminCheck(String email, String pwd) throws ClassNotFoundException, SQLException{
	try (Connection connection = DbConnect.getConnection();
            PreparedStatement prs = connection.prepareStatement("SELECT * FROM users WHERE role='admin' AND email=? AND password=?")) {
            prs.setString(1, email);
            prs.setString(2, pwd);
            try (ResultSet results = prs.executeQuery()) {
                if (results.next()) {
                    /*Employee emp = new Employee();
                    emp.setId(results.getInt("id"));
                    emp.setEmail(results.getString("email"));
                    emp.setName(results.getString("name"));
                    emp.setPassword(results.getString("password"));
                    emp.setSalary(results.getFloat("salaire"));
                    emp.setRole(results.getString("role"));
                    return emp;*/
                    return true;
                }
            }
	} catch (SQLException e) {
                e.printStackTrace();
            }
            //return null;
        return false;
        
    }

    public boolean receptionistCheck(String email, String pwd) throws ClassNotFoundException, SQLException{
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM users WHERE role='receptionists' AND email=? AND password=?")) {
            prs.setString(1, email);
            prs.setString(2, pwd);
            try (ResultSet results = prs.executeQuery()) {
                if (results.next()) {
                    /*Employee emp = new Employee();
                    emp.setId(results.getInt("id"));
                    emp.setEmail(results.getString("email"));
                    emp.setName(results.getString("name"));
                    emp.setPassword(results.getString("password"));
                    emp.setSalary(results.getFloat("salaire"));
                    emp.setRole(results.getString("role"));
                    return emp;*/
                    return  true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return null;
        return false;

    }
    
}
