/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategorie_done file, choose Tools | Temcategorie_dones
 * and open the temcategorie_done in the editor.
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
public class NewFXAdmin extends Application {
   
    @Override
    public void start(Stage primaryStage) throws IOException {
               Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashCategories.fxml")));
        Scene scene = new Scene(root,950,650); 
        primaryStage.setTitle("Gérer categorie_dons");
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
