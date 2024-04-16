package com.example.traskelfx;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Panier {

    private Integer id;

    private Integer nbrProds;
    private Float totalPrix;

    private Collection<Produit> produits = new ArrayList<>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNbrProds() {
        return nbrProds;
    }

    public void setNbrProds(Integer nbrProds) {
        this.nbrProds = nbrProds;
    }

    public Float getTotalPrix() {
        return totalPrix;
    }

    public void setTotalPrix(Float totalPrix) {
        this.totalPrix = totalPrix;
    }

    public Collection<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Collection<Produit> produits) {
        this.produits = produits;
    }

    public void addProduit(Produit produit) {
        produits.add(produit);
        produit.setPanier(this);
    }

    public void removeProduit(Produit produit) {
        produits.remove(produit);
        produit.setPanier(null);
    }


    private static final String url = "jdbc:mysql://localhost:3306/javat";
    private static final String username = "root";
    private static final String password = "";

    public void createPanier(Panier panier) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO panier (nbr_prods, total_prix) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, panier.getNbrProds());
                statement.setFloat(2, panier.getTotalPrix());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Panier readPanier(int id) {
        Panier panier = null;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM panier WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        panier = new Panier();
                        panier.setId(resultSet.getInt("id"));
                        panier.setNbrProds(resultSet.getInt("nbr_prods"));
                        panier.setTotalPrix(resultSet.getFloat("total_prix"));
                        // Pour charger la collection de produits, vous pouvez implémenter une méthode séparée
                        // ou récupérer les produits en utilisant une requête SQL supplémentaire
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panier;
    }

    public void updatePanier(Panier panier) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE panier SET nbr_prods=?, total_prix=? WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, panier.getNbrProds());
                statement.setFloat(2, panier.getTotalPrix());
                statement.setInt(3, panier.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePanier(int id) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM panier WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
