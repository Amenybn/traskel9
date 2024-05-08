package Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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
            // Load the FXML file
            URL resourceUrl = getClass().getResource("/Fxml/commande-view.fxml");
            if (resourceUrl == null) {
                System.err.println("FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            // Display the view in a new window
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
            // Load the FXML file
            URL resourceUrl = getClass().getResource("/Fxml/panier-view.fxml");
            if (resourceUrl == null) {
                System.err.println("FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            // Display the view in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commande");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}