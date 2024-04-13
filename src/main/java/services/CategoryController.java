package services;
import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyDatabase;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;


public class CategoryController {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    @FXML
    private TextField categorie_prod;

    @FXML
    private TableColumn<Categorie, String> nomm;

    @FXML
    private TableColumn<Categorie, Integer> id;

    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

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
    private Categorie categorie;


    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        categorie_prod.setText(categorie.getCategorie_prod());
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Vérifier si le champ de texte est vide
        String categorie = categorie_prod.getText();
        if (categorie.isEmpty()) {
            // Afficher un message si le champ est vide
            errorLabel.setText("Veuillez saisir un nom de catégorie.");
            return; // Arrêter l'exécution de la fonction si le champ est vide
        } else if (categorie.length() < 3) {
            // Afficher un message si la saisie contient moins de 3 caractères
            errorLabel.setText("Le nom de catégorie doit contenir au moins 3 caractères.");
            return; // Arrêter l'exécution de la fonction si la saisie est inférieure à 3 caractères
        } else {
            // Effacer le message d'erreur s'il n'y a pas d'erreur de saisie
            errorLabel.setText("");
        }

        // Si la saisie est valide, procéder à l'insertion des données dans la base de données
        String insert = "INSERT INTO categorie_prod (categorie_prod) VALUES (?)";
        con = MyDatabase.getInstance().getConnection();
        try {
            st = con.prepareStatement(insert);
            st.setString(1, categorie);
            st.executeUpdate();

            // Afficher un message de succès dans un AlertDialog
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Votre catégorie a été ajoutée avec succès.");
            successAlert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher un message d'erreur si une erreur se produit lors de l'ajout
            errorLabel.setText("Une erreur s'est produite lors de l'ajout de la catégorie.");
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if(actionEvent.getSource()==btnOrders)
        {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
    }







}






















