package Controllers;

import entities.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.userProductService;

import java.io.File;
import java.util.List;

public class userProductController {

    @FXML
    public GridPane gridPane;

    @FXML
    public void initialize() {
        afficherr(new ActionEvent());
    }

    public Node createProductNode(Produit produit) {
        VBox produitBox = new VBox();
        produitBox.setSpacing(10); // Augmenter l'espace entre les boîtes
        produitBox.setPadding(new Insets(10, 10, 10, 10)); // Ajouter de la marge autour du VBox
        // Ajouter une image du produit
        ImageView imageView = new ImageView(new Image(new File(produit.getPhoto_prod()).toURI().toString()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        // Ajouter le nom du produit
        Label nameLabel = new Label(produit.getNom_prod());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        // Ajouter le prix du produit
        Label priceLabel = new Label(produit.getPrix_prod() + "DT");
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
        // Retourner le VBox contenant les détails du produit
        return produitBox;
    }

    public void updateProduit(Produit produit) {
        new userProductService(this).updateProduit(produit);
    }

    public void deleteProduit(Produit produit) {
        new userProductService(this).deleteProduit(produit);
    }

    public void afficher() {
        gridPane.getChildren().clear();
        int columnCount = 3;
        int rowCount = 0;
        List<Produit> produits = new userProductService(this).loadProductsFromDatabase();
        for (Produit produit : produits) {
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }
    public void afficherr(javafx.event.ActionEvent event) {
        afficher();
    }


}
