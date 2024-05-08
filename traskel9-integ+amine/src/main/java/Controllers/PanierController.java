package Controllers;
import entities.Panier;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class PanierController {
    @FXML
    private TableView<Produit> productsTableView;
    @FXML
    private TableColumn<Produit, Integer> idColumn;
    @FXML
    private TableColumn<Produit, String> nameColumn;
    @FXML
    private TableColumn<Produit, String> descriptionColumn;
    @FXML
    private TableColumn<Produit, Double> priceColumn;
    @FXML
    private TableColumn<Produit, String> typeColumn;
    @FXML
    private TableView<Panier> tableView;


    @FXML
    private TableColumn<Panier, Integer> nbrProdsColumn;

    @FXML
    private TableColumn<Panier, Float> totalPrixColumn;
    @FXML
    private TableColumn<Panier, Integer> livraisonIdColumn;

    @FXML
    private TableColumn<Panier, String> productIdsColumn;

    private static final String url = "jdbc:mysql://localhost:3306/traskel_bd";
    private static final String username = "root";
    private static final String password = "";

    public void initialize() {
        updateTotalPrice(0.0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom_prod"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descrp_prod"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix_prod"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_prod"));

        populateProductsTable();
    }
    private void populateProductsTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM produit WHERE FIND_IN_SET(id, (SELECT productIds FROM panier WHERE id = 1))");

            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom_prod"),
                        rs.getString("descrp_prod"),
                        rs.getString("photo_prod"),
                        rs.getDouble("prix_prod"),
                        rs.getString("type_prod")
                );
                produits.add(produit);
                calculateAndUpdateTotalPrice();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productsTableView.setItems(produits);
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
                panier.setLivraisonId(resultSet.getInt("livraison_id"));  // Ensure null handling if necessary
                panier.setProductIds(resultSet.getString("productIds"));  // Ensure null handling if necessary
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

    @FXML
    private void goToPanier(ActionEvent event) {
        try {
            // Load the Panier view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPanier.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleReturn(ActionEvent event) {
        try {
            // Load the main application or previous page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListProducts.fxml")); // Adjust path as necessary
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void calculateAndUpdateTotalPrice() {
        System.out.println("Calculating total price...");
        ObservableList<Produit> products = productsTableView.getItems();
        double totalPrice = 0.0;
        for (Produit product : products) {
            totalPrice += product.getPrix_prod();
        }
        System.out.println("Computed total price: $" + totalPrice);
        updateTotalPrice(totalPrice);
    }

    @FXML
    private Label totalPriceLabel;
    public void updateTotalPrice(double totalPrice) {
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
        System.out.println("Total price updated to: $" + totalPrice);
    }

}
