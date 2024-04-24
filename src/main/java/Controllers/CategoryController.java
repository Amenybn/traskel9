package Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.MyDatabase;
import java.io.IOException;
import java.sql.*;


public class CategoryController {
    Connection con = null;
    PreparedStatement st = null;

    @FXML
    private TextField categorie_prod;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Label errorLabel;

    @FXML
    void ajouter(ActionEvent event) {

        if (validateForm()) {

            String categorie = categorie_prod.getText();
            String insert = "INSERT INTO categorie_prod (categorie_prod) VALUES (?)";
            con = MyDatabase.getInstance().getConnection();
            try {
                st = con.prepareStatement(insert);
                st.setString(1, categorie);
                st.executeUpdate();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Votre catégorie a été ajoutée avec succès.");
                successAlert.showAndWait();
                annuler();
            } catch (SQLException e) {
                e.printStackTrace();

                errorLabel.setText("Une erreur s'est produite lors de l'ajout de la catégorie.");
            }
        }
    }
    @FXML
    private void annuler() {
        // Clear all fields and error labels
        categorie_prod.clear();
    }

    private boolean validateForm() {

        String categorie = categorie_prod.getText();
        if (categorie.isEmpty()) {

            errorLabel.setText("Veuillez saisir un nom de catégorie.");
            return false;
        } else if (categorie.length() < 3) {

            errorLabel.setText("Le nom de catégorie doit contenir au moins 3 caractères.");
            return false;
        } else {

            errorLabel.setText("");
            return true;
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }

    }


    @FXML
    void afficherListeCategories(ActionEvent event) {
        try {
            // Charger la page DashCategories.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/DashCategories.fxml"));

            // Créer un nouveau Stage
            Stage stage = new Stage();
            // Définir la scène avec la racine chargée
            stage.setScene(new Scene(root));
            // Afficher le nouveau Stage
            stage.show();

            // Fermer la fenêtre actuelle
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}






















