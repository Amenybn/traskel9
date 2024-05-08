/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategorie_done file, choose Tools | Temcategorie_dones
 * and open the temcategorie_done in the editor.
 */
package com.example.demo4.entities;

import java.sql.Date;

/**
 *
 * @author asus
 */
public class categorie_don {

   
        private int id_categorie_don;



    private String name;


    public categorie_don() {
    }

    public categorie_don(int id_categorie_don, String name) {
        this.id_categorie_don = id_categorie_don;



        this.name = name;



    }
    public categorie_don(String name) {



        this.name = name;



    }
    
    
     public categorie_don(int id_categorie_don) {
        this.id_categorie_don = id_categorie_don;



        this.name = name;


        
    }
    
    
     //****************** getters ****************

    public int getId_categorie_don() {
        return id_categorie_don;
    }

    public String getName() {
        return name;
    }





    
    
    //****************** setters ****************

    public void setId_categorie_don(int id_categorie_don) {
        this.id_categorie_don = id_categorie_don;
    }

    public void setName(String name) {
        this.name = name;
    }










    @Override
    public String toString() {
        return "categorie_don{" + "id_categorie_don=" + id_categorie_don+  ", name=" + name +'}';
    }
    
    
    
    
    
    
}
