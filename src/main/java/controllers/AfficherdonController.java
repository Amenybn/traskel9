            /*
             * To change this license header, choose License Headers in Project Properties.
             * To change this template file, choose Tools | Templates
             * and open the template in the editor.
             */
            package com.example.demo4;

            import com.example.demo4.entities.don;
            import com.example.demo4.services.categorie_donService;
            import com.example.demo4.services.donService;
            import com.itextpdf.text.Element;
            import com.itextpdf.text.Paragraph;
            import com.itextpdf.text.pdf.PdfPCell;
            import com.itextpdf.text.pdf.PdfPTable;
            import com.itextpdf.text.pdf.PdfWriter;
            import javafx.collections.FXCollections;
            import javafx.collections.ObservableList;
            import javafx.event.ActionEvent;
            import javafx.fxml.FXML;
            import javafx.fxml.FXMLLoader;
            import javafx.fxml.Initializable;
            import javafx.scene.Parent;
            import javafx.scene.canvas.Canvas;
            import javafx.scene.control.*;
            import javafx.scene.control.Button;
            import javafx.scene.control.Label;
            import javafx.scene.control.TextField;
            import javafx.scene.control.cell.PropertyValueFactory;
            import javafx.scene.image.Image;
            import javafx.scene.image.ImageView;
            import javafx.scene.input.KeyEvent;
            import javafx.scene.input.MouseEvent;
            import javafx.scene.layout.AnchorPane;
            import javafx.scene.layout.GridPane;
            import javafx.stage.FileChooser;
            import org.apache.poi.hssf.usermodel.HSSFRow;
            import org.apache.poi.hssf.usermodel.HSSFSheet;
            import org.apache.poi.hssf.usermodel.HSSFWorkbook;

            import java.awt.*;
            import java.io.*;
            import java.net.URL;
            import java.sql.SQLException;
            import java.text.SimpleDateFormat;
            import java.util.List;
            import java.util.Locale;
            import java.util.Random;
            import java.util.ResourceBundle;
            import java.util.function.Consumer;
            import java.util.logging.Level;
            import java.util.logging.Logger;


            /**
             * FXML Controller class
             *
             * @author asus
             */
            public class AfficherdonController implements Initializable {

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

                categorie_donService service = new categorie_donService();
                List<String> nameCategorieDons = service.getAllNameCategorieDon();
                ObservableList<String> options = FXCollections.observableArrayList(nameCategorieDons);
                idevComboBox.setItems(options);

                //idLabel.setText("");
                getevs();
                afficherdon();
            }










            //hedha achropane
                public void afficherdon(){
                    try {
                        List<don> don = ab.recupererdon();
                        gridev.getChildren().clear();
                        int row = 0;
                        int column = 0;
                        for (int i = 0; i < don.size(); i++) {
                            //chargement dynamique d'une interface
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("don.fxml"));
                            AnchorPane pane = loader.load();

                            //passage de parametres
                            donController controller = loader.getController();
                            controller.setdon(don.get(i));
                            controller.setIdev(don.get(i).getId());
                            gridev.add(pane, column, row);
                            column++;
                            if (column > 1) {
                                column = 0;
                                row++;
                            }
                            if(don.get(i).getQuantite_don()<=0)
                            {
                                // ab.supprimerdon(don.get(i));

                            }
                        }
                    } catch (SQLException | IOException ex) {
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
                //hedha liste
               public void getevs() {
                     try {
                        // TODO
                        List<don> don = Ev.recupererdon();
                        ObservableList<don> olp = FXCollections.observableArrayList(don);
                        donTv.setItems(olp);
                        nomevTv.setCellValueFactory(new PropertyValueFactory<>("adresse_don"));
                        typeevTv.setCellValueFactory(new PropertyValueFactory<>("point_don"));
                        imageevTv.setCellValueFactory(new PropertyValueFactory<>("image_don"));

                        descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("descrp_don"));
                        quantite_donTv.setCellValueFactory(new PropertyValueFactory<>("quantite_don"));

                         idevTv.setCellValueFactory(new PropertyValueFactory<>("id_categorie_don"));
                       // this.delete();
                    } catch (SQLException ex) {
                        System.out.println("error" + ex.getMessage());
                    }
                }//get evs
                @FXML
                private void excelabonn(ActionEvent abonn) {
                    try {
                        String filename = "C:\\xampp\\htdocs\\fichierExcelJava\\dataabonn.xls";
                        HSSFWorkbook hwb = new HSSFWorkbook();
                        HSSFSheet sheet = hwb.createSheet("new sheet");

                        // Création de la première ligne (en-têtes)
                        HSSFRow rowhead = sheet.createRow((short) 0);
                        rowhead.createCell((short) 0).setCellValue("Adresse du don");
                        rowhead.createCell((short) 1).setCellValue("Description du don");
                        rowhead.createCell((short) 2).setCellValue("Point du don");

                        // Récupération des dons et insertion dans le fichier Excel
                        List<don> dons = Ev.recupererdon();
                        for (int i = 0; i < dons.size(); i++) {
                            HSSFRow row = sheet.createRow((short) (i + 1)); // Commence à la deuxième ligne (après les en-têtes)

                            // Remplissage des cellules avec les données des dons
                            row.createCell((short) 0).setCellValue(dons.get(i).getAdresse_don());
                            row.createCell((short) 1).setCellValue(dons.get(i).getDescrp_don());
                            row.createCell((short) 2).setCellValue(dons.get(i).getPoint_don());
                            // Vous pouvez ajouter d'autres colonnes si nécessaire
                        }

                        // Écriture dans le fichier et ouverture
                        FileOutputStream fileOut = new FileOutputStream(filename);
                        hwb.write(fileOut);
                        fileOut.close();
                        System.out.println("Votre fichier Excel a été généré!");

                        File file = new File(filename);
                        if (file.exists()) {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(file);
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }


                @FXML
                private void modifierdon(ActionEvent ev) throws SQLException {
                    don e = donTv.getItems().get(donTv.getSelectionModel().getSelectedIndex());


                    e.setAdresse_don(adresse_donField.getText());
                    e.setPoint_don(typeevField.getText());
                    e.setDescrp_don(descriptionevField.getText());

                    e.setImage_don(imageevField.getText());
                    e.setQuantite_don(Integer.parseInt(quantite_donField.getText()));

                    // Récupérer la valeur sélectionnée dans le ComboBox
                    String categoryName = idevComboBox.getValue();





                    // Récupérer l'ID de la catégorie à partir de son nom
                    categorie_donService categorieService = new categorie_donService();
                    int categoryId = categorieService.getCategoryIDFromName(categoryName);
                    e.setId_categorie_don(categoryId);

                    Ev.modifierdon(e);
                    reset();
                    getevs();
                    afficherdon();
                }
                @FXML
            //ta3 tablee bch nenzel 3ala wehed ya5tarou w yet3abew textfield
                private void choisirev(MouseEvent ev) throws IOException {
                    don e = donTv.getItems().get(donTv.getSelectionModel().getSelectedIndex());
                    //idLabel.setText(String.valueOf(e.getid()));

                    adresse_donField.setText(e.getAdresse_don());
                    typeevField.setText(e.getPoint_don());
                    imageevField.setText(e.getImage_don());
                    descriptionevField.setText(e.getDescrp_don());
                    //dateevField.setValue((e.getDate()));
                    quantite_donField.setText(String.valueOf(e.getQuantite_don()));


                    // Récupérer l'ID de la catégorie sous forme de String
                    String idCategorieDon = String.valueOf(e.getId_categorie_don());
                    // Sélectionner l'ID de la catégorie dans le ComboBox idevComboBox
                    idevComboBox.getSelectionModel().select(idCategorieDon);

                    //lel image
                    String path = e.getImage_don();
                    File file=new File(path);
                    Image img = new Image(file.toURI().toString());
                    imageview.setImage(img);
                    String filename = Ev.GenerateQrev(e);
                    System.out.println("filename lenaaa " + filename);
                    String path1="C:\\xampp\\htdocs\\imgQr\\qrcode"+filename;
                    File file1=new File(path1);
                    Image img1 = new Image(file1.toURI().toString());
                    //Image image = new Image(getClass().getResourceAsStream("src/utils/img/" + filename));
                    QrCode.setImage(img1);
                }

                @FXML
                private void supprimerdon(ActionEvent ev) {
                       don e = donTv.getItems().get(donTv.getSelectionModel().getSelectedIndex());
                    try {
                        Ev.supprimerdon(e);
                    } catch (SQLException ex) {
                        Logger.getLogger(AfficherdonController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information ");
                    alert.setHeaderText("don delete");
                    alert.setContentText("don deleted successfully!");
                    alert.showAndWait();
                    getevs();
                    afficherdon();
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
                @FXML
                private void pdfabonn(ActionEvent abonn) throws FileNotFoundException, SQLException, IOException {
                    // don tab_Recselected = donTv.getSelectionModel().getSelectedItem();
                    long millis = System.currentTimeMillis();
                    java.sql.Date DateRapport = new java.sql.Date(millis);

                    String DateLyoum = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(DateRapport);//yyyyMMddHHmmss
                    System.out.println("Date d'aujourdhui : " + DateLyoum);

                    com.itextpdf.text.Document document = new com.itextpdf.text.Document();

                    try {
                        PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(DateLyoum + ".pdf")));//yyyy-MM-dd
                        document.open();
                        Paragraph ph1 = new Paragraph("Voici un rapport détaillé de notre application qui contient tous les dons . Pour chaque don, nous fournissons des informations telles que la date d'Aujourd'hui :" + DateRapport );
                        Paragraph ph2 = new Paragraph(".");
                        PdfPTable table = new PdfPTable(4);
                        //On créer l'objet cellule.
                        PdfPCell cell;
                        //contenu du tableau.
                        table.addCell("Adresse");
                        table.addCell("quantité");
                        table.addCell("point");
                        table.addCell("image");

                        don r = new don();
                        Ev.recupererdon().forEach(new Consumer<don>() {
                            @Override
                            public void accept(don e) {
                                table.setHorizontalAlignment(Element.ALIGN_CENTER);
                                table.addCell(String.valueOf(e.getAdresse_don()));
                                table.addCell(String.valueOf(e.getQuantite_don()));
                                table.addCell(String.valueOf(e.getPoint_don()));
                                try {
                                    // Créer un objet Image à partir de l'image
                                    String path = e.getImage_don();
                                    com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(path);

                                    // Définir la taille de l'image dans le tableau
                                    img.scaleToFit(100, 100); // Définir la largeur et la hauteur de l'image

                                    // Ajouter l'image à la cellule du tableau
                                    PdfPCell cell = new PdfPCell(img);
                                    table.addCell(cell);
                                } catch (Exception ex) {
                                    table.addCell("Erreur lors du chargement de l'image");
                                }
                            }
                        });
                        document.add(ph1);
                        document.add(ph2);
                        document.add(table);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    document.close();

                    ///Open FilePdf
                    File file = new File(DateLyoum + ".pdf");
                    Desktop desktop = Desktop.getDesktop();
                    if (file.exists()) //checks file exists or not
                    {
                        desktop.open(file); //opens the specified file
                    }
                }







                @FXML
                private void rechercherev(KeyEvent ev) {

                    donService bs=new donService();
                    don b= new don();
                    ObservableList<don>filter= bs.chercherev(rechercher.getText());
                    populateTable(filter);
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
                @FXML
                private void navigation(ActionEvent abonn) {
                    try {
                        //navigation
                        Parent loader = FXMLLoader.load(getClass().getResource("ListProducts.fxml"));
                        gridev.getScene().setRoot(loader);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                @FXML
                private void navigationstat(ActionEvent abonn) {
                    try {
                        //navigation
                        Parent loader = FXMLLoader.load(getClass().getResource("Statistics.fxml"));
                        gridev.getScene().setRoot(loader);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }


                }










