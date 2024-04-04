package control;

import dao.ClientDao;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import model.Client;

public class clientsMang implements Initializable  {

    @FXML
    TextField nameIn;
    @FXML
    TextField emailIn;
    @FXML
    TextField updateEmail;
    @FXML
    TableColumn ID;
    @FXML
    TableColumn NAME;
    @FXML
    TableColumn EMAIL;
    @FXML
    TableView TABLE;
    @FXML
     Label errmsg;

    ClientDao cli = new ClientDao();
    public void find(Event e){
        //resetBtns();
        if(emailIn.getText().equals("")){
            System.out.println("Donner l'email à chercher.");
            errmsg.setText("Donner l'email à chercher.");
            return;
        }

        try{
            Client client = null;
            client = cli.getClientByEmail(emailIn.getText());
            if(client != null){
                nameIn.setText(client.getName());
            }else{
                System.out.println("Cet email n'existe pas.");
                errmsg.setText("Cet email n'existe pas");
            }
        }catch (SQLException | ClassNotFoundException err ){
            err.printStackTrace();
        }

    }

    public void add(Event e){
        if (!isRemplir()){
            System.out.println("Remplir tous les champs");
            errmsg.setText("Remplir tous les champs");
            return;
        }
        Client client = new Client();
        client.setEmail(emailIn.getText());
        client.setPassword(emailIn.getText());
        client.setName(nameIn.getText());
        client.setRole("client");
        try{
            int statu = cli.createClient(client);
            if(statu == -2){
                System.out.println("client avec cet email existe déjà");
                errmsg.setText("client avec cet email existe déjà");
            }else if(statu == 1){
                System.out.println("done");
                errmsg.setText("done");
                resetBtns();
                refrech();
            }
        }catch(SQLException | ClassNotFoundException err){
            err.printStackTrace();
        }
    }

    public void delete(Event e){
        //resetBtns();
        if(emailIn.getText().equals("")){
            System.out.println("Donner l'email à supprimer");
            errmsg.setText("Donner l'email à supprimer");
            return;
        }

        try{
            int statu = cli.deleteClient(emailIn.getText());
            if(statu == 1){
                System.out.println("dooonne");
                errmsg.setText("done");
                resetBtns();
                refrech();
            }else if(statu == -2){
                System.out.println("Cet email n'existe pas");
                errmsg.setText("Cet email n'existe pas");
            }
        }catch (SQLException | ClassNotFoundException err ){
            err.printStackTrace();
        }

    }

    public void update(Event e){
        if(updateEmail.getText().equals("")){
            System.out.println("Donner l'email à mettre à jour");
            errmsg.setText("Donner l'email à mettre à jour");
            return;
        }
        Client client = null;
        try{
            client = cli.getClientByEmail(updateEmail.getText());
            if(client == null){
                System.out.println("Cet email n'existe pas");
                errmsg.setText("Cet email n'existe pas");
            }else{
                client.setEmail(emailIn.getText());
                client.setName(nameIn.getText());
                int statu = cli.updateClient(client);
                if(statu == -2){
                    System.out.println("client avec cet email existe déjà");
                    errmsg.setText("client avec cet email existe déjà");
                }else if(statu == 1){
                    System.out.println("donnnne");
                    errmsg.setText("done");
                    resetBtns();
                    refrech();
                }
            }
        }catch (SQLException | ClassNotFoundException err){
            err.printStackTrace();
        }
    }

    public void resetBtns(){
        nameIn.setText("");
        emailIn.setText("");
        errmsg.setText("");
    }

    public boolean isRemplir(){
        if(nameIn.getText().equals("") | emailIn.getText().equals("")){
            return false;
        }
        return true;
    }
    public void rtn(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void logout(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrech();
    }
    public void refrech(){
        ID.setCellValueFactory((new PropertyValueFactory<>("id")));
        NAME.setCellValueFactory((new PropertyValueFactory<>("name")));
        EMAIL.setCellValueFactory((new PropertyValueFactory<>("email")));
        try {
            TABLE.setItems(cli.getAllClientsObservable());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        errmsg.setText("");
    }
}
