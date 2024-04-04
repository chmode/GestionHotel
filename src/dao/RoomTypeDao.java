package dao;

import database.DbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.RoomTypes;


public class RoomTypeDao {
    
    public RoomTypeDao(){
        
    }
    
    public int createRoomType(RoomTypes roomType) throws ClassNotFoundException{
	    int generatedUserId = -1; 
	    
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM type_room WHERE label=?")){
                  prs.setString(1,roomType.getLabel());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()) {
                        return -2; // room type already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }        
        
            
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("INSERT INTO type_room(label,description,Nb_personne) VALUES(?,?,?)",
	          PreparedStatement.RETURN_GENERATED_KEYS)) {
                  prs.setString(1,roomType.getLabel());
                  prs.setString(2,roomType.getDescription());
                  prs.setInt(3,roomType.getNbrPersone());
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
    
    
    
    public int updateRoomType(RoomTypes roomType) throws SQLException, ClassNotFoundException {
        
	    try (Connection connection = DbConnect.getConnection();
                  PreparedStatement prs = connection.prepareStatement("SELECT id FROM type_room WHERE label=?")){
                  prs.setString(1,roomType.getLabel());
                try (ResultSet resultSet = prs.executeQuery()) {
                    if (resultSet.next()&& resultSet.getInt("id") != roomType.getId()) {
                        return -2; // roomType already exists
                    }
                }
            }catch (SQLException e) {
	        e.printStackTrace();
                return -1;
            }
            
            
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(
                     "UPDATE type_room SET label=?, description=?, Nb_personne=? WHERE id=?")) {
            updateStmt.setString(1, roomType.getLabel());
            updateStmt.setString(2, roomType.getDescription());
            updateStmt.setInt(3, roomType.getNbrPersone());
            updateStmt.setInt(4, roomType.getId());

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
    
    
    public int deleteUser(String label) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement("SELECT id FROM type_room WHERE label=?")) {
            selectStmt.setString(1, label);
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
             PreparedStatement deleteStmt = deleteConnection.prepareStatement("DELETE FROM type_room WHERE label=?")) {
            deleteStmt.setString(1, label);

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
    
    
    public RoomTypes getRoomType(String label) throws SQLException, ClassNotFoundException {
        RoomTypes roomType = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM type_room WHERE label=?")) {
            prs.setString(1, label);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    roomType = new RoomTypes();
                    roomType.setId(resultSet.getInt("id"));
                    roomType.setLabel(resultSet.getString("label"));
                    roomType.setDescription(resultSet.getString("description"));
                    roomType.setNbrPersone(resultSet.getInt("nbr_persone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomType;
    }      
    
    
    
    public List<RoomTypes> getAllRoomType() throws SQLException, ClassNotFoundException {
        List<RoomTypes> roomTypeList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM type_room");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                RoomTypes roomType = new RoomTypes();
                roomType.setId(resultSet.getInt("id"));
                roomType.setLabel(resultSet.getString("label"));
                roomType.setDescription(resultSet.getString("description"));
                roomType.setNbrPersone(resultSet.getInt("Nb_personne"));
                roomTypeList.add(roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomTypeList;
    }        
    
    
    
}
