package Controllers;

import entities.Panier;
import entities.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyDatabase;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DetailsProdController {
    @FXML
    private Label nomProd;

    @FXML
    private Label PrixProd;

    @FXML
    private ImageView photoProd;

    @FXML
    private Label description;
    static String url="jdbc:mysql://localhost:3306/traskel_bd";
    static String username="root";
    static String password="";
    private Produit currentProduct;
    public void initData(Produit produit) {
        if (produit != null) {
            this.currentProduct = produit;
            nomProd.setText(produit.getNom_prod());
            PrixProd.setText(produit.getPrix_prod() + "DT");
            description.setText(produit.getDescrp_prod());
            if (produit.getPhoto_prod() != null && !produit.getPhoto_prod().isEmpty()) {
                Image image = new Image(new File(produit.getPhoto_prod()).toURI().toString());
                photoProd.setImage(image);
            }
        } else {
            System.out.println("Product data is null");
        }
    }


    @FXML
    private void AddPanier(ActionEvent event) {
        try {
            // Charger le fichier FXML de la vue d'ajout de commande
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/addPanier.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale (stage) à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Changer la scène de la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Produit getCurrentSelectedProduct() {
        return this.currentProduct;
    }

    private static Panier panierInstance = new Panier();
    @FXML private Button checkoutButton;

    public static Panier getPanier() {
        return panierInstance;
    }
    @FXML
    private void goToAddPanier(ActionEvent event) throws IOException {
        if (currentProduct != null) {
            Panier panier = getPanier(); // Retrieve or create a Panier
            panier.addProductId(currentProduct.getId()); // Add current product ID to Panier
            updatePanierInDatabase(panier); // Persist changes

            // Optionally, confirm addition or update UI
            System.out.println("Product added to panier successfully");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/addPanier.fxml"));
            Parent root = loader.load();
            // Maybe close the window or show a message
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close(); // or use any other navigation logic
        } else {
            System.out.println("No product selected");
        }
    }


    private void updatePanierInDatabase(Panier panier) {
        String sql = "UPDATE panier SET productIds = ? WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, panier.getProductIds());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Panier getPanierById(int panierId) {
        Panier panier = null;
        String sql = "SELECT * FROM panier WHERE id = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, panierId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    panier = new Panier();
                    panier.setId(resultSet.getInt("id"));
                    panier.setNbrProds(resultSet.getInt("nbr_prods"));
                    panier.setTotalPrix(resultSet.getFloat("total_prix"));

                    String productIds = resultSet.getString("productIds");
                    if (productIds != null && !productIds.isEmpty()) {
                        // Convert string array to integer list
                        List<Integer> ids = Arrays.stream(productIds.split(","))
                                .map(Integer::parseInt) // Convert each string to an integer
                                .collect(Collectors.toList());
                        panier.setProductIds(ids.toString());
                    } else {
                        panier.setProductIds(String.valueOf(new ArrayList<>()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using proper logging
        } catch (NumberFormatException e) {
            // This block handles cases where the string cannot be parsed to an integer
            System.err.println("Error parsing product IDs: " + e.getMessage());
        }
        return panier;
    }

}

