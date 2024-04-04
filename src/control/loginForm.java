package control;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.SQLException;
import dao.LoginDao;

public class loginForm {

    @FXML
    TextField emailIn;
    @FXML
    PasswordField pswdIn;
    @FXML
    Label errMsg;
    @FXML
    Button signinBtn;
    @FXML
    RadioButton admin;
    @FXML
    RadioButton receptionist;

    public void loginCheck(Event e) throws SQLException, ClassNotFoundException{
        if(emailIn.getText().equals("") || pswdIn.getText().equals("")){
            errMsg.setText("remplir tous les champs");
        }else if(!admin.isSelected() && !receptionist.isSelected()){
            errMsg.setText("selection voute role");
        }else{
            LoginDao loginDao = new LoginDao();
            if(admin.isSelected()){
                if(loginDao.adminCheck(emailIn.getText(),pswdIn.getText())){
                    Node node = (Node) e.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.close();

                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    errMsg.setText("email ou mots de pass incorrecte");
                }
            }else{
                if(loginDao.receptionistCheck(emailIn.getText(),pswdIn.getText())){
                    Node node = (Node) e.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.close();

                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/reservationMangREC.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    errMsg.setText("email ou mots de pass incorrecte");
                }
            }
        }

    }


}
