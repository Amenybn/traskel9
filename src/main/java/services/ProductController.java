package services;

import entities.Produit;
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
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProductController {
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

        // Ajouter les éléments dans le VBox
        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel);


        // Définir le style du VBox
        produitBox.setStyle("-fx-background-color: #393351; -fx-background-radius: 10px; -fx-padding: 20px;"); // Pour définir la couleur de fond en blanc et arrondir les coins du VBox
        produitBox.setMargin(produitBox, new Insets(0, 20, 0, 0));
        // Retourner le VBox contenant les détails du produit
        return produitBox;
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
