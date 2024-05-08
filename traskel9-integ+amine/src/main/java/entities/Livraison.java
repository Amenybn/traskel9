package entities;

public class Livraison {
    private int id;
    private User livreur;
    private Commande commande;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getLivreur() {
        return livreur;
    }

    public void setLivreur(User livreur) {
        this.livreur = livreur;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}
