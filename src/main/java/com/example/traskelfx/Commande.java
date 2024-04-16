package com.example.traskelfx;

import javafx.beans.property.*;

public class Commande {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty adresseCmd = new SimpleStringProperty();
    private final StringProperty statutCmd = new SimpleStringProperty();
    private final FloatProperty prixCmd = new SimpleFloatProperty();
    private final StringProperty delaisCmd = new SimpleStringProperty();
    private Panier idPanier;

    public Commande() {
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getAdresseCmd() {
        return adresseCmd.get();
    }

    public void setAdresseCmd(String adresseCmd) {
        this.adresseCmd.set(adresseCmd);
    }

    public StringProperty adresseCmdProperty() {
        return adresseCmd;
    }

    public String getStatutCmd() {
        return statutCmd.get();
    }

    public void setStatutCmd(String statutCmd) {
        this.statutCmd.set(statutCmd);
    }

    public StringProperty statutCmdProperty() {
        return statutCmd;
    }

    public float getPrixCmd() {
        return prixCmd.get();
    }

    public void setPrixCmd(float prixCmd) {
        this.prixCmd.set(prixCmd);
    }

    public FloatProperty prixCmdProperty() {
        return prixCmd;
    }

    public String getDelaisCmd() {
        return delaisCmd.get();
    }

    public void setDelaisCmd(String delaisCmd) {
        this.delaisCmd.set(delaisCmd);
    }

    public StringProperty delaisCmdProperty() {
        return delaisCmd;
    }

    public Panier getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(Panier idPanier) {
        this.idPanier = idPanier;
    }
}
