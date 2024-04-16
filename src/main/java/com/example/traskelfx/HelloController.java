package com.example.traskelfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void goToCommandeView(ActionEvent event) {
        try {
            // Charger la vue Commande depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("commande-view.fxml"));
            Parent root = loader.load();

            // Afficher la vue dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commande");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToPanierView(ActionEvent event) {
        try {
            // Charger la vue Panier depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("panier-view.fxml"));
            Parent root = loader.load();

            // Afficher la vue dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Panier");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}