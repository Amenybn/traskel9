/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4;
import com.example.demo4.entities.don;
import com.example.demo4.services.donService;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatisticsController implements Initializable {

    @FXML
    private ImageView GoBackBtn;
    @FXML
    private PieChart StatsChart;

    donService rs = new donService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            displayStatistics();
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void navigation(ActionEvent abonn) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("ListProductsAffichage.fxml"));
            StatsChart.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void displayStatistics() throws SQLException {
        List<don> dons = rs.recupererdon();

        // Regrouper les dons par adresse et compter le nombre de dons pour chaque adresse
        Map<String, Long> donsParAdresse = dons.stream()
                .collect(Collectors.groupingBy(don::getAdresse_don, Collectors.counting()));

        // Créer une liste de données pour le PieChart
        List<PieChart.Data> pieChartData = donsParAdresse.entrySet().stream()
                .map(entry -> new PieChart.Data(entry.getKey() + " (" + entry.getValue() + " don(s))", entry.getValue()))
                .collect(Collectors.toList());

        // Afficher les données dans le PieChart
        StatsChart.setData(FXCollections.observableArrayList(pieChartData));

        // Ajouter des fonctionnalités d'interactivité pour afficher des informations supplémentaires lors du clic sur les données
        StatsChart.getData().forEach(data -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                String adresse = data.getName().split(" ")[0];
                long nombreDons = donsParAdresse.get(adresse);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Statistiques par adresse");
                alert.setHeaderText("Adresse : " + adresse);
                alert.setContentText("Nombre de dons ajoutés : " + nombreDons);
                alert.showAndWait();
            });
        });
    }


}