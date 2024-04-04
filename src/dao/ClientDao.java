package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Client;
import database.DbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import model.Receptionist;
import model.User;

public class ClientDao extends UserDao{
    
    public ClientDao() {
        
    }
    
    public int createClient(User user) throws SQLException, ClassNotFoundException {
        int generatedUserId = createUser(user);
        
        if(generatedUserId == -2){
            return -2; //User already exists
        }

        if (generatedUserId != -1) {
            try (Connection connection = DbConnect.getConnection()) {
                //connection.setAutoCommit(false);

                try (PreparedStatement prs = connection.prepareStatement(
                        "INSERT INTO clients(user_id) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    prs.setInt(1, generatedUserId);

                    int affectedRows = prs.executeUpdate();

                    if (affectedRows <= 0) {
                        //connection.rollback();
                        generatedUserId = -1;
                    } /*else {
                        connection.commit();
                    }*/
                } catch (SQLException e) {
                    connection.rollback();
                    e.printStackTrace();
                    generatedUserId = -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                generatedUserId = -1;
            }
        }

        return generatedUserId;
    }
    
    public int deleteClient(String userEmail) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbConnect.getConnection()) {
            //connection.setAutoCommit(false);

            try (PreparedStatement prs = connection.prepareStatement("DELETE FROM clients WHERE user_id = (SELECT id FROM users WHERE email=?)")) {
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
                    //connection.rollback();
                    return -2; // User not found in clients table
                }
                
                //return 1; // Successfully deleted
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
    
    
    public List<Client> getAllClients() throws SQLException, ClassNotFoundException {
        List<Client> clientList = new ArrayList<>();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT clients.id AS client_id, users.* FROM clients JOIN users ON clients.user_id = users.id");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("client_id"));
                client.setName(resultSet.getString("name"));
                client.setEmail(resultSet.getString("email"));
                client.setRole(resultSet.getString("role"));
                client.setPassword(resultSet.getString("password"));

                clientList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientList;
    }
    
    
    public Client getClientByEmail(String userEmail) throws SQLException, ClassNotFoundException {
        Client client = null;

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM users  WHERE email =? AND role='client';")) {
            prs.setString(1, userEmail);

            try (ResultSet resultSet = prs.executeQuery()) {
                if (resultSet.next()) {
                    client = new Client();
                    client.setId(resultSet.getInt("id"));
                    client.setName(resultSet.getString("name"));
                    client.setEmail(resultSet.getString("email"));
                    client.setRole(resultSet.getString("role"));
                    client.setPassword(resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return client;
    }
    
    public int updateClient(Client client) throws SQLException, ClassNotFoundException{
        return updateUser((User)client);
    }

    public ObservableList<Client> getAllClientsObservable() throws SQLException, ClassNotFoundException {
        ObservableList<Client> clientsList = FXCollections.observableArrayList();

        try (Connection connection = DbConnect.getConnection();
             PreparedStatement prs = connection.prepareStatement("SELECT * FROM users WHERE role='client';");
             ResultSet resultSet = prs.executeQuery()) {

            while (resultSet.next()) {
                Client cli = new Client();
                cli.setId(resultSet.getInt("id"));
                cli.setName(resultSet.getString("name"));
                cli.setEmail(resultSet.getString("email"));
                cli.setRole(resultSet.getString("role"));
                cli.setPassword(resultSet.getString("password"));

                clientsList.add(cli);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientsList;
    }

}
