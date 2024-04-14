package services;

import entities.Categorie;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.MyDatabase;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class userProductController {
    ResultSet rs = null;

    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    @FXML
    private GridPane gridPane;
    @FXML
    private TableView<Produit> table;

    @FXML
    public void initialize() {
        afficher(null);
    }
    private Node createProductNode(Produit produit) {
        // Créer un VBox pour afficher les détails du produit
        VBox produitBox = new VBox();
        produitBox.setSpacing(20); // Augmenter l'espace entre les boîtes
        produitBox.setPadding(new Insets(20, 20, 20, 20)); // Ajouter de la marge autour du VBox

        // Ajouter une image du produit
        ImageView imageView = new ImageView(new Image(new File(produit.getPhoto_prod()).toURI().toString()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        // Ajouter le nom du produit
        Label nameLabel = new Label(produit.getNom_prod());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        // Ajouter le prix du produit
        Label priceLabel = new Label(produit.getPrix_prod() + "€");
        priceLabel.setStyle("-fx-text-fill: white;");

        // Ajouter la description du produit
        Label descriptionLabel = new Label(produit.getDescrp_prod());
        descriptionLabel.setWrapText(true); // Pour que la description soit sur une seule ligne
        descriptionLabel.setStyle("-fx-text-fill: white;");

        // Créer des boutons "Supprimer" et "Modifier"
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteProduit(produit));
        Button editButton = new Button("Modifier");
        editButton.setOnAction(event -> updateProduit(produit));

        // Créer une boîte horizontale pour les boutons
        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(deleteButton, editButton);

        // Ajouter les éléments dans le VBox
        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, buttonsBox);

        // Définir le style du VBox
        produitBox.setStyle("-fx-background-color: #393351; -fx-background-radius: 10px; -fx-padding: 20px;"); // Pour définir la couleur de fond en blanc et arrondir les coins du VBox
        produitBox.setMargin(produitBox, new Insets(0, 20, 0, 0));

        // Retourner le VBox contenant les détails du produit
        return produitBox;
    }


    public void updateProduit(Produit produit) {
        // Créer un TextInputDialog pour permettre à l'utilisateur de saisir les nouvelles valeurs
        TextInputDialog dialog = new TextInputDialog(produit.getNom_prod());
        dialog.setTitle("Modifier le produit");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouveau nom de produit :");

        // Créer un Label pour le titre
        Label titleLabel = new Label("Modifier le produit");
        titleLabel.getStyleClass().add("title");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #10165F; -fx-padding: 20px 0 70px 40px;");

        // Créer un VBox pour contenir le titre et le contenu du dialog
        VBox vbox = new VBox();
        vbox.getChildren().addAll(titleLabel, dialog.getDialogPane().getContent());

        // Définir la largeur et la hauteur souhaitées pour le dialog pane
        dialog.getDialogPane().setPrefWidth(400);
        dialog.getDialogPane().setPrefHeight(300);

        // Ajouter le fichier CSS personnalisé au dialog pane
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/DashStyle.css").toExternalForm());

        // Définir le contenu du dialog pane comme le VBox personnalisé
        dialog.getDialogPane().setContent(vbox);

        // Supprimer l'icône de l'en-tête
        dialog.getDialogPane().setGraphic(null);

        // Attendre que l'utilisateur valide ou annule la boîte de dialogue
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE produit SET nom_prod=? WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, newName);
                    statement.setInt(2, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            populateTableView(); // Mettre à jour l'affichage après la modification du produit
        });
    }





    private void populateTableView() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM produit")) {

            ObservableList<Produit> produits = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Produit produit = new Produit();
                produit.setId(resultSet.getInt("id"));
                produit.setNom_prod(resultSet.getString("nom_prod"));
                produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                produit.setType_prod(resultSet.getString("photo_prod")); // Utilisez resultSet au lieu de rs
                produits.add(produit);
            }
            table.setItems(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    interface ButtonActionHandler {
        void handle(Produit produit);
    }


    public void deleteProduit(Produit produit) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer le produit");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = MyDatabase.getInstance().getConnection()) {
                String sql = "DELETE FROM produit WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Mettre à jour l'affichage après la suppression du produit
            afficher(null);
        }
    }
    private List<Produit> loadProductsFromDatabase() {
        List<Produit> produits = new ArrayList<>();

        String query = "SELECT * FROM produit";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Produit produit = new Produit();
                produit.setId(resultSet.getInt("id"));
                produit.setNom_prod(resultSet.getString("nom_prod"));
                produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                produit.setPhoto_prod(resultSet.getString("photo_prod"));
                produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                produit.setType_prod(resultSet.getString("type_prod"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }

    @FXML
    void afficher(ActionEvent event) {
        gridPane.getChildren().clear(); // Efface les anciens éléments de la grille
        int columnCount = 3; // Nombre de colonnes dans la grille
        int rowCount = 0; // Compteur de lignes

        // Charger les produits depuis la base de données ou tout autre source de données
        List<Produit> produits = loadProductsFromDatabase();

        for (Produit produit : produits) {
            // Créer un élément d'affichage pour chaque produit (par exemple, un VBox avec une image, un nom et un prix)
            Node produitNode = createProductNode(produit);

            // Ajouter l'élément dans la grille
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }




}
