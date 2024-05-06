package entities;

import entities.enums.Role;

import java.util.Objects;

public class Utilisateur {
    private int id;
    private int cin;
    private int tel_user;
    private String nom_user;
    private String prenom_user;
    private String email;
    private String password;
    private Role roles;

    public Utilisateur() {
    }

    public Utilisateur(int id, int cin, int tel_user, String nom_user, String prenom_user, String email, String password,Role roles) {
        this.id = id;
        this.cin=cin;
        this.tel_user=tel_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email = email;
        this.password = password;
        this.roles=roles;
    }
    public Utilisateur(int cin, int tel_user,String nom_user, String prenom_user, String email, String password,Role roles) {
        this.cin=cin;
        this.tel_user=tel_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email = email;
        this.password = password;
        this.roles=roles;
    }
    public Utilisateur(int id,int cin, int tel_user,String nom_user, String prenom_user, String email, String password) {
        this.id=id;
        this.cin=cin;
        this.tel_user=tel_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email = email;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getTel_user() {
        return tel_user;
    }

    public void setNum_tel(int tel_user) {
        this.tel_user = tel_user;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Vérifier si l'email est au format email kifeh?
        this.email = email;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        {
            this.password = password;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        if (id != that.id) return false;
        return cin == that.cin;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + cin;
        return result;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", cin=" + cin +
                ", tel_user=" + tel_user +
                ", nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
