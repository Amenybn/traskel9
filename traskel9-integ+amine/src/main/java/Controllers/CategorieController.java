package Controllers;

import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import services.CategorieService;

public class CategorieController {

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Button btnCustomers;
    private CategorieService categorieService = new CategorieService();
    private ObservableList<Categorie> observableCategories = FXCollections.observableArrayList();

    @FXML
    private ListView<Categorie> categorieListView;

    @FXML
    private TextField nomField;

    @FXML
    public void initialize() {
        observableCategories.addAll(categorieService.getAllCategories());
        categorieListView.setItems(observableCategories);
    }

    @FXML
    public void ajouterCategorie() {
        String nom = nomField.getText();
        if (!nom.isEmpty()) {
            categorieService.ajouterCategorie(nom);
            observableCategories.setAll(categorieService.getAllCategories());
            nomField.clear();
        }
    }

    @FXML
    public void modifierCategorie() {
        Categorie selectedCategorie = categorieListView.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            String nouveauNom = nomField.getText();
            if (!nouveauNom.isEmpty()) {
                categorieService.modifierCategorie(selectedCategorie.getId(), nouveauNom);
                observableCategories.setAll(categorieService.getAllCategories());
                nomField.clear();
            }
        }
    }

    @FXML
    public void supprimerCategorie() {
        Categorie selectedCategorie = categorieListView.getSelectionModel().getSelectedItem();
        if (selectedCategorie != null) {
            categorieService.supprimerCategorie(selectedCategorie.getId());
            observableCategories.setAll(categorieService.getAllCategories());
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }

    }
}
