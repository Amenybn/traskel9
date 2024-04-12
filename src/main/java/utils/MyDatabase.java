package utils;

import entities.Categorie;
import entities.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDatabase {

    static String URL="jdbc:mysql://localhost:3306/traskel";

    static String USERNAME="root";
    static String PASSWORD="";
    static String driver ="com.mysql.cj.jdbc.Driver";
    Connection connection;

    static MyDatabase instance;

  /*  public static Connection getCon(){
        Connection con = null;
        try {
            Class.forName(driver);
            try {
                con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return con;
    }*/

    private MyDatabase(){
        try {
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance(){
        if (instance==null){
            instance= new MyDatabase();
        }
      return instance;
    }


    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Produit produit = new Produit();
                produit.setId(resultSet.getInt("id"));
                produit.setNom_prod(resultSet.getString("nom_prod"));
                produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                produit.setPhoto_prod(resultSet.getString("photo_prod"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }


    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie_prod";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(resultSet.getInt("id"));
                categorie.setCategorie_prod(resultSet.getString("categorie_prod"));

                categories.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }









    public Connection getConnection() {
        return connection;
    }
}
