        /*
         * To change this license header, choose License Headers in Project Properties.
         * To change this template file, choose Tools | Templates
         * and open the template in the editor.
         */
        package com.example.demo4.services;

        //import com.sun.javafx.iio.ImageStorage.ImageType;
        import com.example.demo4.entities.don;
        import com.example.demo4.entities.categorie_don;

        import java.io.*;
        import java.sql.SQLException;
        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.ArrayList;

        import com.example.demo4.utils.MyDB;
        import javafx.collections.ObservableList;

        //**************//
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.util.List;
        import java.util.logging.Level;
        import java.util.logging.Logger;
        import javafx.collections.FXCollections;
        import net.glxn.qrgen.QRCode;
        import net.glxn.qrgen.image.ImageType;

        /**
         *
         * @author asus
         */
        public class donService  implements IdonService<don>  {

            Connection cnx;
            public Statement ste;
            public PreparedStatement pst;

            public donService() {
                cnx = MyDB.getInstance().getCnx();

            }

            @Override
            public void ajouterdon(don e) throws SQLException {
                categorie_donService es = new categorie_donService();
                String requete = "INSERT INTO `don` (`adresse_don`,`point_don`,`image_don`,`descrp_don`,`quantite_don`,`id_categorie_don`) "
                        + "VALUES (?,?,?,?,?,?);";
                try {
                    categorie_don tempev = es.FetchOneev(e.getId_categorie_don());
                    System.out.println("before" + tempev);
                    es.modifiercategorie_don(tempev);
                    int new_id = tempev.getId_categorie_don();
                    e.setCategorie_don(tempev);
                    System.out.println("after" + tempev);
                    pst = (PreparedStatement) cnx.prepareStatement(requete);
                    pst.setString(1, e.getAdresse_don());
                    pst.setString(2, e.getPoint_don());
                    pst.setString(3, e.getImage_don());
                    pst.setString(4, e.getDescrp_don());

                    pst.setInt(5, e.getQuantite_don());

                    pst.setInt(6, e.getId_categorie_don());
                    pst.executeUpdate();
                    System.out.println("ev " + e.getAdresse_don()+ e.getId_categorie_don()  + " added successfully");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }
            public String GenerateQrev(don ev) throws FileNotFoundException, IOException {
                String evName = "don adresse: " + ev.getAdresse_don() + "\n" + "don point: " + ev.getPoint_don() + "\n" + "don description: " + ev.getDescrp_don() + "\n" +  "\n";
                ByteArrayOutputStream out = QRCode.from(evName).to(ImageType.JPG).stream();
                String filename = ev.getName() + "_QrCode.jpg";
                //File f = new File("src\\utils\\img\\" + filename);
                File f = new File("C:\\xampp\\htdocs\\imgQr\\qrcode" + filename);
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(out.toByteArray());
                fos.flush();

                System.out.println("qr yemshi");
                return filename;
            }

            @Override
            public void modifierdon(don e) throws SQLException {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                String req = "UPDATE don SET adresse_don = ?,point_don = ?,image_don=?,descrp_don = ?,quantite_don=?,id_categorie_don = ? where id = ?";
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setString(1, e.getAdresse_don());
                ps.setString(2, e.getPoint_don());
                ps.setString(3, e.getImage_don());
                ps.setString(4, e.getDescrp_don());

                ps.setInt(5, e.getQuantite_don());

                ps.setInt(6, e.getId_categorie_don());
                ps.setInt(7, e.getId());

                ps.executeUpdate();
            }

            @Override
            public void supprimerdon(don e) throws SQLException {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                String req = "delete from don where id = ?";
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, e.getId());
                ps.executeUpdate();
                System.out.println("ev with id= " + e.getId() + "  is deleted successfully");
            }





            @Override
            public List<don> recupererdon() throws SQLException {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                List<don> don = new ArrayList<>();
                String s = "select * from don";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(s);
                while (rs.next()) {
                    don e = new don();
                    e.setAdresse_don(rs.getString("adresse_don"));
                    e.setPoint_don(rs.getString("point_don"));
                    e.setImage_don(rs.getString("Image_don"));
                    e.setDescrp_don(rs.getString("descrp_don"));

                    e.setQuantite_don(rs.getInt("quantite_don"));

                    e.setId_categorie_don(rs.getInt("id_categorie_don"));

                    e.setId(rs.getInt("id"));

                    don.add(e);

                }
                return don;
            }

            public don FetchOneev(int id) {
                don ev = new don();
                String requete = "SELECT * FROM `don` where id = " + id;

                try {
                    ste = (Statement) cnx.createStatement();
                    ResultSet rs = ste.executeQuery(requete);

                    while (rs.next()) {

                        ev = new don(rs.getInt("id"), rs.getInt("quantite_don"), rs.getString("adresse_don"), rs.getString("point_don"), rs.getString("image_don"), rs.getString("descrp_don"), rs.getInt("id_categorie_don"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(donService.class.getName()).log(Level.SEVERE, null, ex);
                }
                return ev;
            }

            public ObservableList<don> Fetchevs() {
                ObservableList<don> evs = FXCollections.observableArrayList();
                String requete = "SELECT * FROM `don`";
                try {
                    ste = (Statement) cnx.createStatement();
                    ResultSet rs = ste.executeQuery(requete);

                    while (rs.next()) {
                        evs.add(new don(rs.getInt("id"), rs.getInt("quantite_don"), rs.getString("adresse_don"), rs.getString("point_don"), rs.getString("image_don"), rs.getString("descrp_don"), rs.getInt("id_categorie_don")));
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(donService.class.getName()).log(Level.SEVERE, null, ex);
                }
                return evs;
            }




            public ObservableList<don> chercherev(String chaine) {
                String sql = "SELECT * FROM don WHERE (adresse_don LIKE ?  ) order by adresse_don ";
                //Connection cnx= Maconnexion.getInstance().getCnx();
                String ch = "%" + chaine + "%";
                ObservableList<don> myList = FXCollections.observableArrayList();
                try {

                    Statement ste = cnx.createStatement();
                    // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
                    PreparedStatement stee = cnx.prepareStatement(sql);
                    stee.setString(1, ch);


                    ResultSet rs = stee.executeQuery();
                    while (rs.next()) {
                        don e = new don();

                        e.setAdresse_don(rs.getString("adresse_don"));
                        e.setPoint_don(rs.getString("point_don"));
                        e.setImage_don(rs.getString("Image_don"));
                        e.setDescrp_don(rs.getString("descrp_don"));

                        e.setQuantite_don(rs.getInt("quantite_don"));


                        e.setId(rs.getInt("id"));

                        myList.add(e);
                        System.out.println("ev trouvé! ");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                return myList;
            }

            public List<don> trierev()throws SQLException {
                List<don> don = new ArrayList<>();
                String s = "select * from don order by adresse_don ";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(s);
                while (rs.next()) {
                    don e = new don();
                    e.setAdresse_don(rs.getString("adresse_don"));
                    e.setPoint_don(rs.getString("point_don"));
                    e.setImage_don(rs.getString("Image_don"));
                    e.setDescrp_don(rs.getString("descrp_don"));

                    e.setQuantite_don(rs.getInt("quantite_don"));


                    e.setId(rs.getInt("id"));
                    don.add(e);
                }
                return don;
            }


        }
