package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

public class ProfilPropController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;
    @FXML
    private Button btn_deco1;

    @FXML
    private Button btn_deco3;

    @FXML
    private Button btn_deco4;


    @FXML
    private Button btn_deco2;

    @FXML
    private Button btn_enregismodif;

    @FXML
    private RadioButton rbmembre;

    @FXML
    private RadioButton rbproprietaire;

    @FXML
    private TextField tfcin;

    @FXML
    private TextField tfemail;

    @FXML
    private PasswordField tfmdp;

    @FXML
    private TextField tfnom;

    @FXML
    private TextField tfnum_tel;

    @FXML
    private TextField tfprenom;

    @FXML
    private Label nomUtilisateur;

    private Utilisateur utilisateur;

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    private String email;
    private String password;
    @FXML
    private TextField tfid;
    public void initData(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Pre-fill text fields with user information
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getTel_user()));
            tfnom.setText(utilisateur.getNom_user());
            tfprenom.setText(utilisateur.getPrenom_user());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getPassword()); // Consider security implications of displaying password
        }
    }

    @FXML
    void savePerson(ActionEvent event) {

    }

    public void initialize() {
        // Effectuer la liaison bidirectionnelle entre le texte de tfnom et le texte de nomUtilisateur
        nomUtilisateur.textProperty().bindBidirectional(tfnom.textProperty());

        Utilisateur utilisateur = utilisateurCrud.getUtilisateurById(MyConnection.instance.getId());
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getTel_user()));
            tfnom.setText(utilisateur.getNom_user());
            tfprenom.setText(utilisateur.getPrenom_user());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getPassword());
            rbproprietaire.setSelected(false);
            rbmembre.setSelected(true);
        } else {
            // Handle the case where user details are not found
            System.out.println("User details not found.");
        }

        // Récupérer l'ID de l'utilisateur authentifié depuis MyConnection
        int userId = MyConnection.getInstance().getId();

        // Interroger la base de données pour récupérer les données du profil de l'utilisateur
        // Utiliser une méthode de service pour récupérer les données du profil en fonction de l'ID de l'utilisateur
        UtilisateurCrud utilisateurCrud = new UtilisateurCrud();


        // Mettre à jour les champs de texte dans l'interface utilisateur avec les données du profil
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnom.setText(utilisateur.getNom_user());
            tfprenom.setText(utilisateur.getPrenom_user());
            tfemail.setText(utilisateur.getEmail());
            tfnum_tel.setText(String.valueOf(utilisateur.getTel_user()));
        }

        btn_deco2.setOnAction(event -> {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the user clicks "OK", close the current window and open the authentication page
                Stage stage = (Stage) btn_deco2.getScene().getWindow();
                stage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                    Parent root = loader.load();
                    Stage authStage = new Stage();
                    authStage.setScene(new Scene(root));
                    authStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // maysir chay
            }
        });


        // Remplir les champs de texte avec les données de l'utilisateur
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getTel_user()));
            tfnom.setText(utilisateur.getNom_user());
            tfprenom.setText(utilisateur.getPrenom_user());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getPassword());
        }

        // Gérer l'événement du bouton "Modifier profil"
        btn_enregismodif.setOnAction(event -> {
            // Récupérer les nouvelles valeurs des champs de texte
            int cin = Integer.parseInt(tfcin.getText());
            int tel_user = Integer.parseInt(tfnum_tel.getText());
            String nom_user = tfnom.getText();
            String prenom_user = tfprenom.getText();
            String email = tfemail.getText();
            String password = tfmdp.getText();

            // Mettre à jour les propriétés de l'utilisateur
            utilisateur.setCin(cin);
            utilisateur.setNum_tel(tel_user);
            utilisateur.setNom_user(nom_user);
            utilisateur.setPrenom_user(prenom_user);
            utilisateur.setEmail(email);
            utilisateur.setPassword(password);

            // Appeler la méthode de votre classe UtilisateurCrud pour mettre à jour les informations de l'utilisateur dans la base de données
            utilisateurCrud.modifierEntite(utilisateur);
        });
    }


}