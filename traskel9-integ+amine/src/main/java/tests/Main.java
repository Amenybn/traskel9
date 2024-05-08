package tests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/ListProducts.fxml"));

        // Créer une nouvelle scène avec le contenu chargé depuis le fichier FXML
        Scene scene = new Scene(root, 640, 480);

        // Titre de la fenêtre
        stage.setTitle("Gestion Produit");

        // Définir la scène
        stage.setScene(scene);


        // Afficher la fenêtre
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
