package control;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;public class adminPage {

    @FXML
    Button logoutBtn;

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

    public void receptionistsMng(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/receptionistsMang.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void clientsMng(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/clientsMang.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void roomMng(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/chambreMang.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reservationMng(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/reservationMang.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}