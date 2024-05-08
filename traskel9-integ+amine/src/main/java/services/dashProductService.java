package services;

import Controllers.dashProductController;
import entities.Categorie;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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

public class dashProductService {
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    private dashProductController controller;

    public dashProductService(dashProductController controller) {
        this.controller = controller;
    }



    public List<Produit> loadProductsFromDatabase() {
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
