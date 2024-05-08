    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package com.example.demo4;
    import com.example.demo4.entities.don;
    import com.example.demo4.entities.categorie_don;
    import java.sql.SQLException;
    import java.sql.ResultSet;
    import java.sql.Statement;
    import java.util.ArrayList;
    import com.twilio.Twilio;
    import com.twilio.rest.api.v2010.account.Message;
    import com.twilio.type.PhoneNumber;
    import com.example.demo4.utils.MyDB;
    import javafx.collections.ObservableList;
    import com.twilio.Twilio;
    import com.twilio.rest.api.v2010.account.Message;
    import com.twilio.type.PhoneNumber;
    //**************//
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.util.List;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import javafx.collections.FXCollections;
    import com.example.demo4.entities.don;
    import com.example.demo4.services.categorie_donService;
    import com.example.demo4.services.donService;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.media.MediaPlayer;
    import javafx.stage.FileChooser;

    import java.io.*;
    import java.net.URL;

    import java.sql.SQLException;

    import java.util.List;
    import java.util.*;
    import java.util.logging.Level;
    import java.util.logging.Logger;






    import java.io.BufferedInputStream;
    import java.io.BufferedOutputStream;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.Date;
    import java.sql.SQLException;
    import java.time.LocalDate;
    import java.time.ZoneId;
    import java.util.List;
    import java.util.Random;
    import java.util.ResourceBundle;

    import javafx.scene.control.Alert;

    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;

    import java.util.Optional;


    import javafx.scene.control.ButtonType;













    /**
     * FXML Controller class
     *
     * @author asus
     */
    public class AjouterdonController implements Initializable {

        @FXML
        private TextField descriptionevField;

        @FXML
        private TextField typeevField;
        @FXML
        private TextField imageevField;
        @FXML
        private TextField adresse_donField;



        @FXML
        private TableView<don> donTv;
        @FXML
        private TableColumn<don, String> nomevTv;
        @FXML
        private TableColumn<don, String> typeevTv;
        @FXML
        private TableColumn<don, String> imageevTv;
        @FXML
        private TableColumn<don, String> dateevTv;
        @FXML
        private TableColumn<don, String> descriptionevTv;
         @FXML
        private TableColumn<don, Integer> quantite_donTv;

        @FXML
        private TableColumn<don, Integer> idevTv;

        @FXML
        private TextField quantite_donField;

        @FXML
        private ComboBox<String> idevComboBox;




        @FXML
        private Label partError;
        @FXML
        private Label idLabel;

        ObservableList<don> evs;
        donService Ev=new donService();
        categorie_donService Eb=new categorie_donService();



        @FXML
        private Button participerbutton;
        @FXML
        private ImageView imageview;
        @FXML
        private TextField rechercher;
        @FXML
        private ImageView QrCode;
        @FXML
        private ImageView GoBackBtn;
        @FXML
        private Canvas myCanvas;
        @FXML
        private GridPane gridev;

        donService ab=new donService();
        @FXML
        private TextField chercherevField;
        @FXML
        private Button ajouter;
        @FXML
        private Button mailButton;


        /**
         * Initializes the controller class.
         */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choix de l'action");
        alert.setHeaderText("Que voulez-vous faire ?");
        alert.setContentText("Voulez-vous passer à l'application ou choisir une musique ?");

        ButtonType buttonTypeApp = new ButtonType("Aller à l'application");
        ButtonType buttonTypeMusic = new ButtonType("Choisir une musique");

        alert.getButtonTypes().setAll(buttonTypeApp, buttonTypeMusic);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeApp){
            // Faire quelque chose pour passer à l'application
        } else if (result.get() == buttonTypeMusic) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier audio");
            File defaultDirectory = new File("C:\\Users\\Motaz\\mm Dropbox\\Motaz Sammoud\\PC\\Desktop\\ahmed_metier\\ahmed\\src\\main\\java\\com\\example\\demo4\\musique");
            fileChooser.setInitialDirectory(defaultDirectory);
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers audio (*.mp3, *.wav, *.flac)", "*.mp3", "*.wav", "*.flac");
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                javafx.scene.media.Media javafxMedia = new javafx.scene.media.Media(selectedFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(javafxMedia);
                mediaPlayer.setAutoPlay(true);
            } else {
                System.out.println("Aucun fichier audio sélectionné.");
            }
        }
        categorie_donService service = new categorie_donService();
        List<String> nameCategorieDons = service.getAllNameCategorieDon();
        ObservableList<String> options = FXCollections.observableArrayList(nameCategorieDons);
        idevComboBox.setItems(options);

        //idLabel.setText("");

    }

        private final String ACCOUNT_SID = "AC58c6b368a2c36cc3416e2d3f0167b30b";
        private final String AUTH_TOKEN = "70a9ed949b4981d11aebd22126c90bca";
        private final String TWILIO_PHONE_NUMBER = "+12562935975";





        @FXML
        private void ajouterdon(ActionEvent ev) {
            if ((adresse_donField.getText().isEmpty()) ||  (imageevField.getText().isEmpty()) || (quantite_donField.getText().isEmpty()) || (idevComboBox.getValue() == null) || (descriptionevField.getText().isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error!");
                alert.setContentText("Fields cannot be empty");
                alert.showAndWait();
            } else {
                don e = new don();
                e.setAdresse_don(adresse_donField.getText());
                e.setDescrp_don(descriptionevField.getText());
                e.setQuantite_don(Integer.parseInt(quantite_donField.getText()));

                // Récupérer la valeur sélectionnée dans le ComboBox
                String categoryName = idevComboBox.getValue();

                // Déterminer la valeur de point_don en fonction de la valeur sélectionnée dans le ComboBox
                double pointDonValue = 0;
                if (categoryName.equals("fer")) {
                    pointDonValue = 15;
                } else if (categoryName.equals("aluminium")) {
                    pointDonValue = 30;
                }
                else if (categoryName.equals("plastic")) {
                    pointDonValue = 10;
                }
                else if (categoryName.equals("cuivre")) {
                    pointDonValue = 20;
                }
                else if (categoryName.equals("bois")) {
                    pointDonValue = 5;
                }
                else {
                    pointDonValue = 3;
                }

                // Calculer la valeur de point_don en fonction de pointDonValue et la quantité
                double quantite = Double.parseDouble(quantite_donField.getText());
                double pointDon = pointDonValue * quantite;

                // Convertir la valeur de point_don en chaîne de caractères et l'affecter à point_don
                e.setPoint_don(Double.toString(pointDon));

                // Récupérer l'ID de la catégorie à partir de son nom
                categorie_donService categorieService = new categorie_donService();
                int categoryId = categorieService.getCategoryIDFromName(categoryName);
                e.setId_categorie_don(categoryId);

                // L'image
                e.setImage_don(imageevField.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information ");
                alert.setHeaderText("don add");
                alert.setContentText("don added successfully!");
                alert.showAndWait();

                try {
                    Ev.ajouterdon(e);
                    // Envoi du SMS
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                    Message message = Message.creator(
                            new PhoneNumber("+21625107310"),  // Numéro de téléphone du destinataire
                            new PhoneNumber(TWILIO_PHONE_NUMBER),   // Numéro Twilio
                            "Vous avez ajouter un don!"
                    ).create();

                    System.out.println("SMS envoyé avec succès! SID: " + message.getSid());

                    reset();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        }
        @FXML
        private void navigation(ActionEvent abonn) {
            try {
                //navigation
                Parent loader = FXMLLoader.load(getClass().getResource("ListProductsAffichage.fxml"));
                quantite_donField.getScene().setRoot(loader);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }





        //fin d ajout d'un don
        private void reset() {
            adresse_donField.setText("");
            typeevField.setText("");
            descriptionevField.setText("");
            imageevField.setText("");
            quantite_donField.setText("");



        }
















        @FXML
        private void uploadImage(ActionEvent ev)throws FileNotFoundException, IOException  {

            Random rand = new Random();
            int x = rand.nextInt(1000);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload File Path");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File file = fileChooser.showOpenDialog(null);
            String DBPath = "C:\\\\xampp\\\\htdocs\\\\imageP\\\\"  + x + ".jpg";
            if (file != null) {
                FileInputStream Fsource = new FileInputStream(file.getAbsolutePath());
                FileOutputStream Fdestination = new FileOutputStream(DBPath);
                BufferedInputStream bin = new BufferedInputStream(Fsource);
                BufferedOutputStream bou = new BufferedOutputStream(Fdestination);
                System.out.println(file.getAbsoluteFile());
                String path=file.getAbsolutePath();
                Image img = new Image(file.toURI().toString());
                imageview.setImage(img);
                imageevField.setText(DBPath);
                int b = 0;
                while (b != -1) {
                    b = bin.read();
                    bou.write(b);
                }
                bin.close();
                bou.close();
            } else {
                System.out.println("error");
            }
        }





         private void populateTable(ObservableList<don> branlist){
           donTv.setItems(branlist);

           }
           @FXML
        private void GoBk(MouseEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistics.fxml"));
                Parent root = loader.load();

                // Set the root of the current scene to the new FXML file
                GoBackBtn.getScene().setRoot(root);
        }



        }










