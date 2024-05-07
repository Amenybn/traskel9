package controllers;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class DetailsProdController {
    @FXML
    private Label nomProd;

    @FXML
    private Label PrixProd;

    @FXML
    private ImageView photoProd;

    @FXML
    private Label description;


    public void initData(Produit produit) {
        // Afficher les détails du produit
        nomProd.setText(produit.getNom_prod());
        PrixProd.setText(produit.getPrix_prod() + "DT");
        description.setText(produit.getDescrp_prod());
        String imagePath = produit.getPhoto_prod();
        Image image = new Image(new File(imagePath).toURI().toString());
        photoProd.setImage(image);
    }
}
