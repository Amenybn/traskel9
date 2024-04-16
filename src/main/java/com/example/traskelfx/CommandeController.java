package com.example.traskelfx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.Optional;

public class CommandeController {

    @FXML
    private TableView<Commande> tableView;

    @FXML
    private TableColumn<Commande, Integer> idColumn;

    @FXML
    private TableColumn<Commande, String> adresseColumn;

    @FXML
    private TableColumn<Commande, String> statutColumn;

    @FXML
    private TableColumn<Commande, Float> prixColumn;

    @FXML
    private TableColumn<Commande, String> delaiColumn;

    private static final String url = "jdbc:mysql://localhost:3306/javat";
    private static final String username = "root";
    private static final String password = "";

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseCmd"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutCmd"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixCmd"));
        delaiColumn.setCellValueFactory(new PropertyValueFactory<>("delaisCmd"));

        addButtonToTable("View", this::viewCommandeDetails, "View");
        addButtonToTable("Update", this::updateCommande, "Update");
        addButtonToTable("Delete", this::deleteCommande, "Delete");

        populateTableView();
    }


    private void populateTableView() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Commande")) {

            ObservableList<Commande> commandes = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Commande commande = new Commande();
                commande.setId(resultSet.getInt("id"));
                commande.setAdresseCmd(resultSet.getString("adresseCmd"));
                commande.setStatutCmd(resultSet.getString("statutCmd"));
                commande.setPrixCmd(resultSet.getFloat("prixCmd"));
                commande.setDelaisCmd(resultSet.getString("delaisCmd"));
                commandes.add(commande);
            }
            tableView.setItems(commandes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable(String buttonText, ButtonActionHandler actionHandler, String columnHeader) {
        TableColumn<Commande, Void> column = new TableColumn<>(columnHeader);
        column.setCellValueFactory(new PropertyValueFactory<>(null));
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button(buttonText);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    button.setOnAction(event -> actionHandler.handle(getTableView().getItems().get(getIndex())));
                    setGraphic(button);
                }
            }
        });
        tableView.getColumns().add(column);
    }



    private void viewCommandeDetails(Commande commande) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Commande Details");
        alert.setHeaderText(null);
        alert.setContentText("ID: " + commande.getId() + "\n"
                + "Adresse: " + commande.getAdresseCmd() + "\n"
                + "Statut: " + commande.getStatutCmd() + "\n"
                + "Prix: " + commande.getPrixCmd() + "\n"
                + "Delai: " + commande.getDelaisCmd());
        alert.showAndWait();
    }

    interface ButtonActionHandler {
        void handle(Commande commande);
    }
    public void createCommande(Commande commande) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO commande (adressecmd, statutcmd, prixcmd, delaiscmd) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, commande.getAdresseCmd());
                statement.setString(2, commande.getStatutCmd());
                statement.setFloat(3, commande.getPrixCmd());
                statement.setString(4, commande.getDelaisCmd());
                statement.executeUpdate();
            }
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCommande() {
        // Create a dialog window
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Add Commande");
        dialog.setHeaderText("Enter Commande Details");

        // Create a GridPane layout to organize input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add input fields and labels to the GridPane
        TextField adresseField = new TextField();
        grid.add(new Label("Adresse:"), 0, 0);
        grid.add(adresseField, 1, 0);

        TextField statutField = new TextField("EnCours"); // Default value for statut
        statutField.setEditable(false); // Set the field as non-editable
        grid.add(new Label("Statut:"), 0, 1);
        grid.add(statutField, 1, 1);

        TextField prixField = new TextField();
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(prixField, 1, 2);

        TextField delaiField = new TextField();
        grid.add(new Label("Delai:"), 0, 3);
        grid.add(delaiField, 1, 3);

        // Set the custom GridPane layout to the dialog window
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(grid);

        boolean isValidInput = false;
        while (!isValidInput) {
            // Show the dialog window and wait for user input
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve user input from the input fields
                String adresse = adresseField.getText();
                String statut = statutField.getText();
                float prix;
                try {
                    prix = Float.parseFloat(prixField.getText());
                    if (prix <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    // Display error message for zero or negative price
                    showErrorDialog("Price must be greater than zero.");
                    continue; // Restart the loop
                }
                String delai = delaiField.getText();

                if (!adresse.matches("^\\d{4}\\s.+")) {
                    // Display error message for invalid address format
                    showErrorDialog("Address must start with a valid 4-digit postal code followed by a space and at least one character.");
                    continue; // Restart the loop
                }


                // Validate delai is not null
                if (delai.isEmpty()) {
                    // Display error message for empty delai
                    showErrorDialog("Delai cannot be empty.");
                    continue; // Restart the loop
                }

                isValidInput = true; // Input is valid, exit the loop

                // Now, create the Commande object and insert it into the database
                Commande newCommande = new Commande();
                newCommande.setAdresseCmd(adresse);
                newCommande.setStatutCmd(statut);
                newCommande.setPrixCmd(prix);
                newCommande.setDelaisCmd(delai);

                // Call the createCommande method
                createCommande(newCommande);
            } else {
                // User clicked Cancel or closed the dialog
                return; // Exit the method
            }
        }
    }

    // Method to show error dialog
    private void showErrorDialog(String message) {
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle("Error");
        errorDialog.setHeaderText("Invalid Input");
        errorDialog.setContentText(message);
        errorDialog.showAndWait();
    }
    public void updateCommande(Commande commande) {
        // Create a dialog window
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Update Commande");
        dialog.setHeaderText("Enter Updated Commande Details");

        // Create a GridPane layout to organize input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add input fields and labels to the GridPane
        TextField adresseField = new TextField(commande.getAdresseCmd());
        grid.add(new Label("Adresse:"), 0, 0);
        grid.add(adresseField, 1, 0);

        TextField statutField = new TextField(commande.getStatutCmd());
        grid.add(new Label("Statut:"), 0, 1);
        grid.add(statutField, 1, 1);

        TextField prixField = new TextField(String.valueOf(commande.getPrixCmd()));
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(prixField, 1, 2);

        TextField delaiField = new TextField(commande.getDelaisCmd());
        grid.add(new Label("Delai:"), 0, 3);
        grid.add(delaiField, 1, 3);

        // Set the custom GridPane layout to the dialog window
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(grid);

        boolean isValidInput = false;
        while (!isValidInput) {
            // Show the dialog window and wait for user input
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve user input from the input fields
                String adresse = adresseField.getText();
                String statut = statutField.getText();
                float prix;
                try {
                    prix = Float.parseFloat(prixField.getText());
                    if (prix <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    // Display error message for zero or negative price
                    showErrorDialog("Price must be greater than zero.");
                    continue; // Restart the loop
                }
                String delai = delaiField.getText();

                // Validate the updated inputs
                if (!adresse.matches("^\\d{4}\\s.+")) {
                    // Display error message for invalid address format
                    showErrorDialog("Address must start with a valid 4-digit postal code followed by a space and at least one character.");
                    continue; // Restart the loop
                }

                if (delai.isEmpty()) {
                    // Display error message for empty delai
                    showErrorDialog("Delai cannot be empty.");
                    continue; // Restart the loop
                }

                // Input is valid, exit the loop
                isValidInput = true;

                // Update the Commande object with the new values
                commande.setAdresseCmd(adresse);
                commande.setStatutCmd(statut);
                commande.setPrixCmd(prix);
                commande.setDelaisCmd(delai);

                // Perform the update operation
                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String sql = "UPDATE commande SET adressecmd=?, statutcmd=?, prixcmd=?, delaiscmd=? WHERE id=?";
                    try (PreparedStatement statement = conn.prepareStatement(sql)) {
                        statement.setString(1, adresse);
                        statement.setString(2, statut);
                        statement.setFloat(3, prix);
                        statement.setString(4, delai);
                        statement.setInt(5, commande.getId());
                        statement.executeUpdate();
                    }
                    populateTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // User clicked Cancel or closed the dialog
                return; // Exit the method
            }
        }
    }


    public void deleteCommande(Commande commande) {
        // Créer une boîte de dialogue de confirmation
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Commande");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to delete this commande?");

        // Afficher la boîte de dialogue de confirmation et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, effectuer l'opération de suppression
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "DELETE FROM commande WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, commande.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Actualiser la TableView après la suppression
            populateTableView();
        }
    }

}
