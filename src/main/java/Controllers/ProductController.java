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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.CategorieService;
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
    private int itemsPerPage = 8;
    private int currentPage = 1;
    private int columnCount = 3;

    @FXML
    private TextField minPriceTextField;

    @FXML
    private TextField maxPriceTextField;

    @FXML
    public void initialize() {
        List<String> categories = CategorieService.chargerCategories();
        categories.add(0, "Tous les catégories");
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        loadProducts(null);

        categoryComboBox.setOnAction(this::handleCategorySelection);
    }


    private Double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return null;
        }
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

        Button favoriteButton = new Button();
        ImageView favoriteIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/transparent.png")));
        favoriteButton.setGraphic(favoriteIcon);

        if (isProductInFavorites(getCurrentUserId(), produit.getId())) {
            favoriteIcon.setImage(new Image(getClass().getResourceAsStream("/resources/red.png")));
        }

        favoriteButton.setOnAction(event -> {
            int userId = getCurrentUserId();
            if (isProductInFavorites(userId, produit.getId())) {
                removeProductFromFavorites(userId, produit.getId());
                favoriteIcon.setImage(new Image(getClass().getResourceAsStream("/resources/transparent.png")));
            } else {
                saveProductAsFavorite(userId, produit.getId());
                favoriteIcon.setImage(new Image(getClass().getResourceAsStream("/resources/red.png")));
            }
        });

        favoriteIcon.setFitWidth(24);
        favoriteIcon.setFitHeight(24);
        HBox imageAndFavoriteBox = new HBox(imageView, favoriteButton);
        imageAndFavoriteBox.setSpacing(10); // Ajouter de l'espace entre l'image et le bouton
        imageAndFavoriteBox.setStyle("-fx-alignment: CENTER_RIGHT;");
        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, favoriteButton);
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

    private void saveProductAsFavorite(int userId, int productId) {
        String query = "INSERT INTO favoris (user_id, product_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeProductFromFavorites(int userId, int productId) {
        String query = "DELETE FROM favoris WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isProductInFavorites(int userId, int productId) {
        String query = "SELECT COUNT(*) FROM favoris WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
        Double minPrice = parsePrice(minPriceTextField.getText());
        Double maxPrice = parsePrice(maxPriceTextField.getText());

        if (selectedCategory == null || selectedCategory.isEmpty() || "Tous les catégories".equals(selectedCategory)) {
            searchProducts(searchTerm, null, minPrice, maxPrice);
        } else {
            searchProducts(searchTerm, selectedCategory, minPrice, maxPrice);
        }
    }



    private void searchProducts(String searchTerm, String category, Double minPrice, Double maxPrice) {
        gridPane.getChildren().clear();
        int columnCount = 3;
        int rowCount = 0;

        List<Produit> produits = searchProductsFromDatabase(searchTerm, category, minPrice, maxPrice);

        for (Produit produit : produits) {
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }
    private List<Produit> searchProductsFromDatabase(String searchTerm, String category, Double minPrice, Double maxPrice) {
        List<Produit> produits = new ArrayList<>();
        String query;

        if (category == null || category.isEmpty()) {
            query = "SELECT * FROM produit WHERE nom_prod LIKE ?";
        } else {
            query = "SELECT * FROM produit WHERE nom_prod LIKE ? AND type_prod = ?";
        }

        if (minPrice != null) {
            query += " AND prix_prod >= " + minPrice;
        }

        if (maxPrice != null) {
            query += " AND prix_prod <= " + maxPrice;
        }

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "%" + searchTerm + "%");
            if (category != null && !category.isEmpty()) {
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
        int columnCount = 4; // Nombre de colonnes dans la grille
        int rowCount = 0; // Compteur de lignes
        if ("Tous les catégories".equals(category)) {
            produits = loadAllProductsFromDatabase();
        } else {
            produits = loadProductsFromDatabase(category);
        }
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, produits.size());

        for (int i = startIndex; i < endIndex; i++) {
            Produit produit = produits.get(i);
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, (i - startIndex) % columnCount, (i - startIndex) / columnCount);
        }
    }

    private List<Produit> loadAllProductsFromDatabase() {

        return loadProductsFromDatabase(null);
    }
    @FXML
    void handleCategorySelection(ActionEvent event) {
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            // Si aucune catégorie n'est sélectionnée, charger tous les produits
            loadProducts(null);
        } else {
            // Sinon, charger les produits selon la catégorie sélectionnée
            loadProducts(selectedCategory);

            // Initialiser les champs des prix
            minPriceTextField.setText("");
            maxPriceTextField.setText("");
            searchTextField.setText("");

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

    private int getCurrentUserId() {
        // Implémentez le code pour obtenir l'ID de l'utilisateur actuel
        return 1; // Par exemple, retourne toujours l'ID 1 pour le moment
    }
}
