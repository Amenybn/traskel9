package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Panier {
    private Integer id;
    private Integer nbrProds;
    private Float totalPrix;
    private String productIds = String.valueOf(new ArrayList<>());
    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";
    private int livraisonId;

    public Panier() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getNbrProds() { return nbrProds; }
    public void setNbrProds(Integer nbrProds) { this.nbrProds = nbrProds; }
    public Float getTotalPrix() { return totalPrix; }
    public void setTotalPrix(Float totalPrix) { this.totalPrix = totalPrix; }
    public void addProductId(int productId) {
        if (productIds == null || productIds.isEmpty()) {
            productIds = String.valueOf(productId);
        } else {
            productIds += "," + productId;
        }
    }
    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getProductIds() {
        return productIds;
    }
    // Database Operations
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void createPanier(Panier panier) {
        String sql = "INSERT INTO panier (nbr_prods, total_prix) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, panier.getNbrProds());
            statement.setFloat(2, panier.getTotalPrix());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Panier readPanier(int id) {
        String sql = "SELECT * FROM panier WHERE id=?";
        Panier panier = null;
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    panier = new Panier();
                    panier.setId(resultSet.getInt("id"));
                    panier.setNbrProds(resultSet.getInt("nbr_prods"));
                    panier.setTotalPrix(resultSet.getFloat("total_prix"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panier;
    }

    public void updatePanier(Panier panier) {
        String sql = "UPDATE panier SET nbr_prods=?, total_prix=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, panier.getNbrProds());
            statement.setFloat(2, panier.getTotalPrix());
            statement.setInt(3, panier.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePanier(int id) {
        String sql = "DELETE FROM panier WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLivraisonId(int livraisonId) {this.livraisonId = livraisonId;
    }
    /**
     * Returns a list of product IDs from a comma-separated string.
     * @return List of integer IDs
     */
    public List<Integer> getProductIdsList() {
        if (productIds == null || productIds.isEmpty()) {
            return Arrays.asList(); // Returns an empty list if productIds is null or empty
        }
        return Arrays.stream(productIds.split(","))
                .map(String::trim)  // Trims any leading or trailing spaces
                .map(Integer::parseInt) // Converts String to Integer
                .collect(Collectors.toList());
    }
}
