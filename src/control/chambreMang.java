package control;

import dao.RoomDao;
import database.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import model.Room;

public class chambreMang implements Initializable {

    @FXML
    ComboBox typeIN;
    @FXML
    TextField matriculeIn;
    @FXML
    ComboBox statutIn;
    @FXML
    TableView TABLE;
    @FXML
    TableColumn ID;
    @FXML
    TableColumn TYPE;
    @FXML
    TableColumn MATRICULE;
    @FXML
    TableColumn STATUT;
    @FXML
    Label errmsg;

    RoomDao romdao = new RoomDao();

    public void find(Event e){
        //resetBtns();
        if(matriculeIn.getText().equals("")){
            System.out.println("donner le matricule a cherche");
            errmsg.setText("Donner le matricule à chercher");
            return;
        }

        try{
            Room room = null;
            room = romdao.getRoom(Integer.parseInt(matriculeIn.getText()));
            if(room != null){
                typeIN.setValue(room.getTypeId());
                statutIn.setValue(Boolean.toString(room.getStatus()));
            }else{
                System.out.println("pas de cette metricule.");
                errmsg.setText("ce matricule n'existe pas");
            }
        }catch (SQLException | ClassNotFoundException err ){
            err.printStackTrace();
        }

    }

    ObservableList<Boolean> sts = FXCollections.observableArrayList(true, false);
    public void update(Event e){
        if(!isRemplir()){
            System.out.println("Remplir tous les champs");
            errmsg.setText("Remplir tous les champs");
            return;
        }
        Room room = null;
        try{
            room = romdao.getRoom(Integer.parseInt(matriculeIn.getText()));
            if(room == null){
                System.out.println("pas de cette matricule.");
                errmsg.setText("ce matricule n'existe pas");
            }else{
                room.setTypeId((Integer) typeIN.getSelectionModel().getSelectedItem());

                Object value = statutIn.getValue();
                if (value instanceof Boolean) {
                    room.setStatus((Boolean) value);
                } else {
                    System.out.println("Invalid value for status");
                    errmsg.setText("Invalid value for status");
                }


                int statu = romdao.updateRoom(room);
                if(statu == -2){
                    System.out.println("La chambre avec ce matricule existe déjà");
                    errmsg.setText("La chambre avec ce matricule existe déjà");
                }else if(statu == 1){
                    System.out.println("done");
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
        typeIN.setValue(null);
        statutIn.setValue(null);
        matriculeIn.setText("");
        errmsg.setText("");
    }

    public boolean isRemplir(){
        if(matriculeIn.getText().equals("") || statutIn.getValue()==null || typeIN.getValue()==null){
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


    public ObservableList<Integer> typs(){
            ObservableList<Integer> typeList = FXCollections.observableArrayList();

            try (Connection connection = DbConnect.getConnection();
                 PreparedStatement prs = connection.prepareStatement("SELECT * FROM type_room;");
                 ResultSet resultSet = prs.executeQuery()) {

                while (resultSet.next()) {
                    typeList.add(resultSet.getInt("id"));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return typeList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrech();
    }

    public void refrech(){
        statutIn.setItems(sts);
        typeIN.setItems(typs());

        ID.setCellValueFactory((new PropertyValueFactory<>("id")));
        TYPE.setCellValueFactory((new PropertyValueFactory<>("typeId")));
        MATRICULE.setCellValueFactory((new PropertyValueFactory<>("matricule")));
        STATUT.setCellValueFactory((new PropertyValueFactory<>("status")));
        try {
            TABLE.setItems(romdao.getAllClientsObservable());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        errmsg.setText("");
    }
}
