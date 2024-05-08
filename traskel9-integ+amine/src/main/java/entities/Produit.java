package entities;

public class Produit {
    private int id;
    private String nom_prod;
    private String descrp_prod;
    private String photo_prod;
    private double prix_prod;
    private String type_prod;
    private int id_user_id; // Modifier le nom de l'attribut
    private int panier_id;

    public Produit() {
    }

    public Produit(int id, String nom_prod, String descrp_prod, String photo_prod, double prix_prod, String type_prod, int id_user_id, int panier_id) {
        this.id = id;
        this.nom_prod = nom_prod;
        this.descrp_prod = descrp_prod;
        this.photo_prod = photo_prod;
        this.prix_prod = prix_prod;
        this.type_prod = type_prod;
        this.id_user_id = id_user_id; // Modifier le nom de l'attribut
        this.panier_id = panier_id;
    }

    public Produit(int id, String nomProd, String descrpProd, String photoProd, double prixProd, String typeProd) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_prod() {
        return nom_prod;
    }

    public void setNom_prod(String nom_prod) {
        this.nom_prod = nom_prod;
    }

    public String getDescrp_prod() {
        return descrp_prod;
    }

    public void setDescrp_prod(String descrp_prod) {
        this.descrp_prod = descrp_prod;
    }

    public String getPhoto_prod() {
        return photo_prod;
    }

    public void setPhoto_prod(String photo_prod) {
        this.photo_prod = photo_prod;
    }

    public double getPrix_prod() {
        return prix_prod;
    }

    public void setPrix_prod(double prix_prod) {
        this.prix_prod = prix_prod;
    }

    public String getType_prod() {
        return type_prod;
    }

    public void setType_prod(String type_prod) {
        this.type_prod = type_prod;
    }

    public int getId_user_id() { // Modifier le nom de l'accesseur
        return id_user_id;
    }

    public void setId_user_id(int id_user_id) { // Modifier le nom de l'accesseur
        this.id_user_id = id_user_id;
    }

    public int getPanier_id() {
        return panier_id;
    }

    public void setPanier_id(int panier_id) {
        this.panier_id = panier_id;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom_prod='" + nom_prod + '\'' +
                ", descrp_prod='" + descrp_prod + '\'' +
                ", photo_prod='" + photo_prod + '\'' +
                ", prix_prod=" + prix_prod +
                ", type_prod='" + type_prod + '\'' +
                ", id_user_id=" + id_user_id + // Modifier le nom de l'attribut
                ", panier_id=" + panier_id +
                '}';
    }
}
