package services;

import entities.Categorie;
import javafx.scene.control.Alert;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategorieService {

    private List<Categorie> categories = new ArrayList<>();
    private int nextId = 1;
    private Connection connection;

    public CategorieService() {
        // Initialisez la connexion dans le constructeur
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouterCategorie(String nom) {


        if (nom.isEmpty()) {
            showAlert("Veuillez entrer une catégorie.");
            return; // Sortir de la méthode si le champ est vide
        }

        // Vérifier si la longueur du champ est inférieure à 3 caractères
        if (nom.length() < 3) {
            showAlert("Veuillez entrer une catégorie d'au moins 3 caractères.");
            return; // Sortir de la méthode si la longueur est insuffisante
        }
        // Ajouter la catégorie dans la base de données
        // Exemple avec une requête SQL
        String sql = "INSERT INTO categorie_prod (categorie_prod) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nom);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajouter la catégorie dans la liste en mémoire
        categories.add(new Categorie(nextId++, nom));
    }

    public void modifierCategorie(int id, String nouveauNom) {
        // Modifier la catégorie dans la base de données
        // Exemple avec une requête SQL
        String sql = "UPDATE categorie_prod SET categorie_prod = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nouveauNom);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Modifier la catégorie dans la liste en mémoire
        for (Categorie categorie : categories) {
            if (categorie.getId() == id) {
                categorie.setCategorie_prod(nouveauNom);
                break;
            }
        }
    }

    public void supprimerCategorie(int id) {
        // Supprimer la catégorie de la base de données
        // Exemple avec une requête SQL
        String sql = "DELETE FROM categorie_prod WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Supprimer la catégorie de la liste en mémoire
        categories.removeIf(c -> c.getId() == id);
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


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
