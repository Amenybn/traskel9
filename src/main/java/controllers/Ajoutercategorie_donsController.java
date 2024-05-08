/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategorie_done file, choose Tools | Temcategorie_dones
 * and open the temcategorie_done in the editor.
 */
package com.example.demo4;


import com.example.demo4.entities.categorie_don;
import com.example.demo4.entities.don;
import com.example.demo4.services.categorie_donService;
import com.example.demo4.services.donService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class Ajoutercategorie_donsController implements Initializable {


    @FXML
    private TextField nameField;
    @FXML
    private ImageView QrCode;


  
    @FXML
    private TableView<categorie_don> categorie_donTv;
    @FXML
    private TableColumn<categorie_don, String> nomevTv;







    donService Evv=new donService();

 
    ObservableList<categorie_don> evs;
    categorie_donService Ev=new categorie_donService();

    @FXML
    private TableView<don> donTv;
    @FXML
    private TableColumn<don, String> nomevTvv;
    @FXML
    private TableColumn<don, String> typeevTv;
    @FXML
    private TableColumn<don, String> imageevTv;
    @FXML
    private TableColumn<don, String> dateevTv;
    @FXML
    private TableColumn<don, String> descriptionevTv;
    @FXML
    private TableColumn<don, Integer> quantite_donTv;

    @FXML
    private TableColumn<don, Integer> idevTv;
    
    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;



    /**
     * Initializes the controller class.
     */
@Override
public void initialize(URL url, ResourceBundle rb) {



    //idLabel.setText("");

    getevss();
}




    
  


    

    


     








    public void getevss() {
        try {
            // TODO
            List<don> don = Evv.recupererdon();
            ObservableList<don> olp = FXCollections.observableArrayList(don);
            donTv.setItems(olp);
            nomevTvv.setCellValueFactory(new PropertyValueFactory<>("adresse_don"));
            typeevTv.setCellValueFactory(new PropertyValueFactory<>("point_don"));
            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image_don"));

            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("descrp_don"));
            quantite_donTv.setCellValueFactory(new PropertyValueFactory<>("quantite_don"));

            idevTv.setCellValueFactory(new PropertyValueFactory<>("id_categorie_don"));
            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }
    @FXML
    private void supprimerdon(ActionEvent ev) {
        don e = donTv.getItems().get(donTv.getSelectionModel().getSelectedIndex());
        try {
            Evv.supprimerdon(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterdonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("don delete");
        alert.setContentText("don deleted successfully!");
        alert.showAndWait();

        getevss();
    }


     private void populateTable(ObservableList<categorie_don> branlist){
       categorie_donTv.setItems(branlist);
   
       }

    @FXML
    private void navigation(ActionEvent abonn) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("DashCategories.fxml"));
            donTv.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    private void choisirev(MouseEvent ev) throws IOException {
        don e = donTv.getItems().get(donTv.getSelectionModel().getSelectedIndex());
        idmodifierField.setText(String.valueOf(e.getId()));



        String path = e.getImage_don();
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        imageview.setImage(img);


        //////// qr
        String filename = Evv.GenerateQrev(e);
        System.out.println("filename lenaaa " + filename);
        String path1="C:\\xampp\\htdocs\\imgQr\\qrcode"+filename;
        File file1=new File(path1);
        Image img1 = new Image(file1.toURI().toString());
        //Image image = new Image(getClass().getResourceAsStream("src/utils/img/" + filename));
        QrCode.setImage(img1);
    }


}


    





    

