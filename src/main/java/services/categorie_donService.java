/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temcategorie_done file, choose Tools | Temcategorie_dones
 * and open the temcategorie_done in the editor.
 */
package com.example.demo4.services;

//import com.sun.javafx.iio.ImageStorage.ImageType;

import com.example.demo4.entities.categorie_don;
import com.example.demo4.utils.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class categorie_donService implements Icategorie_donService<categorie_don> {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public categorie_donService() {
        cnx = MyDB.getInstance().getCnx();

    }

    @Override
    public void ajoutercategorie_don(categorie_don e) throws SQLException {

        String requete = "INSERT INTO `categorie_don` (`name`) "
                + "VALUES (?);";
        try {
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setString(1, e.getName());




            pst.executeUpdate();
            System.out.println("ev " + e.getName() + " added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public List<String> getAllNameCategorieDon() {
        List<String> nameCategorieDons = new ArrayList<>();
        String requete = "SELECT name FROM categorie_don"; // Modifiez si nécessaire
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                nameCategorieDons.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nameCategorieDons;
    }


    public int getCategoryIDFromName(String categoryName) {
        String requete = "SELECT id_categorie_don FROM categorie_don WHERE name = ?";
        int categoryId = -1; // Par défaut, retourne -1 si la catégorie n'est pas trouvée
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, categoryName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                categoryId = rs.getInt("id_categorie_don");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return categoryId;
    }


    @Override
    public void modifiercategorie_don(categorie_don e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategorie_dones.
        String req = "UPDATE categorie_don SET name = ? where id_categorie_don = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());




        ps.setInt(2, e.getId_categorie_don());
        ps.executeUpdate();
    }

    @Override
    public void supprimercategorie_don(categorie_don e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategorie_dones.
        String req = "delete from categorie_don where id_categorie_don = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId_categorie_don());
        ps.executeUpdate();
        System.out.println("ev with id= " + e.getId_categorie_don() + "  is deleted successfully");
    }





    @Override
    public List<categorie_don> recuperercategorie_don() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temcategorie_dones.

        List<categorie_don> categorie_don = new ArrayList<>();
        String s = "select * from categorie_don";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            categorie_don e = new categorie_don();
            e.setName(rs.getString("name"));





            e.setId_categorie_don(rs.getInt("id_categorie_don"));

            categorie_don.add(e);

        }
        return categorie_don;
    }

    public categorie_don FetchOneev(int id) {
        categorie_don ev = new categorie_don();
        String requete = "SELECT * FROM `categorie_don` where id_categorie_don = " + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new categorie_don(rs.getInt("id_categorie_don"), rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(categorie_donService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }

    public ObservableList<categorie_don> Fetchevs() {
        ObservableList<categorie_don> evs = FXCollections.observableArrayList();
        String requete = "SELECT * FROM `categorie_don`";
        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                evs.add(new categorie_don(rs.getInt("id_categorie_don"), rs.getString("name")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(categorie_donService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return evs;
    }


    

    public ObservableList<categorie_don> chercherev(String chaine) {
        String sql = "SELECT * FROM categorie_don WHERE (name LIKE ?   ) order by name ";
        //Connection cnx= Maconnexion.getInstance().getCnx();
        String ch = "%" + chaine + "%";
        ObservableList<categorie_don> myList = FXCollections.observableArrayList();
        try {

            Statement ste = cnx.createStatement();
            // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);


            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                categorie_don e = new categorie_don();

                e.setName(rs.getString("name"));





                e.setId_categorie_don(rs.getInt("id_categorie_don"));

                myList.add(e);
                System.out.println("ev trouvé! ");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }

    public List<categorie_don> trierev()throws SQLException {
        List<categorie_don> categorie_don = new ArrayList<>();
        String s = "select * from categorie_don order by name ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            categorie_don e = new categorie_don();
            e.setName(rs.getString("name"));





            e.setId_categorie_don(rs.getInt("id_categorie_don"));
            categorie_don.add(e);
        }
        return categorie_don;
    }
   

}
