package dao;

import database.DbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Room;


public class RoomDao {
    
    public RoomDao(){
        
    }
    
    public int createRoom(Room room) throws ClassNotFoundException{
	    int generatedUserId = -1; 
	    
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM room WHERE matricule=?")){
                  prs.setInt(1,room.getMatricule());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()) {
                        return -2; // room already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }        
        
            
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("INSERT INTO room(matricule,status,type) VALUES(?,?,?)",
	          PreparedStatement.RETURN_GENERATED_KEYS)) {
                  prs.setInt(1,room.getMatricule());
                  prs.setBoolean(2,room.getStatus());
                  prs.setInt(3,room.getTypeId());
	        /*String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
	        prs.setString(5, hashedPassword);*/

	        int affectedRows = prs.executeUpdate();
	        
	        if (affectedRows > 0) {
	            // Retrieve the generated keys
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
    
    
    
    public int updateRoom(Room room) throws SQLException, ClassNotFoundException {
        
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM room WHERE matricule=?")){
                  prs.setInt(1,room.getMatricule());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()&& resultSet.getInt("id") != room.getId()) {
                        return -2; // room already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }
            
            
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(
                     "UPDATE room SET matricule=?, status=?, type=? WHERE id=?")) {
            updateStmt.setInt(1, room.getMatricule());
            updateStmt.setBoolean(2, room.getStatus());
            updateStmt.setInt(3, room.getTypeId());
            updateStmt.setInt(4, room.getId());

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
    
    
    public int deleteRoom(int matricule) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement("SELECT id FROM room WHERE matricule=?")) {
            selectStmt.setInt(1, matricule);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (!resultSet.next()) {
                    return -2; // roomType not exist
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // not deleted
        }

        try (Connection deleteConnection = DbConnect.getConnection();
             PreparedStatement deleteStmt = deleteConnection.prepareStatement("DELETE FROM room WHERE matricule=?")) {
            deleteStmt.setInt(1, matricule);

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
    
    
    public Room getRoom(int matricule) throws SQLException, ClassNotFoundException {
        Room room = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM room WHERE matricule=?")) {
            prs.setInt(1, matricule);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    room = new Room();
                    room.setId(resultSet.getInt("id"));
                    room.setMatricule(resultSet.getInt("matricule"));
                    room.setStatus(resultSet.getBoolean("status"));
                    room.setTypeId(resultSet.getInt("type"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return room;
    }      
    
    
    
    public List<Room> getAllRoom() throws SQLException, ClassNotFoundException {
        List<Room> roomList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM room");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Room room = new Room();
                    room.setId(resultSet.getInt("id"));
                    room.setMatricule(resultSet.getInt("matricule"));
                    room.setStatus(resultSet.getBoolean("status"));
                    room.setTypeId(resultSet.getInt("type"));
                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomList;
    }


    public ObservableList<Room> getAllClientsObservable() throws SQLException, ClassNotFoundException {
        ObservableList<Room> roomList = FXCollections.observableArrayList();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM room;");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Room room = new Room();
                room.setId(resultSet.getInt("id"));
                room.setTypeId(resultSet.getInt("type"));
                room.setMatricule(resultSet.getInt("matricule"));
                room.setStatus(resultSet.getBoolean("status"));

                roomList.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomList;
    }
    
    
    
}
