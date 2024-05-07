package Controllers;

import entities.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.dashProductService;

import java.io.File;
import java.io.IOException;
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
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #030303;");

        Label priceLabel = new Label(produit.getPrix_prod() + "DT");
        priceLabel.setStyle("-fx-text-fill: #000000;");

        Label descriptionLabel = new Label(produit.getDescrp_prod());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-text-fill: #050505;");

        // Créer un bouton "Supprimer"
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> deleteProduit(produit));

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().add(deleteButton);

        produitBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, buttonsBox);
        produitBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10px; -fx-padding: 20px;");

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


    public void deleteProduit(Produit produit) {
        new dashProductService(this).deleteProduit(produit);
    }

    public void afficher() {
        gridPane.getChildren().clear();
        int columnCount = 4;
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
