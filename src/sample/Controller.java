package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.lang.management.PlatformLoggingMXBean;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextArea console;


    /**
     * On initialise la taille des lignes et colonnes du GridPane
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // On redefinit la taille des lignes et colonnes
        ColumnConstraints columnConstraint = new ColumnConstraints(100);
        RowConstraints rowConstraints = new RowConstraints(100);
        for(int i=0 ; i<9 ; i++)
            gridPane.getColumnConstraints().add(columnConstraint);
        for(int i=0 ; i<5 ; i++)
            gridPane.getRowConstraints().add(rowConstraints);

        gridPane.setStyle("-fx-background-image : url('images/plateau.png')");
        console.setDisable(true);
    }

    /**
     * Permet d'inserer un noeud dans une case du gridPane si aucun noeud ne s'y trouve déjà
     * @param x
     * @param y
     */
    public void addNoeud (Noeud noeud , int x , int y) {
        GridPane.setConstraints(noeud , x , y);
        Platform.runLater( () -> {
            gridPane.getChildren().add(noeud);
            GridPane.setHalignment(noeud , HPos.CENTER);
            GridPane.setValignment(noeud , VPos.CENTER);
        });
    }

    /**
     * Permet de modifier le texte du TextArea
     * @Param texte
     */
    public void setTexte (String texte){
        console.setText(console.getText()+"\n"+texte);
    }
}
