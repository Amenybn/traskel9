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
import services.ServiceCategorie;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    @FXML
    private TextField searchTextField;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> categoryComboBox;

    private List<Produit> produits;
    private int itemsPerPage = 6;
    private int currentPage = 1;
    private int columnCount = 3;

    @FXML
    public void initialize() {
        List<String> categories = ServiceCategorie.chargerCategories();
        categories.add(0, "Tous les catégories");
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        loadProducts(null);
    }

    private Node createProductNode(Produit produit) {
        VBox produitBox = new VBox();
        produitBox.setSpacing(10);
        produitBox.setPadding(new Insets(10, 10, 10, 10));

        ImageView imageView = new ImageView(new Image(new File(produit.getPhoto_prod()).toURI().toString()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        Label nameLabel = new Label(produit.getNom_prod());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        Label priceLabel = new Label(produit.getPrix_prod() + "DT");
        priceLabel.setStyle("-fx-text-fill: white;");

        Label descriptionLabel = new Label(produit.getDescrp_prod());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-text-fill: white;");

        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel);
        produitBox.setStyle("-fx-background-color: #393351; -fx-background-radius: 10px; -fx-padding: 20px;");

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

        DetailsProdController detailsProdController = loader.getController();
        detailsProdController.initData(produit);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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

            if (category != null && !category.isEmpty()) {
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
        gridPane.getChildren().clear();
        produits = loadProductsFromDatabase(category);
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, produits.size());

        for (int i = startIndex; i < endIndex; i++) {
            Produit produit = produits.get(i);
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, (i - startIndex) % columnCount, (i - startIndex) / columnCount);
        }
    }

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
    void goToPreviousPage(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            loadProducts(categoryComboBox.getValue());
        }
    }

    @FXML
    void goToNextPage(ActionEvent event) {
        int maxPage = (int) Math.ceil((double) produits.size() / itemsPerPage);
        if (currentPage < maxPage) {
            currentPage++;
            loadProducts(categoryComboBox.getValue());
        }
    }
}
