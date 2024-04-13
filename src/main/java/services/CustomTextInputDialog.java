package services;

import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomTextInputDialog extends TextInputDialog {

    public CustomTextInputDialog(String defaultValue) {
        super(defaultValue);

        // Définir le titre personnalisé
        this.setHeaderText("Nouveau nom du catégorie");

        // Supprimer l'icône
        ((Stage) this.getDialogPane().getScene().getWindow()).getIcons().clear();

        // Appliquer la feuille de style CSS
        this.getDialogPane().getStylesheets().add(getClass().getResource("../css/DashStyle.css").toExternalForm());
    }
}

