package services;

import entities.Livraison;
import entities.User;
import com.pi.traskel.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateLiv {

    @FXML
    private Button confirmBtn;

    @FXML
    private ListView<User> livreurListView;

    @FXML
    private TextField adresseCmd;

    @FXML
    private TextField delaiCmd;

    @FXML
    private TextField statutCmd;

    @FXML
    private TextField livreurCmd;

    @FXML
    private TextField searchField;

    private FilteredList<User> filteredLivreurUsers;

    private Livraison livraison;

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
        if (livraison != null) {
            adresseCmd.setText(livraison.getCommande().getAdresse());
            delaiCmd.setText(livraison.getCommande().getDelai());
            statutCmd.setText(livraison.getCommande().getStatut());
            livreurCmd.setText(livraison.getLivreur().toString());

            adresseCmd.setEditable(false);
            delaiCmd.setEditable(false);
            statutCmd.setEditable(false);
            livreurCmd.setEditable(false);
            adresseCmd.setDisable(true);
            delaiCmd.setDisable(true);
            statutCmd.setDisable(true);
            livreurCmd.setDisable(true);

            adresseCmd.setStyle("-fx-opacity: 1;");
            delaiCmd.setStyle("-fx-opacity: 1;");
            statutCmd.setStyle("-fx-opacity: 1;");
            livreurCmd.setStyle("-fx-opacity: 1;");

            populateLivreurListView();
        }
    }

    private void populateLivreurListView() {
        // Create a FilteredList with all livreur users
        List<User> allLivreurUsers = getAllLivreurUsers();
        filteredLivreurUsers = new FilteredList<>(FXCollections.observableArrayList(allLivreurUsers), p -> true);

        // Bind the FilteredList to the searchField's text property for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLivreurUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all users if search field is empty
                }

                // Compare the search term with user's first name or last name
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getPrenom().toLowerCase().contains(lowerCaseFilter) || user.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Match found, show user
                }

                return false; // No match, hide user
            });
        });

        // Set the filtered list to your ListView
        livreurListView.setItems(filteredLivreurUsers);

        // Set the cell factory
        livreurListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> listView) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            setText(user.getPrenom() + " " + user.getNom());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    private List<User> getAllLivreurUsers() {
        List<User> allLivreurUsers = new ArrayList<>();
        try (Connection con = DBConnection.getCon();
             PreparedStatement st = con.prepareStatement("SELECT * FROM user WHERE role = ?")) {
            st.setString(1, "livreur");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    User livreur = new User();
                    livreur.setId(rs.getInt("id"));
                    livreur.setPrenom(rs.getString("prenom"));
                    livreur.setNom(rs.getString("nom"));
                    livreur.setEmail(rs.getString("email"));
                    livreur.setPassword(rs.getString("password"));
                    livreur.setAdresse(rs.getString("adresse"));
                    livreur.setRole(rs.getString("role"));
                    allLivreurUsers.add(livreur);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allLivreurUsers;
    }

    @FXML
    private void confirmUpdate() {
        User selectedLivreur = livreurListView.getSelectionModel().getSelectedItem();
        if (selectedLivreur != null) {
            livraison.setLivreur(selectedLivreur);

            updateLivraisonInDatabase(livraison);

            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.close();
        }
    }

    private void updateLivraisonInDatabase(Livraison livraison) {
        String query = "UPDATE livraisons SET livreur_id = ? WHERE id = ?";
        try (Connection con = DBConnection.getCon();
             PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, livraison.getLivreur().getId());
            st.setInt(2, livraison.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}