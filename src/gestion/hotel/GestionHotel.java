/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package gestion.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


import model.*;
import dao.*;
import java.sql.SQLException;

/**
 *
 * @author pc
 */
public class GestionHotel extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Login Page");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        launch(args);
        
        /*User usr = new User("a", "b","c","d");
        UserDao userOperations = new UserDao();
        try{
         if(userOperations.createUser(usr) == -2){
             System.out.println("deja existe");
         }else if(userOperations.createUser(usr) == 1){
             System.out.println("dooooneeeeeee");
         }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("llllllllll");
        }*/
    }
    
}
