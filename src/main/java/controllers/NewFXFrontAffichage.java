/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author asus
 */
public class NewFXFrontAffichage extends Application {
   
    @Override
    public void start(Stage primaryStage) throws IOException {
               Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListProductsAffichage.fxml")));
        Scene scene = new Scene(root,950,650); 
        primaryStage.setTitle("Gérer dons");
        //primaryStage.setIconified(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
