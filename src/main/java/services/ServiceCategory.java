package services;


import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MyDatabase;

import java.util.List;

public class ServiceCategory {
    public static ObservableList<String> chargerCategories() {
        List<Categorie> categories = MyDatabase.getInstance().getAllCategories();
        ObservableList<String> nomCategories = FXCollections.observableArrayList();
        for (Categorie categorie : categories) {
            nomCategories.add(categorie.getCategorie_prod());
        }
        return nomCategories;
    }
}
