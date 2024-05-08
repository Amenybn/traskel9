package Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LivraisonCadController {

    @FXML
    private Label mainLabel;

    @FXML
    private TableColumn<?, ?> colCADEAUX;

    @FXML
    private TableColumn<?, ?> colLIVREUR;

    @FXML
    private TableColumn<?, ?> colMEMBRE;

    @FXML
    private TableView<?> tableLivCad;

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;




}
