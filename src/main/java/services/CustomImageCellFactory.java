package services;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

class CustomImageCellFactory<S, T> extends TableCell<S, String> {
    private final ImageView imageView;

    public CustomImageCellFactory() {
        this.imageView = new ImageView();
        imageView.setFitHeight(50); // Définir la hauteur de l'image
        imageView.setFitWidth(50); // Définir la largeur de l'image
    }

    @Override
    protected void updateItem(String fileName, boolean empty) {
        super.updateItem(fileName, empty);
        if (empty || fileName == null) {
            setGraphic(null);
        } else {
            // Construire le chemin complet du fichier image
            String filePath = "photos/" + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                // Charger l'image depuis le fichier
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                setGraphic(imageView);
            } else {
                // Le fichier n'existe pas, afficher une image par défaut ou un message d'erreur
                System.err.println("Le fichier photo '" + fileName + "' n'existe pas.");
                // Vous pouvez définir une image par défaut ou afficher un message d'erreur à la place
                // Par exemple :
                // Image image = new Image("chemin/vers/une/image/par/defaut.jpg");
                // imageView.setImage(image);
                setGraphic(null);
            }
        }
    }
}
