package services;

import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.MyDatabase;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class ProductController {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private TableColumn<Produit, String> listeCat;

    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";

    @FXML
    private TableView<Produit> table;

    @FXML
    private TableColumn<Produit, Integer> id;

    @FXML
    private TableColumn<Produit, String> nom_prod;

    @FXML
    private TableColumn<Produit, Double> prix_prod;

    @FXML
    private TableColumn<Produit, String> descrp_prod;

    @FXML
    private TableColumn<Produit, String> photo_prod;




    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom_prod.setCellValueFactory(new PropertyValueFactory<>("nom_prod"));
        prix_prod.setCellValueFactory(new PropertyValueFactory<>("prix_prod"));
        descrp_prod.setCellValueFactory(new PropertyValueFactory<>("descrp_prod"));
        listeCat.setCellValueFactory(new PropertyValueFactory<>("type_prod"));
        photo_prod.setCellFactory(param -> new CustomImageCellFactory<>());



        addButtonToTable("Supprimer", this::deleteProduit, "Supprimer");
        addButtonToTable("Modifier", this::updateProduit, "Modifier");
        afficher(null);
    }


    public void updateProduit(Produit produit) {
        // Créer une boîte de dialogue avec des champs pour les valeurs à mettre à jour
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Modifier le produit");
        dialog.setHeaderText(null);

        // Créer les champs pour le nom, le prix, la description et la catégorie du produit
        TextField nomField = new TextField(produit.getNom_prod());
        TextField prixField = new TextField(String.valueOf(produit.getPrix_prod()));
        TextArea descrpField = new TextArea(produit.getDescrp_prod());
        ComboBox<String> categorieField = new ComboBox<>();

        // Ajouter les options de catégorie à la liste déroulante (ComboBox)
        // Vous devrez remplacer "listeDesCategories" par votre liste de catégories réelle
        ObservableList<String> categories = FXCollections.observableArrayList();
        // Ajoutez vos catégories à la liste
        categorieField.setItems(categories);
        categorieField.setValue(produit.getType_prod()); // Sélectionnez la catégorie actuelle du produit

        // Créer un layout pour organiser les champs dans la boîte de dialogue
        VBox content = new VBox();
        content.getChildren().addAll(
                new Label("Nom du produit :"), nomField,
                new Label("Prix du produit :"), prixField,
                new Label("Description du produit :"), descrpField,
                new Label("Catégorie du produit :"), categorieField
        );
        content.setSpacing(10);
        content.setPadding(new Insets(10));

        // Définir le contenu de la boîte de dialogue et ajouter des boutons pour OK et Annuler
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Attendre que l'utilisateur valide ou annule la boîte de dialogue
        Optional<Void> result = dialog.showAndWait();
        result.ifPresent(ok -> {
            if (ok.equals(ButtonType.OK)) {
                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String sql = "UPDATE produit SET nom_prod=?, prix_prod=?, descrp_prod=?, categorie_prod=? WHERE id=?";
                    try (PreparedStatement statement = conn.prepareStatement(sql)) {
                        statement.setString(1, nomField.getText());
                        statement.setDouble(2, Double.parseDouble(prixField.getText()));
                        statement.setString(3, descrpField.getText());
                        statement.setString(4, categorieField.getValue());
                        statement.setInt(5, produit.getId());
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                populateTableView();
            }
        });
    }
/*
    public void updateProduit(Produit produit) {
        TextInputDialog dialog = new TextInputDialog(produit.getNom_prod());
        dialog.setTitle("Modifier le produit");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouveau nom de produit :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE produit SET nom_prod=? WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, newName);
                    statement.setInt(2, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            populateTableView();
        });
    } */

    @FXML
    void afficher(ActionEvent event) {
        ObservableList<Produit> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM produit";
        con = MyDatabase.getInstance().getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setNom_prod(rs.getString("nom_prod"));
                produit.setDescrp_prod(rs.getString("descrp_prod"));
                produit.setType_prod(rs.getString("type_prod"));
                produit.setPrix_prod(rs.getDouble("prix_prod"));
                String fileName = rs.getString("photo_prod");
                String filePath;
                if (fileName.startsWith("photos\\")) {
                    // Si le nom de fichier commence par "photos\", retirez ce préfixe
                    filePath = fileName.substring(7); // 7 est la longueur de "photos\"
                } else {
                    filePath = fileName; // Sinon, utilisez le nom de fichier tel quel
                }
                File file = new File("photos", filePath); // Construisez le chemin absolu du fichier
                if (file.exists()) {
                    produit.setPhoto_prod(filePath); // Utilisez le chemin du fichier complet uniquement s'il existe dans le dossier photos
                } else {
                    System.err.println("Le fichier photo '" + filePath + "' n'existe pas.");
                    produit.setPhoto_prod(null); // Si le fichier n'existe pas, définissez la valeur sur null ou une image par défaut
                }
                list.add(produit);
            }

            table.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void deleteProduit(Produit produit) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer le produit");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = MyDatabase.getInstance().getConnection()) {
                String sql = "DELETE FROM produit WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, produit.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            afficher(null);
        }

        populateTableView();
    }

    private void populateTableView()  {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM produit")) {

            ObservableList<Produit> produits = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Produit produit = new Produit();
                produit.setId(resultSet.getInt("id"));
                produit.setNom_prod(resultSet.getString("nom_prod"));
                produit.setDescrp_prod(resultSet.getString("descrp_prod"));
                produit.setPrix_prod(resultSet.getDouble("prix_prod"));
                produit.setType_prod(rs.getString("photo_prod"));
                produits.add(produit);
            }
            table.setItems(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    interface ButtonActionHandler {
        void handle(Produit produit);
    }

    private void addButtonToTable(String buttonText, ButtonActionHandler actionHandler, String columnHeader) {
        TableColumn<Produit, Void> column = new TableColumn<>(columnHeader);
        column.setCellValueFactory(new PropertyValueFactory<>(null));
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button(buttonText);

            {
                button.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    actionHandler.handle(produit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
        table.getColumns().add(column);
    }
}
