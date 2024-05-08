package entities;

public class LivraisonCadeau {
    private int id;
    private CommandeCad cmdCad;
    private User livreur;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CommandeCad getCmdCad() {
        return cmdCad;
    }

    public void setCmdCad(CommandeCad cmdCad) {
        this.cmdCad = cmdCad;
    }

    public User getLivreur() {
        return livreur;
    }

    public void setLivreur(User livreur) {
        this.livreur = livreur;
    }
}
