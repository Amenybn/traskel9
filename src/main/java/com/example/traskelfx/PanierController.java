package com.example.traskelfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.Optional;

public class PanierController {

    @FXML
    private TableView<Panier> tableView;

    @FXML
    private TableColumn<Panier, Integer> idColumn;

    @FXML
    private TableColumn<Panier, Integer> nbrProdsColumn;

    @FXML
    private TableColumn<Panier, Float> totalPrixColumn;

    private static final String url = "jdbc:mysql://localhost:3306/javat";
    private static final String username = "root";
    private static final String password = "";

    public void initialize() {
        nbrProdsColumn.setCellValueFactory(new PropertyValueFactory<>("nbrProds"));
        totalPrixColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrix"));

        addButtonToTable("View", this::viewPanierDetails, "View");
        addButtonToTable("Update", this::updatePanier, "Update");
        addButtonToTable("Delete", this::deletePanier, "Delete");

        populateTableView();
    }


    private void populateTableView() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Panier")) {

            ObservableList<Panier> paniers = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Panier panier = new Panier();
                panier.setId(resultSet.getInt("id"));
                panier.setNbrProds(resultSet.getInt("nbr_prods"));
                panier.setTotalPrix(resultSet.getFloat("total_prix"));
                // You may need to populate the produits collection here as well
                paniers.add(panier);
            }
            tableView.setItems(paniers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable(String buttonText, ButtonActionHandler actionHandler, String columnHeader) {
        TableColumn<Panier, Void> column = new TableColumn<>(columnHeader);
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

    private void viewPanierDetails(Panier panier) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Panier Details");
        alert.setHeaderText(null);
        alert.setContentText("ID: " + panier.getId() + "\n"
                + "Nombre de produits: " + panier.getNbrProds() + "\n"
                + "Prix total: " + panier.getTotalPrix());
        alert.showAndWait();
    }

    interface ButtonActionHandler {
        void handle(Panier panier);
    }

    public void createPanier(Panier panier) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO panier (nbr_prods, total_prix) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, panier.getNbrProds());
                statement.setFloat(2, panier.getTotalPrix());
                statement.executeUpdate();
            }
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPanier() {
        // Create a dialog window
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Add Panier");
        dialog.setHeaderText("Enter Panier Details");

        // Create a GridPane layout to organize input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add input fields and labels to the GridPane
        TextField nbrProdsField = new TextField();
        grid.add(new Label("Nombre de Produits:"), 0, 0);
        grid.add(nbrProdsField, 1, 0);

        TextField totalPrixField = new TextField();
        grid.add(new Label("Total Prix:"), 0, 1);
        grid.add(totalPrixField, 1, 1);

        // Set the custom GridPane layout to the dialog window
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(grid);

        // Show the dialog window and wait for user input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Retrieve user input from the input fields
            int nbrProds = Integer.parseInt(nbrProdsField.getText());
            float totalPrix = Float.parseFloat(totalPrixField.getText());

            // Now, create the Panier object and insert it into the database
            Panier newPanier = new Panier();
            newPanier.setNbrProds(nbrProds);
            newPanier.setTotalPrix(totalPrix);

            // Call the createPanier method
            createPanier(newPanier);
        }
    }
    public void updatePanier(Panier panier) {
        // Create a dialog window
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Update Panier");
        dialog.setHeaderText("Enter Updated Panier Details");

        // Create a GridPane layout to organize input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add input fields and labels to the GridPane
        TextField nbrProdsField = new TextField(String.valueOf(panier.getNbrProds()));
        grid.add(new Label("Nombre de Produits:"), 0, 0);
        grid.add(nbrProdsField, 1, 0);

        TextField totalPrixField = new TextField(String.valueOf(panier.getTotalPrix()));
        grid.add(new Label("Total Prix:"), 0, 1);
        grid.add(totalPrixField, 1, 1);

        // Set the custom GridPane layout to the dialog window
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(grid);

        // Show the dialog window and wait for user input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Retrieve user input from the input fields
            int nbrProds = Integer.parseInt(nbrProdsField.getText());
            float totalPrix = Float.parseFloat(totalPrixField.getText());

            // Update the Panier object with the new values
            panier.setNbrProds(nbrProds);
            panier.setTotalPrix(totalPrix);

            // Perform the update operation
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE panier SET nbr_prods=?, total_prix=? WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, nbrProds);
                    statement.setFloat(2, totalPrix);
                    statement.setInt(3, panier.getId());
                    statement.executeUpdate();
                }
                populateTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletePanier(Panier panier) {
        // Create a confirmation dialog
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Panier");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to delete this panier?");

        // Display the confirmation dialog and wait for user input
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed the deletion, perform the delete operation
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "DELETE FROM panier WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, panier.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Refresh the TableView after deletion
            populateTableView();
        }
    }


}
