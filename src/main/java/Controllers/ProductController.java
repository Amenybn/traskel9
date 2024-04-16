package Controllers;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static services.ServiceCategorie.chargerCategories;
public class ProductController {
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";

    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField searchTextField;

    @FXML
    public void initialize() {

        List<String> categories = chargerCategories();
        categories.add(0, "Tous les catégories");
        categoryComboBox.setItems(FXCollections.observableArrayList(categories)); //màj liste cat
        loadProducts(null);

        // Ajouter un gestionnaire d'événements pour le ComboBox des catégories
        categoryComboBox.setOnAction(this::handleCategorySelection);
    }

    private Node createProductNode(Produit produit) {

        VBox produitBox = new VBox();
        produitBox.setSpacing(10); // Augmenter l'espace entre les boîtes
        produitBox.setPadding(new Insets(10, 10, 10, 10));
        // Ajouter une image du produit
        ImageView imageView = new ImageView(new Image(new File(produit.getPhoto_prod()).toURI().toString()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        // Ajouter le nom du produit
        Label nameLabel = new Label(produit.getNom_prod());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        Label priceLabel = new Label(produit.getPrix_prod() + "DT");
        priceLabel.setStyle("-fx-text-fill: white;");

        Label descriptionLabel = new Label(produit.getDescrp_prod());
        descriptionLabel.setWrapText(true); // Pour que la description soit sur une seule ligne
        descriptionLabel.setStyle("-fx-text-fill: white;");
        // Ajouter les éléments dans le VBox
        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel);
        // Définir le style du VBox
        produitBox.setStyle("-fx-background-color: #393351; -fx-background-radius: 10px; -fx-padding: 20px;"); // Pour définir la couleur de fond en blanc et arrondir les coins du VBox
        // Ajouter un gestionnaire d'événements pour le clic sur chaque produit
        produitBox.setOnMouseClicked(event -> {
            try {
                showProductDetails(produit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return produitBox;
    }

    private void showProductDetails(Produit produit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/DetailsProd.fxml"));
        Parent root = loader.load();

        // Passer le produit sélectionné au contrôleur de la page DetailsProd
        DetailsProdController detailsProdController = loader.getController();
        detailsProdController.initData(produit);

        // Afficher la scène avec les détails du produit
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private List<Produit> loadProductsFromDatabase(String category) {
        List<Produit> produits = new ArrayList<>();
        String query;

        if (category == null || category.isEmpty()) {
            query = "SELECT * FROM produit";
        } else {
            query = "SELECT * FROM produit WHERE type_prod = ?";
        }

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

            if (category != null && !category.isEmpty()) { // Vérifie si une catégorie est sélectionnée
                statement.setString(1, category);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }



    private void loadProducts(String category) {
        gridPane.getChildren().clear(); // Efface les anciens éléments de la grille
        int columnCount = 3; // Nombre de colonnes dans la grille
        int rowCount = 0; // Compteur de lignes

        // Charger les produits depuis la base de données ou tout autre source de données
        List<Produit> produits;

        if ("Tous les catégories".equals(category)) {
            produits = loadAllProductsFromDatabase();
        } else {
            produits = loadProductsFromDatabase(category);
        }

        for (Produit produit : produits) {
            // Créer un élément d'affichage pour chaque produit (par exemple, un VBox avec une image, un nom et un prix)
            Node produitNode = createProductNode(produit);

            // Ajouter l'élément dans la grille
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }

    private List<Produit> loadAllProductsFromDatabase() {

        return loadProductsFromDatabase(null);
    }

   //in comboBox
    @FXML
    void handleCategorySelection(ActionEvent event) {
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            loadProducts(null);
        } else {
            loadProducts(selectedCategory);
        }
    }






    @FXML
    void handleSearch(ActionEvent event) {
        String searchTerm = searchTextField.getText();
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();


        if (selectedCategory == null || selectedCategory.isEmpty() || "Tous les catégories".equals(selectedCategory)) {
            searchProducts(searchTerm, null);
        } else {
            searchProducts(searchTerm, selectedCategory);
        }
    }

    private void searchProducts(String searchTerm, String category) {
        gridPane.getChildren().clear();
        int columnCount = 3;
        int rowCount = 0;

        List<Produit> produits = searchProductsFromDatabase(searchTerm, category);

        for (Produit produit : produits) {
            Node produitNode = createProductNode(produit);

            // Ajouter l'élément dans la grille
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }

    private List<Produit> searchProductsFromDatabase(String searchTerm, String category) {
        List<Produit> produits = new ArrayList<>();
        String query;

        if (category == null || category.isEmpty()) {
            query = "SELECT * FROM produit WHERE nom_prod LIKE ?";
        } else {
            query = "SELECT * FROM produit WHERE nom_prod LIKE ? AND type_prod = ?";
        }
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "%" + searchTerm + "%");
            if (category != null && !category.isEmpty()) { // Vérifie si une catégorie est sélectionnée
                statement.setString(2, category);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
}
