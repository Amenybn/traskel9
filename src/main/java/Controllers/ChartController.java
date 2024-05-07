package controllers;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import services.CategorieService;

public class ChartController {

    @FXML
    private Pane pnlCustomer;
    @FXML
    private Button btnCustomers;


    @FXML
    public void initialize() {
        loadChartData();
    }

    private void loadChartData() {
        // Récupérer la liste des catégories
        List<String> categories = CategorieService.chargerCategories();

        // Créer un ensemble de données par défaut
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

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
            dataset.addValue(count, "Produits", category);
        }

        // Créer le graphique
        JFreeChart barChart = ChartFactory.createBarChart(
                "Nombre de produits par catégorie", // Titre du graphique
                "Catégorie",                        // Axe des X
                "Nombre de produits",               // Axe des Y
                dataset);                           // Données

        ChartPanel chartPanel = new ChartPanel(barChart);

        // Créer un SwingNode pour intégrer le ChartPanel dans JavaFX
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(chartPanel);

        // Ajouter le SwingNode au conteneur JavaFX
        pnlCustomer.getChildren().add(swingNode);
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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/traskel", "root", "");
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
    private void downloadChart(ActionEvent event) {
        // Créer un document PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Créer un ensemble de données par défaut
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Récupérer la liste des catégories
            List<String> categories = CategorieService.chargerCategories();

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
                dataset.addValue(count, "Produits", category);
            }

            // Créer le graphique
            JFreeChart barChart = ChartFactory.createBarChart(
                    "Nombre de produits par catégorie", // Titre du graphique
                    "Catégorie",                        // Axe des X
                    "Nombre de produits",               // Axe des Y
                    dataset);                           // Données

            // Capturer l'image du graphique
            BufferedImage chartImage = barChart.createBufferedImage(800, 600);

            // Convertir l'image du graphique en PDImageXObject
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, chartImage);

            // Ajouter l'image du graphique à la page PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, 100, 500, pdImage.getWidth() / 2, pdImage.getHeight() / 2);
            }

            // Afficher une boîte de dialogue pour permettre à l'utilisateur de choisir l'emplacement de sauvegarde du fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le graphique");

            // Définir le filtre pour ne montrer que les fichiers PDF
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            // Afficher la boîte de dialogue et obtenir le fichier sélectionné
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                // Enregistrer le document PDF dans le fichier sélectionné par l'utilisateur
                document.save(file);
                System.out.println("Le graphique a été enregistré avec succès dans : " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
