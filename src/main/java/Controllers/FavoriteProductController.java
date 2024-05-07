package controllers;

import entities.Produit;
import entities.Utilisateur;
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
import services.UtilisateurCrud;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteProductController {
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    @FXML
    private GridPane gridPane;
    private int itemsPerPage = 8;
    private int currentPage = 1;
    private int columnCount = 4;

    Connection cnx2;

    @FXML
    public void initialize() {
        loadFavoriteProducts();
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
                loadFavoriteProducts();
            } else {
                saveProductAsFavorite(userId, produit.getId());
                favoriteIcon.setImage(new Image(getClass().getResourceAsStream("/resources/red.png")));
            }
        });

        favoriteIcon.setFitWidth(24);
        favoriteIcon.setFitHeight(24);
        HBox imageAndFavoriteBox = new HBox(imageView, favoriteButton);

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

    private void showProductDetails(Produit produit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/DetailsProd.fxml"));
        Parent root = loader.load();

        DetailsProdController detailsProdController = loader.getController();
        detailsProdController.initData(produit);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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




    private void loadFavoriteProducts() {
        gridPane.getChildren().clear();
        List<Produit> favoriteProducts = getFavoriteProductsFromDatabase();

        int startIndex = 0;
        int endIndex = Math.min(itemsPerPage, favoriteProducts.size());

        for (int i = startIndex; i < endIndex; i++) {
            Produit produit = favoriteProducts.get(i);
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, (i - startIndex) % columnCount, (i - startIndex) / columnCount);
        }
    }

    private List<Produit> getFavoriteProductsFromDatabase() {
        List<Produit> favoriteProducts = new ArrayList<>();
        int userId = getCurrentUserId(); // Obtenez l'ID de l'utilisateur courant

        String query = "SELECT * FROM produit INNER JOIN favoris ON produit.id = favoris.product_id WHERE favoris.user_id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produit produit = new Produit();
                    produit.setId(resultSet.getInt("id"));
                    produit.setNom_prod(resultSet.getString("nom_prod"));
                    produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                    produit.setPhoto_prod(resultSet.getString("photo_prod"));
                    produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                    favoriteProducts.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteProducts;
    }

    private int getCurrentUserId() {
        String email = Sess.getEmailUtilisateurCourant();
        if (email != null) {
            UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
            Utilisateur utilisateur = utilisateurCrud.getUtilisateurByEmail(email);
            if (utilisateur != null) {
                return utilisateur.getId();
            }
        }
        return -1; // Retourne -1 ou une autre gestion d'erreur si l'utilisateur n'est pas trouvé
    }

}
