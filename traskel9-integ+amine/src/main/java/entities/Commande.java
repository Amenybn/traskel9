package entities;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Commande {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty adresseCmd = new SimpleStringProperty();
    private final StringProperty statutCmd = new SimpleStringProperty();
    private final FloatProperty prixCmd = new SimpleFloatProperty();
    private final StringProperty delaisCmd = new SimpleStringProperty();
    private Panier idPanier;

    public Commande() {
    }

    public Commande(int id, String adresseCmd, String statutCmd, float prixCmd, String delaisCmd) {
    }

    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public String getAdresseCmd() {
        return (String)this.adresseCmd.get();
    }

    public void setAdresseCmd(String adresseCmd) {
        this.adresseCmd.set(adresseCmd);
    }

    public StringProperty adresseCmdProperty() {
        return this.adresseCmd;
    }

    public String getStatutCmd() {
        return (String)this.statutCmd.get();
    }

    public void setStatutCmd(String statutCmd) {
        this.statutCmd.set(statutCmd);
    }

    public StringProperty statutCmdProperty() {
        return this.statutCmd;
    }

    public float getPrixCmd() {
        return this.prixCmd.get();
    }

    public void setPrixCmd(float prixCmd) {
        this.prixCmd.set(prixCmd);
    }

    public FloatProperty prixCmdProperty() {
        return this.prixCmd;
    }

    public String getDelaisCmd() {
        return (String)this.delaisCmd.get();
    }

    public void setDelaisCmd(String delaisCmd) {
        this.delaisCmd.set(delaisCmd);
    }

    public StringProperty delaisCmdProperty() {
        return this.delaisCmd;
    }

    public Panier getIdPanier() {
        return this.idPanier;
    }

    public void setIdPanier(Panier idPanier) {
        this.idPanier = idPanier;
    }
}
