package Controllers;

import entities.Utilisateur;

public class Sess {
    private static Utilisateur utilisateurCourant;

    public static void setUtilisateurCourant(Utilisateur utilisateur) {
        utilisateurCourant = utilisateur;
    }

    public static Utilisateur getUtilisateurCourant() {
        return utilisateurCourant;
    }

    public static String getEmailUtilisateurCourant() {
        if (utilisateurCourant != null) {
            return utilisateurCourant.getEmail();
        }
        return null; // ou gestion d'erreur selon besoin
    }
}
