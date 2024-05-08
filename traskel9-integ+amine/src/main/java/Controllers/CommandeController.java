package Controllers;

import entities.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    private static final String url = "jdbc:mysql://localhost:3306/traskel_bd";
    private static final String username = "root";
    private static final String password = "";

    public void initialize() {
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseCmd"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutCmd"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixCmd"));
        delaiColumn.setCellValueFactory(new PropertyValueFactory<>("delaisCmd"));

        addButtonToTable("View", this::viewCommandeDetails, "View");
        addButtonToTable("Update", this::updateCommande, "Update");
        addButtonToTable("Delete", this::deleteCommande, "Delete");
        // Add a button to download PDF
        TableColumn<Commande, Void> pdfColumn = new TableColumn<>("PDF");
        Callback<TableColumn<Commande, Void>, TableCell<Commande, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Commande, Void> call(final TableColumn<Commande, Void> param) {
                final TableCell<Commande, Void> cell = new TableCell<>() {
                    private final Button downloadButton = new Button("Download PDF");

                    {
                        downloadButton.setOnAction(event -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            // Call method to generate PDF
                            generatePDF(commande);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(downloadButton);
                        }
                    }
                };
                return cell;
            }
        };
        pdfColumn.setCellFactory(cellFactory);
        tableView.getColumns().add(pdfColumn);

        populateTableView();
    }
    private void generatePDF(Commande commande) {
        String fileName = "commande_" + commande.getId() + ".pdf";
        File file = new File(fileName);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("PDF Content for Commande: " + commande.getId()); // Example text
            contentStream.endText();
            contentStream.close();

            document.save(file);
            document.close();

            openFile(file);
            System.out.println("PDF generated and opened: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error generating or opening the PDF file.");
            e.printStackTrace();
        }
    }
    public double calculateAverageOrderValue() {
        ObservableList<Commande> items = tableView.getItems();
        if (items.isEmpty()) {
            return 0;
        }
        return calculateTotalRevenue() / items.size();
    }
    public double calculateTotalRevenue() {
        double total = 0;
        for (Commande cmd : tableView.getItems()) {
            total += cmd.getPrixCmd();
        }
        return total;
    }
    public void generateStatisticsPDF() {
        String dest = "statistics.pdf"; // PDF file path
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adding a title
            Paragraph title = new Paragraph("Order Statistics")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16)
                    .setFontColor(ColorConstants.BLUE);
            document.add(title);

            // Total Revenue
            double totalRevenue = calculateTotalRevenue();
            Paragraph revenue = new Paragraph("Total Revenue: $" + String.format("%.2f", totalRevenue))
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(0, 153, 204));
            document.add(revenue);

            // Average Order Value
            double averageValue = calculateAverageOrderValue();
            Paragraph average = new Paragraph("Average Order Value: $" + String.format("%.2f", averageValue))
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(153, 204, 0));
            document.add(average);

            // Orders by Status
            Map<String, Integer> statusCounts = countOrdersByStatus();
            Table table = new Table(2);
            table.addCell("Status");
            table.addCell("Count");
            statusCounts.forEach((status, count) -> {
                table.addCell(status)
                        .setFontColor(ColorConstants.BLACK);
                table.addCell(String.valueOf(count))
                        .setFontColor(ColorConstants.BLACK);
            });
            document.add(table);

            document.close();
            System.out.println("PDF Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Map<String, Integer> countOrdersByStatus() {
        Map<String, Integer> statusCounts = new HashMap<>();
        for (Commande cmd : tableView.getItems()) {
            statusCounts.merge(cmd.getStatutCmd(), 1, Integer::sum);
        }
        return statusCounts;
    }
    private void openFile(File file) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists() && desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
            }
        }
    }

    private void populateTableView() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM commande")) {

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
            String sql = "INSERT INTO commande (adresse_cmd, statutcmd, prixcmd, delaiscmd) VALUES (?, ?, ?, ?)";
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
                    String sql = "UPDATE commande SET adresse_cmd=?, statutcmd=?, prixcmd=?, delaiscmd=? WHERE id=?";
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

    private VBox createCommandeNode(Commande commande) {
        VBox commandeBox = new VBox(10);
        commandeBox.getChildren().addAll(
                new Label("ID: " + commande.getId()),
                new Label("Address: " + commande.getAdresseCmd()),
                new Label("Status: " + commande.getStatutCmd()),
                new Label("Price: " + commande.getPrixCmd() + " DT"),
                new Label("Delivery: " + commande.getDelaisCmd())
        );
        return commandeBox;
    }

    private List<Commande> loadCommandesFromDatabase() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Commande commande = new Commande(
                        resultSet.getInt("id"),
                        resultSet.getString("adresse_cmd"),
                        resultSet.getString("statut_cmd"),
                        resultSet.getFloat("prix_cmd"),
                        resultSet.getString("delais_cmd")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    private void loadCommandes() {
        List<Commande> commandes = loadCommandesFromDatabase();
        TableView<Object> commandeListView = null;
        commandeListView.setItems(FXCollections.observableArrayList());
        commandes.forEach(commande -> {
            VBox commandeNode = createCommandeNode(commande);
            commandeListView.getItems().add(commandeNode);
        });
    }


}
