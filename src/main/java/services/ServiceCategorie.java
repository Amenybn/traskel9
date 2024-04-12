package services;
import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyDatabase;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;


public class ServiceCategorie {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    @FXML
    private TextField categorie_prod;

    @FXML
    private TableColumn<Categorie, String> nomm;

    @FXML
    private TableColumn<Categorie, Integer> id;

    private static final String url = "jdbc:mysql://localhost:3306/traskel";
    private static final String username = "root";
    private static final String password = "";

    @FXML
    private TableView<Categorie> table;
    @FXML
    private Categorie categorieToModify;
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Label errorLabel;

    public void initialize() {

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomm.setCellValueFactory(new PropertyValueFactory<>("categorie_prod"));
        addButtonToTable("Supprimer", this::deleteCategorie, "Supprimer");
        addButtonToTable("Modifier", this::updateCategorie, "Modifier");
        afficher(null);
    }


    public void updateCategorie(Categorie categorie) {
        TextInputDialog dialog = new TextInputDialog(categorie.getCategorie_prod());
        dialog.setTitle("Modifier la catégorie");
        dialog.setHeaderText(null);
        dialog.setContentText("Nouveau nom de catégorie :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE categorie_prod SET categorie_prod=? WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, newName);
                    statement.setInt(2, categorie.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            populateTableView();
        });}



    @FXML
    void afficher(ActionEvent event) {
        ObservableList<Categorie> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM categorie_prod";
        con = MyDatabase.getInstance().getConnection();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(rs.getInt("id"));
                categorie.setCategorie_prod(rs.getString("categorie_prod"));
                list.add(categorie);
            }

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomm.setCellValueFactory(new PropertyValueFactory<>("categorie_prod"));

            table.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   @FXML
    public void deleteCategorie(Categorie categorie) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer la catégorie");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = MyDatabase.getInstance().getConnection()) {
                String sql = "DELETE FROM categorie_prod WHERE id=?";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, categorie.getId());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            afficher(null);
        }

       populateTableView();
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if(actionEvent.getSource()==btnOrders)
        {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
    }
    private void populateTableView() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM categorie_prod")) {

            ObservableList<Categorie> categories = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(resultSet.getInt("id"));
                categorie.setCategorie_prod(resultSet.getString("categorie_prod"));
                // You may need to populate the produits collection here as well
                categories.add(categorie);
            }
            table.setItems(categories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    interface ButtonActionHandler {
        void handle(Categorie categorie);
    }

    private void addButtonToTable(String buttonText, ButtonActionHandler actionHandler, String columnHeader) {
        TableColumn<Categorie, Void> column = new TableColumn<>(columnHeader);
        column.setCellValueFactory(new PropertyValueFactory<>(null));
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button(buttonText);

            {
                button.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    actionHandler.handle(categorie);
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






















