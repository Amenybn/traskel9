package Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import services.ServiceCategorie;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";

    @FXML
    private Button btnCustomers;
    @FXML
    private Pane pnlCustomer;
    @FXML
    public void initialize() {
        loadChartData();
    }

    private void loadChartData() {
        // Récupérer la liste des catégories
        List<String> categories = ServiceCategorie.chargerCategories();

        // Créer un HashMap pour stocker le nombre de produits par catégorie
        ObservableList<BarChart.Data<String, Number>> chartData = FXCollections.observableArrayList();

        // Récupérer la liste des produits depuis la base de données
        List<String> products = loadProductsFromDatabase();

        // Compter le nombre de produits dans chaque catégorie
        for (String category : categories) {
            int count = 0;
            for (String product : products) {
                if (getProductCategory(product).equals(category)) {
                    count++;
                }
            }
            chartData.add(new BarChart.Data<>(category, count));
        }

        // Créer la série de données pour la charte
        BarChart.Series<String, Number> series = new BarChart.Series<>(chartData);

        // Ajouter la série de données à la charte
        barChart.getData().add(series);
    }

    // Fonction pour charger les produits depuis la base de données
    private List<String> loadProductsFromDatabase() {
        List<String> products = new ArrayList<>();
        String query = "SELECT nom_prod FROM produit";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traskel", "root", "");
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                products.add(resultSet.getString("nom_prod"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Fonction pour récupérer la catégorie d'un produit

    public static String getProductCategory(String productName) {
        String category = null;
        String query = "SELECT type_prod FROM produit WHERE nom_prod = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, productName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = resultSet.getString("type_prod");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
    }


    @FXML
    private void downloadChart() {
        // Créer un sélecteur de fichier pour choisir l'emplacement de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le graphique");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PNG", "*.png")
        );
        File file = fileChooser.showSaveDialog(barChart.getScene().getWindow());

        if (file != null) {
            // Capturer l'image du graphique
            WritableImage image = barChart.snapshot(new SnapshotParameters(), null);

            // Sauvegarder l'image dans le fichier
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
