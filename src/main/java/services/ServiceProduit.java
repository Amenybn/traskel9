package services;

import entities.Categorie;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import utils.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ServiceProduit {
    private File selectedFile;

    @FXML
    private TextField nom;

    @FXML
    private TextField prix;

    @FXML
    private TextArea descrp;

    @FXML
    private ImageView photo;


    @FXML
    private TableColumn<Produit, Integer> id;
    @FXML
    private TableColumn<Produit, String> nom_prod;
    @FXML
    private TableColumn<Produit, Double> prix_prod;
    @FXML
    private TableColumn<Produit, String> descrp_prod;
    @FXML
    private TableColumn<Produit, String> photo_prod;

    @FXML
    private TableView<Produit> table;

    @FXML
    private ComboBox<String> ListeCat;

    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            photo.setImage(image);
        }
    }


    @FXML
    void initialize() {

        chargerCategories();
    }




    @FXML
    void ajouter(ActionEvent event) {
        String insert = "INSERT INTO produit (nom_prod, descrp_prod, prix_prod, photo_prod, type_prod) VALUES (?, ?, ?, ?, ?)";
        Connection con = MyDatabase.getInstance().getConnection();

        chargerCategories();
        try {
            if (selectedFile != null) {
                String fileName = UUID.randomUUID().toString() + selectedFile.getName();
                Path targetPath = Paths.get("photos", fileName); // Chemin relatif au dossier images dans votre projet
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                String photoProd = targetPath.toString();

                // Récupérer la catégorie sélectionnée
                String categorie = ListeCat.getValue();

                PreparedStatement st = con.prepareStatement(insert);
                st.setString(1, nom.getText());
                st.setString(2, descrp.getText());
                st.setString(3, prix.getText());
                st.setString(4, photoProd);
                st.setString(5, categorie); // Ajouter la catégorie à la requête SQL
                st.executeUpdate();

                // Afficher une boîte de dialogue d'alerte pour indiquer que le produit a été ajouté avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le produit a été ajouté avec succès !");
                alert.showAndWait();

                // Effacer les champs de saisie après l'ajout du produit
                nom.clear();
                descrp.clear();
                prix.clear();
                photo.setImage(null); // Effacer l'image affichée

            } else {
                System.out.println("Aucune photo sélectionnée.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    private void chargerCategories() {
        // Charger les catégories depuis la base de données
        List<Categorie> categories = MyDatabase.getInstance().getAllCategories();

        // Créer une liste observable des noms de catégories
        ObservableList<String> nomCategories = FXCollections.observableArrayList();
        for (Categorie categorie : categories) {
            nomCategories.add(categorie.getCategorie_prod());
        }

        // Ajouter les noms de catégories au ComboBoxn
        ListeCat.setItems(nomCategories);
    }



    @FXML
    void afficher(ActionEvent event) {
        // Récupérer la liste des produits depuis le service de produit
        List<Produit> produits = MyDatabase.getInstance().getAllProduits();

        // Assigner les valeurs aux colonnes de la TableView
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom_prod.setCellValueFactory(new PropertyValueFactory<>("nom_prod"));
        prix_prod.setCellValueFactory(new PropertyValueFactory<>("prix_prod"));
        descrp_prod.setCellValueFactory(new PropertyValueFactory<>("descrp_prod"));
        photo_prod.setCellValueFactory(new PropertyValueFactory<>("photo_prod"));

        table.getItems().setAll(produits);

    }



    @FXML
    void annuler(ActionEvent event) {
        // Ajoutez ici le code à exécuter lorsque le bouton Annuler est cliqué
    }
}






















