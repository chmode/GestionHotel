package control;

import dao.ReservationDao;
import javafx.fxml.FXML;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class reservationMang implements Initializable {

    ReservationDao reservationdao = new ReservationDao();
    @FXML
    TextField clientId;
    @FXML
    ComboBox nbrPersonne;
    @FXML
    ComboBox rommMatricule;
    @FXML
    DatePicker dateIn;
    @FXML
    DatePicker dateOut;
    @FXML
    TableView TABLE;
    @FXML
    TableColumn ID;
    @FXML
    TableColumn client_id;
    @FXML
    TableColumn date_in;
    @FXML
    TableColumn date_out;
    @FXML
    TableColumn room_id;



    public void rechercher(){
        //rommMatricule.setValue(null);
        rommMatricule.getItems().clear();
        if(dateOut.getValue()==null || dateIn.getValue()==null || nbrPersonne.getValue()==null){
            System.out.println("remplir tous les champs");
        }else{
            rommMatricule.setItems(reservationdao.availableRooms((Integer) nbrPersonne.getSelectionModel().getSelectedItem(), dateIn.getValue(), dateOut.getValue()));
        }
    }
    public void addReservationn() {
        if (isRemplir()) {
            System.out.println("remplir tous les champs");
            return;
        }

        int statu = reservationdao.addReservation(clientId.getText(), dateIn.getValue(), dateOut.getValue(),(Integer) rommMatricule.getSelectionModel().getSelectedItem());

        switch (statu) {
            case -3:
                System.out.println("date illogique");
                break;
            case -2:
                System.out.println("pas de chambre disponible");
                break;
            case 1:
                System.out.println("donnne");
                resetBtns();
                refrech(); // Fix the typo here
                break;
            case -4:
                System.out.println("chambre  pas disponible");
                break;
            case -5:
                System.out.println("date pas disponible");
                break;
            case -22:
                System.out.println("chambre nexeste pas");
                break;
            case -44:
                System.out.println("pas de client pour ce email");
                break;
            default:
                System.out.println("erreur de creation");
                break;
        }
    }



    public void resetBtns(){
        nbrPersonne.setValue(null);
        //rommMatricule.setValue(null);
        rommMatricule.getItems().clear();
        clientId.setText("");
        dateIn.setValue(null);
        dateOut.setValue(null);
    }

    public boolean isRemplir(){
        if(clientId.getText().equals("") || nbrPersonne.getValue()==null || rommMatricule.getValue()==null  || dateIn.getValue()==null || dateOut.getValue()==null){
            return true;
        }
        return false;
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

        /*statutIn.setItems(sts);
        typeIN.setItems(typs());*/
        nbrPersonne.setItems(reservationdao.loadNBR());

        ID.setCellValueFactory((new PropertyValueFactory<>("id")));
        client_id.setCellValueFactory((new PropertyValueFactory<>("clientId")));
        room_id.setCellValueFactory((new PropertyValueFactory<>("roomId")));
        date_in.setCellValueFactory((new PropertyValueFactory<>("checkInDate")));
        date_out.setCellValueFactory((new PropertyValueFactory<>("checkOutDate")));
        try {
            TABLE.setItems(reservationdao.getAllReservationsObservable());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
