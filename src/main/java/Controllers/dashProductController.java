package Controllers;

import entities.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.dashProductService;

import java.io.File;
import java.util.List;

public class dashProductController {

    @FXML
    public GridPane gridPane;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Button btnCustomers;
    @FXML
    public void initialize() {
        afficherr(new ActionEvent());
    }

    public Node createProductNode(Produit produit) {
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

        // Créer un bouton "Supprimer"
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteProduit(produit));

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().add(deleteButton);

        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, buttonsBox);
        produitBox.setStyle("-fx-background-color: #393351; -fx-background-radius: 10px; -fx-padding: 20px;");

        return produitBox;
    }


    public void deleteProduit(Produit produit) {
        new dashProductService(this).deleteProduit(produit);
    }

    public void afficher() {
        gridPane.getChildren().clear();
        int columnCount = 3;
        int rowCount = 0;
        List<Produit> produits = new dashProductService(this).loadProductsFromDatabase();
        for (Produit produit : produits) {
            Node produitNode = createProductNode(produit);
            gridPane.add(produitNode, rowCount % columnCount, rowCount / columnCount);
            rowCount++;
        }
    }
    public void afficherr(ActionEvent event) {
        afficher();
    }
    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
    }

}
