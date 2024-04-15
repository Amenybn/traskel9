package Controllers;


import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceCategory;
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

import static services.ServiceCategory.chargerCategories;

public class ControllerAjoutProd {
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
    private ComboBox<String> ListeCat;

    @FXML
    private Label errorLabel;
    @FXML
    private Label nomValidationLabel; // Error label for nom

    @FXML
    private Label prixValidationLabel; // Error label for prix

    @FXML
    private Label catValidationLabel; // Error label for ListeCat
    @FXML
    private Label photoValidationLabel;



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
        ListeCat.setItems(chargerCategories());
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Vérification des champs du formulaire
        if (!validateForm()) {
            return;
        }

        String insert = "INSERT INTO produit (nom_prod, descrp_prod, prix_prod, photo_prod, type_prod) VALUES (?, ?, ?, ?, ?)";
        Connection con = MyDatabase.getInstance().getConnection();

        chargerCategories();
        try {
            if (selectedFile != null) {
                String fileName = UUID.randomUUID().toString() + selectedFile.getName();
                Path targetPath = Paths.get("photos", fileName);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                String photoProd = targetPath.toString();
                String categorie = ListeCat.getValue();
                // prepareStatement un objet utilisé pour préparer une instruction SQL
                //st  un objet utilisé pour exécuter des requêtes SQL paramétrées.

                PreparedStatement st = con.prepareStatement(insert);
                st.setString(1, nom.getText());
                st.setString(2, descrp.getText());
                st.setString(3, prix.getText());
                st.setString(4, photoProd);
                st.setString(5, categorie);
                st.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le produit a été ajouté avec succès !");
                alert.showAndWait();

                annuler();

            } else {
                System.out.println("Aucune photo sélectionnée.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void annuler() {
        // Clear all fields and error labels
        nom.clear();
        prix.clear();
        ListeCat.getSelectionModel().clearSelection();
        descrp.clear();
        //photo.setImage(null);
        nomValidationLabel.setText("");
        prixValidationLabel.setText("");
        catValidationLabel.setText("");
        photoValidationLabel.setText("");
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (nom.getText().isEmpty()) {
            nomValidationLabel.setText("Veuillez saisir un nom.");
            isValid = false;
        } else if (nom.getText().length() < 3) {
            nomValidationLabel.setText("Le nom doit contenir au moins 3 caractères.");
            isValid = false;
        } else {
            nomValidationLabel.setText(""); // Clear the error message
        }

        if (prix.getText().isEmpty()) {
            prixValidationLabel.setText("Veuillez saisir un prix.");
            isValid = false;
        } else {
            try {
                Double.parseDouble(prix.getText());
                prixValidationLabel.setText(""); // Clear the error message
            } catch (NumberFormatException e) {
                prixValidationLabel.setText("Veuillez saisir un prix valide.");
                isValid = false;
            }
        }

        if (ListeCat.getValue() == null) {
            catValidationLabel.setText("Veuillez sélectionner une catégorie.");
            isValid = false;
        } else {
            catValidationLabel.setText(""); // Clear the error message
        }

        // Vérification si une image est sélectionnée
        if (selectedFile == null) {
            // Afficher un message d'erreur pour l'image
            photoValidationLabel.setText("Veuillez sélectionner une image.");
            isValid = false;
        } else {
            photoValidationLabel.setText(""); // Clear the error message
        }

        return isValid;
    }





}

