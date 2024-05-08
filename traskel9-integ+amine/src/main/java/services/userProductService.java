package services;

import Controllers.userProductController;
import Controllers.userProductController;
import entities.Categorie;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class userProductService {
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    private userProductController controller;

    public userProductService(userProductController controller) {
        this.controller = controller;
    }



    public void updateProduit(Produit produit) {

        Label titleLabel = new Label("Modifier le produit");
        titleLabel.getStyleClass().add("title");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #10165F; -fx-padding: 20px 40px 20px 120px;");

        // Charger toutes les catégories depuis la base de données
        ObservableList<String> nomCategories = chargerCategories();


        TextInputDialog dialog = new TextInputDialog(produit.getNom_prod());
        dialog.setTitle("Modifier le produit");
        dialog.setHeaderText(null);


        TextField prixField = new TextField(String.valueOf(produit.getPrix_prod()));
        TextArea descriptionArea = new TextArea(produit.getDescrp_prod());
        descriptionArea.setWrapText(true); // Pour que la description soit sur une seule ligne

        // Créer le ComboBox pour la catégorie de produit
        ComboBox<String> categorieField = new ComboBox<>(nomCategories);
        categorieField.setValue(produit.getType_prod());


        ImageView oldImageView = new ImageView(new Image(new File(produit.getPhoto_prod()).toURI().toString()));
        oldImageView.setFitWidth(170);
        oldImageView.setFitHeight(150);
        VBox.setMargin(oldImageView, new Insets(10, 0, 10, 0)); // Ajouter une marge de 10 pixels en haut et en bas


        // Créer un bouton pour modifier la photo du produit
        Button modifierPhotoButton = new Button("Modifier Photo");
        modifierPhotoButton.setOnAction(e -> {
            String newPhotoPath = modifierPhoto();
            if (newPhotoPath != null) {
                produit.setPhoto_prod(newPhotoPath);
                // Màj img
                oldImageView.setImage(new Image(new File(newPhotoPath).toURI().toString()));
            }
        });


        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                titleLabel, new Label("Nom :"),
                dialog.getDialogPane().getContent(),
                new Label("Prix :"), prixField,
                new Label("Description :"), descriptionArea,
                new Label("Catégorie du produit :"),
                categorieField,
                oldImageView,
                modifierPhotoButton);


        dialog.getDialogPane().setPrefWidth(500);
        dialog.getDialogPane().setPrefHeight(600); // Ajustez la hauteur pour inclure l'image et le bouton de modification de photo


        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/DashStyle.css").toExternalForm());

        dialog.getDialogPane().setContent(vbox);

        // Supprimer l'icône de l'en-tête
        dialog.getDialogPane().setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE produit SET nom_prod=?, prix_prod=?, descrp_prod=?, type_prod=?, photo_prod=? WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, newName);
                    statement.setDouble(2, Double.parseDouble(prixField.getText()));
                    statement.setString(3, descriptionArea.getText());
                    statement.setString(4, categorieField.getValue());
                    statement.setString(5, produit.getPhoto_prod());
                    statement.setInt(6, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            controller.afficher();
        });
    }


    public List<Produit> loadProductsFromDatabase(int userId) {
        List<Produit> produits = new ArrayList<>();

        String query = "SELECT * FROM produit WHERE id_user_id = ?"; // Modifier la requête pour sélectionner uniquement les produits de l'utilisateur courant

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId); // Passer l'ID de l'utilisateur en tant que paramètre à la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produit produit = new Produit();
                    produit.setId(resultSet.getInt("id"));
                    produit.setNom_prod(resultSet.getString("nom_prod"));
                    produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                    produit.setPhoto_prod(resultSet.getString("photo_prod"));
                    produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                    produit.setType_prod(resultSet.getString("type_prod"));
                    produit.setId_user_id(resultSet.getInt("id_user_id"));
                    produit.setPanier_id(resultSet.getInt("panier_id"));
                    produits.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }

    public void deleteProduit(Produit produit) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer le produit");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "DELETE FROM produit WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            controller.afficher(); // Màj l'affichage
        }
    }
    private String modifierPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String fileName = UUID.randomUUID().toString() + selectedFile.getName();
                Path targetPath = Paths.get("photos", fileName); // Chemin relatif au dossier images dans votre projet
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                return targetPath.toString(); // Retourner le chemin de la nouvelle photo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null; // Retourner null si aucune nouvelle photo n'est sélectionnée
    }

    private ObservableList<String> chargerCategories() {
        // Charger les catégories depuis la base de données
        List<Categorie> categories = MyDatabase.getInstance().getAllCategories();

        // Créer une liste observable des noms de catégories
        ObservableList<String> nomCategories = FXCollections.observableArrayList();
        for (Categorie categorie : categories) {
            nomCategories.add(categorie.getCategorie_prod());
        }

        return nomCategories;
    }
}
