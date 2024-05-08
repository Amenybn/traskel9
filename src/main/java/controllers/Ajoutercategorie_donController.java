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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;















/**
 * FXML Controller class
 *
 * @author asus
 */
public class Ajoutercategorie_donController implements Initializable {


    @FXML
    private TextField nameField;



  
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
    getevs();

}




    
  

          @FXML
    private void ajoutercategorie_don(ActionEvent ev) {
    
         int part=0;
        if ((nameField.getText().length() == 0)  ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        }

       else{     

        categorie_don e = new categorie_don();


        e.setName(nameField.getText());








        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("categorie_don add");
            alert.setContentText("categorie_don added successfully!");
            alert.showAndWait();      
        try {
            Ev.ajoutercategorie_don(e);
            reset();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
        getevs();

        

    }}
    
    //fin d ajout d'un categorie_don
    private void reset() {
        nameField.setText("");




    }
    
   public void getevs() {  
         try {
            // TODO
            List<categorie_don> categorie_don = Ev.recuperercategorie_don();
            ObservableList<categorie_don> olp = FXCollections.observableArrayList(categorie_don);
            categorie_donTv.setItems(olp);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));





           // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }//get evs

     
     @FXML
   private void modifiercategorie_don(ActionEvent ev) throws SQLException {
        categorie_don e = new categorie_don();
        e.setId_categorie_don(Integer.parseInt(idmodifierField.getText()));
        e.setName(nameField.getText());




         Ev.modifiercategorie_don(e);
        reset();
        getevs();

    }

    @FXML
    private void supprimercategorie_don(ActionEvent ev) {
           categorie_don e = categorie_donTv.getItems().get(categorie_donTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimercategorie_don(e);
        } catch (SQLException ex) {
            Logger.getLogger(Ajoutercategorie_donController.class.getName()).log(Level.SEVERE, null, ex);
        }   
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("categorie_don delete");
        alert.setContentText("categorie_don deleted successfully!");
        alert.showAndWait();        
        getevs();

    }



  
    @FXML
    //ta3 tablee bch nenzel 3ala wehed ya5tarou w yet3abew textfield
    private void choisirev(MouseEvent ev) throws IOException {
        categorie_don e = categorie_donTv.getItems().get(categorie_donTv.getSelectionModel().getSelectedIndex());
        //idLabel.setText(String.valueOf(e.getid_categorie_don()));
        idmodifierField.setText(String.valueOf(e.getId_categorie_don()));
        nameField.setText(e.getName());







            
    }








    @FXML
    private void rechercherev(KeyEvent ev) {
        
        categorie_donService bs=new categorie_donService(); 
        categorie_don b= new categorie_don();
        ObservableList<categorie_don>filter= bs.chercherev(rechercher.getText());
        populateTable(filter);
    }
     private void populateTable(ObservableList<categorie_don> branlist){
       categorie_donTv.setItems(branlist);
   
       }
    @FXML
    private void navigation(ActionEvent abonn) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(getClass().getResource("DashCategoriesdon.fxml"));
            categorie_donTv.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
  


    }


    





    

