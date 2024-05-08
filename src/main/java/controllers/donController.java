/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.don;

import java.io.File;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.demo4.services.donService;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class donController implements Initializable {

    int idev;
    @FXML
    private Label nomevLabel;
    @FXML
    private Label typeevLabel;
    @FXML
    private Label descriptionevLabel;
    @FXML
    private Label dateevLabel;
    @FXML
    private Button participerevButton;
    @FXML
    private Label nb_reservationsLabel;
    

    @FXML
    private TextField idevF;
    @FXML
    private TextField iduserF;
    
    donService Ev=new donService();
    @FXML
    private ImageView imageview;
    @FXML
    private Label reservationComplet;
    @FXML
    private TextField idPartField;
    @FXML
    private Button annulerButton;
    @FXML
    private Button likeButton;
     @FXML
    private Button deslikeButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        idevF.setVisible(false);





                

    }    
    private don eve=new don();
    
    public void setdon(don e) {
        this.eve=e;
        nomevLabel.setText(e.getAdresse_don());
        typeevLabel.setText(e.getPoint_don());
        descriptionevLabel.setText(e.getDescrp_don());

        nb_reservationsLabel.setText(String.valueOf(e.getQuantite_don()));
        idevF.setText(String.valueOf(e.getId()));
        iduserF.setText(String.valueOf(1));
         String path = e.getImage_don();
         File file=new File(path);
         Image img = new Image(file.toURI().toString());
         imageview.setImage(img);

    }
    public void setIdev(int idev){
        this.idev=idev;
    }



    



    
    
}
