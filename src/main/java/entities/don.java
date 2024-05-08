/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4.entities;


/**
 *
 * @author asus
 */
public class don extends categorie_don {

   
        private int id;
        private int quantite_don;



    private String adresse_don,point_don,image_don,descrp_don;
    public int id_categorie_don;
    public categorie_don categorie_don;


    public don() {
    }

    public don(int id, int quantite_don, String adresse_don, String point_don, String image_don, String descrp_don,int id_categorie_don) {
        this.id = id;
        this.quantite_don=quantite_don;


        this.adresse_don = adresse_don;
        this.point_don = point_don;
        this.image_don = image_don;
        this.descrp_don = descrp_don;
        this.id_categorie_don = id_categorie_don;

    }
    public don(int quantite_don, String adresse_don, String point_don, String image_don, String descrp_don,int id_categorie_don) {
        this.quantite_don=quantite_don;


        this.adresse_don = adresse_don;
        this.point_don = point_don;
        this.image_don = image_don;
        this.descrp_don = descrp_don;
        this.id_categorie_don = id_categorie_don;

    }
    
    

    
    
     //****************** getters ****************

    public int getId() {
        return id;
    }

    public String getAdresse_don() {
        return adresse_don;
    }

    public String getPoint_don() {
        return point_don;
    }

    public String getImage_don() {
        return image_don;
    }

    public String getDescrp_don() {
        return descrp_don;
    }


    
    
    //****************** setters ****************

    public void setId(int id) {
        this.id = id;
    }

    public void setAdresse_don(String adresse_don) {
        this.adresse_don = adresse_don;
    }

    public void setPoint_don(String point_don) {
        this.point_don = point_don;
    }

    public void setImage_don(String image_don) {
        this.image_don = image_don;
    }

    public void setDescrp_don(String descrp_don) {
        this.descrp_don = descrp_don;
    }



    public int getQuantite_don() {
        return quantite_don;
    }

    public void setQuantite_don(int quantite_don) {
        this.quantite_don = quantite_don;
    }

    public int getId_categorie_don() {
        return id_categorie_don;
    }

    public categorie_don getcategorie_don() {
        return categorie_don;
    }
    public void setCategorie_don(categorie_don categorie_don) {
        this.categorie_don = categorie_don;
    }
    public void setId_categorie_don(int id_categorie_don) {
        this.id_categorie_don = id_categorie_don;
    }
    
    

    @Override
    public String toString() {
        return "don{" + "id=" + id+ ", quantite_don=" + quantite_don +  ", adresse_don=" + adresse_don + ", point_don=" + point_don + ", image_don=" + image_don + ", descrp_don=" + descrp_don + ", id_reclamation=" + id_categorie_don +  '}';
    }
    
    
    
    
    
    
}
