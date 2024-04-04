package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
	static Connection  connection = null;
        
            private DbConnect() {
            }
	
	public static Connection getConnection() throws ClassNotFoundException,SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_java_db?" + "user=root&password=");
                        System.out.println("Database connect by succee");
                    } catch (SQLException ex) {
                        // handle any errors
                        System.out.println("SQLException: " + ex.getMessage());
                        System.out.println("SQLState: " + ex.getSQLState());
                        System.out.println("VendorError: " + ex.getErrorCode());
                    }
                    return connection;
	}
}