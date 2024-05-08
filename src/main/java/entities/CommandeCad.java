package entities;

import javafx.scene.control.Button;

import java.util.List;

public class CommandeCad {
    private int id;
    private User membre;
    private List<Cadeau> cadeaux;
    private String statut;
    private Button button;

    public CommandeCad(int id, User membre, List<Cadeau> cadeaux, String statut, Button button) {
        this.id = id;
        this.membre = membre;
        this.cadeaux = cadeaux;
        this.statut = statut;
        this.button = button;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getMembre() {
        return membre;
    }

    public void setMembre(User membre) {
        this.membre = membre;
    }

    public List<Cadeau> getCadeaux() {
        return cadeaux;
    }

    public void setCadeaux(List<Cadeau> cadeaux) {
        this.cadeaux = cadeaux;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
