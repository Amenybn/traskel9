/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4.test;

import java.sql.Date;
import java.sql.SQLException;

import com.example.demo4.services.donService;


/**
 *
 * @author asus
 */
public class test {
    
      public static void main(String[] args) {   
          
          Date d=Date.valueOf("2022-06-11");
          Date d1=Date.valueOf("2020-04-12");
        try {
            //kifeh ya9ra el orde fel base de donnée , kifeh 3raf nom ev bch n3amarha f nom 

            
            

            //ps.reservation(p);
          //  ps.reservation(p1);
           // ps.reservation(p2);

            //ps.reservation(p2);
            System.out.println("");
            donService ab = new donService();
            //ab.ajouterdon(e1);
            //ab.ajouterdon(e2);
           // ab.ajouterdon(e3);
            //ab.ajouter(p);
            //ab.modifierdon(e);
            //ab.supprimerdon(e3);
            System.out.println(ab.recupererdon());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
