package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import entities.Mailing;
import entities.QRcodeGen;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;


public class InscriptionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button btn_annul;

    @FXML
    private Button btn_inscri;
    @FXML
    private Hyperlink hyperlink;
    private TableView<Utilisateur> tableView;
    private List<Utilisateur> registeredUsers;
    private Stage primaryStage;

    @FXML
    private TextField tfshowpassword;

    @FXML
    private CheckBox show;
    @FXML
    private Label passwordStrengthLabel;
    // Twilio account credentials

    public void setRegisteredUsers(List<Utilisateur> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }
    public void setTableView(TableView<Utilisateur> tableView) {
        this.tableView = tableView;
    }
    public void setHomePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    void initialize() {

        show.setOnAction(event -> handleShowPassCheckboxClick());
        tfshowpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfmdp.setText(newValue);
            }
        });

        tfmdp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfshowpassword.setText(newValue);
            }

            // Vérifier la force du mot de passe et mettre à jour le label en conséquence
            String password = newValue;
            if (password.length() < 8 || !password.matches(".*[A-Z].*")) {
                // Si le mot de passe ne répond pas aux critères de force, afficher un message en rouge
                passwordStrengthLabel.setText("Mot de passe faible!");
                passwordStrengthLabel.setStyle("-fx-text-fill: red;");
            } else {
                // Si le mot de passe est fort, afficher un message en vert
                passwordStrengthLabel.setText("Mot de passe fort.");
                passwordStrengthLabel.setStyle("-fx-text-fill: green;");
            }
        });
        hyperlink.setOnAction(event -> {
            try {
                // Chargez la page d'authentification depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Authentification.fxml"));
                Parent root = loader.load();

                // Créez une nouvelle scène et un nouveau stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Affichez la nouvelle scène
                stage.show();

                // Fermez la fenêtre actuelle
                ((Stage) hyperlink.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace(); // Gérez l'exception selon vos besoins
            }

        });

    }
    @FXML
    void savePerson(ActionEvent event) throws IOException {
        if (tfcin.getText().isEmpty() || tfnum_tel.getText().isEmpty() || tfnom.getText().isEmpty() ||
                tfprenom.getText().isEmpty() || tfemail.getText().isEmpty() || tfmdp.getText().isEmpty()) {
            // Afficher un message d'erreur si les champs sont vides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerCIN(tfcin.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le CIN doit avoir 8 chiffres et commencer par 0 ou 1.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerFormatEmail(tfemail.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "L'adresse email n'est pas dans un format valide.", ButtonType.OK);
            alert.show();
            return;
        }
        String password = tfmdp.getText();
        if (password.length() < 8 || password.equals(password.toLowerCase())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le mot de passe doit contenir au moins 8 caractères et au moins une majuscule.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerNumeroTelephone(tfnum_tel.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le numéro de téléphone doit être composé de 8 chiffres.", ButtonType.OK);
            alert.show();
            return;
        }
        // Sauvegarde de personne dans la BD


        Utilisateur p = new Utilisateur(Integer.parseInt(tfcin.getText()), Integer.parseInt(tfnum_tel.getText()), tfnom.getText(), tfprenom.getText(), tfemail.getText(), tfmdp.getText(), rbmembre.isSelected() ? Role.MEMBRE : Role.LIVREUR);

        // Ajouter des vérifications ici avant d'ajouter l'utilisateur
        UtilisateurCrud uc = new UtilisateurCrud();
        if (!verifierChampsUtilisateur(p)) {
            // Afficher un message d'erreur si les champs ne sont pas valides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs correctement.", ButtonType.OK);
            alert.show();
            return;
        }

        if (uc.utilisateurExisteDeja(tfcin.getText(), tfemail.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cet utilisateur existe déjà.", ButtonType.OK);
            alert.show();
            return;
        }

        // Ajouter l'utilisateur uniquement si les vérifications sont réussies
        uc.ajouterEntite2(p);
        // Send welcome SMS
        //sendWelcomeSMS(p);
        //Role selectedRole = p.getRole();
        //List<Utilisateur> allUsers = uc.getAllUtilisateurs();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Utilisateur ajouté", ButtonType.OK);
        alert.showAndWait();
        String qrCodeContent = "Nom: " + p.getNom_user() + "\n" +
                "Prénom: " + p.getPrenom_user() + "\n" +
                "Email: " + p.getEmail() + "\n" +
                "Numéro de téléphone: " + p.getTel_user();

        // Generate QR code image
        File qrCodeFile = generateQRCode(qrCodeContent);

        // Send welcome email with QR code attached
        sendWelcomeEmailWithQRCode(p, qrCodeFile);
        // Envoyer un e-mail de bienvenue à l'utilisateur ajouté
        //  String emailSubject = "Bienvenue sur notre plateforme";
        // String emailBody = "Bonjour " + p.getPrenom() + ",\n\nBienvenue sur notre plateforme Sportify. Merci pour votre inscription.\n\nCordialement,\nL'équipe de notre plateforme.";
        //Mailing.sendEmail( p.getEmail(), emailSubject, emailBody);
        // Redirection vers la page d'authentification
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"destinations\":[{\"to\":\"21651371144\"}],\"from\":\"ServiceSMS\",\"text\":\""+ "Bienvenue dans notre Plateform" + " Traskel: "  + p.getNom_user()+"\"}]}");

        Request request = new Request.Builder()
                .url("https://e1kxv1.api.infobip.com/sms/2/text/advanced")
                .post(body)
                .addHeader("Authorization", "App 219c8554fe09a34515d5e0ee480f4f89-da377b0c-1572-47e7-a5ba-401f7994d3dc")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        redirectToAuthPage();

    }




    private boolean validerCIN(String cin) {
        String regex = "^[01]\\d{7}$";
        return cin.matches(regex);
    }
    private boolean verifierChampsUtilisateur(Utilisateur utilisateur) {
        // Ajoutez vos vérifications de champs ici
        // Par exemple, vérifiez si les champs ne sont pas vides
        return !utilisateur.getNom_user().isEmpty() &&
                !utilisateur.getPrenom_user().isEmpty() &&
                !utilisateur.getEmail().isEmpty() &&
                !utilisateur.getPassword().isEmpty();
    }
    private boolean validerFormatEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(regex);
    }
    private boolean validerNumeroTelephone(String numeroTelephone) {
        // Ajuster selon le format spécifique du pays (Tunisie)
        String regex = "^\\d{8}$";
        return numeroTelephone.matches(regex);
    }
    @FXML
    void annuler(ActionEvent event) {
        // Obtenez la scène à partir du bouton
        Scene scene = btn_annul.getScene();
        if (scene != null) {
            // Obtenez la fenêtre à partir de la scène
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                // Fermez la fenêtre
                stage.close();
            }
        }
    }

    private File generateQRCode(String content) {
        File qrCodeFile = null;
        try {
            // Generate QR code using QRcodeGen class
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            QRcodeGen.generateQRCode(content, 200, 200, outputStream);

            // Convert ByteArrayOutputStream to byte array
            byte[] qrCodeData = outputStream.toByteArray();

            // Save QR code to a file
            qrCodeFile = new File("C:/Users/ASUS/Pictures/Saved Pictures/hh.jpg");
            try (FileOutputStream fileOutputStream = new FileOutputStream(qrCodeFile)) {
                fileOutputStream.write(qrCodeData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qrCodeFile;
    }

    private void sendWelcomeEmailWithQRCode(Utilisateur utilisateur, File qrCodeFile) {
        String emailSubject = "Bienvenue sur notre plateforme";
        String emailBody = "Bonjour " + utilisateur.getPrenom_user() + ",\n\nBienvenue sur notre plateforme Traskel. Merci pour votre inscription.\n\nCordialement,\nL'équipe de notre plateforme.";

        try {
            // Create a MimeMultipart object
            MimeMultipart multipart = new MimeMultipart();

            // Create a MimeBodyPart for the email body
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(emailBody);

            // Attach the email body MimeBodyPart to the multipart
            multipart.addBodyPart(textBodyPart);

            // Create a MimeBodyPart for the QR code image attachment
            MimeBodyPart qrCodeBodyPart = new MimeBodyPart();
            qrCodeBodyPart.attachFile(qrCodeFile); // Attach the QR code file to the MimeBodyPart

            // Set the content ID of the attachment
            qrCodeBodyPart.setContentID("<qr_code_image>");

            // Attach the QR code MimeBodyPart to the multipart
            multipart.addBodyPart(qrCodeBodyPart);

            // Send the email with multipart content
            Mailing.sendEmailWithAttachment(utilisateur.getEmail(), emailSubject, multipart);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowPassCheckboxClick() {
        if (show.isSelected()) {
            // Afficher le mot de passe en clair
            tfshowpassword.setText(tfmdp.getText());
            tfshowpassword.setVisible(true);
            tfmdp.setVisible(false);
        } else {
            // Masquer le mot de passe en clair et montrer le PasswordField à nouveau
            tfmdp.setText(tfshowpassword.getText());
            tfmdp.setVisible(true);
            tfshowpassword.setVisible(false);
        }
    }
    @FXML
    private void redirectToAuthPage() {
        try {
            // Load the authentication FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/" +
                    "Authentification.fxml"));
            Parent authParent = loader.load();

            // Create a new scene
            Scene authScene = new Scene(authParent);

            // Get the stage information
            Stage stage = new Stage();
            stage.setTitle("Authentification");
            stage.setScene(authScene);

            // Close the current registration window
            Stage currentStage = (Stage) hyperlink.getScene().getWindow();
            currentStage.close();

            // Show the authentication window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


