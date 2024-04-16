package utils;

import entities.Categorie;
import entities.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
// les informations de con
    static String URL="jdbc:mysql://localhost:3306/traskel";

    static String USERNAME="root";
    static String PASSWORD="";
    static String driver ="com.mysql.cj.jdbc.Driver"; // contient le nom de la classe du pilote JDBC MySQL
    Connection connection; //exécuter req SQL et récupérer le résultat

    static MyDatabase instance;


    //constructeur
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


    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie_prod";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // prepareStatement un objet utilisé pour préparer une instruction SQL
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
/* pom.xml est un fichier de configuration utilisé par Apache Maven,
un outil de gestion de projet utilisé principalement pour les projets Java.
 Gestion des dépendances
 Configuration du projet
 Gestion des plugins
 Configuration de la structure du projet*/