package entities;

public class Categorie {
    private int id;
    private String categorie_prod;
    public Categorie( String s) {
    }
    public Categorie(int id, String categorie_prod) {
        this.id = id;
        this.categorie_prod= categorie_prod;
    }
    public Categorie() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCategorie_prod() {
        return categorie_prod;
    }
    public void setCategorie_prod(String nom_prod) {
        this.categorie_prod = nom_prod;
    }
    @Override
    public String toString() {
        return categorie_prod ;
    }



}
