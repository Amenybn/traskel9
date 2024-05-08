package entities;

public class Panier {

    private int id;
    private int nbrProduits;
    private double totalPrix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbrProduits() {
        return nbrProduits;
    }

    public void setNbrProduits(int nbrProduits) {
        this.nbrProduits = nbrProduits;
    }

    public double getTotalPrix() {
        return totalPrix;
    }

    public void setTotalPrix(double totalPrix) {
        this.totalPrix = totalPrix;
    }
}
