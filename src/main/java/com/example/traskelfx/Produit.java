package com.example.traskelfx;

public class Produit {
    private Integer id;

    private String nomProd;

    private String descrpProd;

    private String photoProd;

    private Boolean typeProd;
    private Float prixProd;


    private Panier panier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public String getDescrpProd() {
        return descrpProd;
    }

    public void setDescrpProd(String descrpProd) {
        this.descrpProd = descrpProd;
    }

    public String getPhotoProd() {
        return photoProd;
    }

    public void setPhotoProd(String photoProd) {
        this.photoProd = photoProd;
    }

    public Boolean getTypeProd() {
        return typeProd;
    }

    public void setTypeProd(Boolean typeProd) {
        this.typeProd = typeProd;
    }

    public Float getPrixProd() {
        return prixProd;
    }

    public void setPrixProd(Float prixProd) {
        this.prixProd = prixProd;
    }


    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }
}
