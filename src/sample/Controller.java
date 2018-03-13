package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextArea console;


    /**
     * Initialisation of line and column sizes for the GridPane
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // define the rows and colums sizes
        ColumnConstraints columnConstraint = new ColumnConstraints(100);
        RowConstraints rowConstraints = new RowConstraints(100);
        for(int i=0 ; i<9 ; i++)
            gridPane.getColumnConstraints().add(columnConstraint);
        for(int i=0 ; i<5 ; i++)
            gridPane.getRowConstraints().add(rowConstraints);


        gridPane.setStyle("-fx-background-image : url('images/plateau.jpg')");

        anchorPane.setStyle("-fx-background-image : url('images/background_textarea.jpg')");
        console.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        console.setEditable(false);
    }

    /**
     * Add a node in a cell of the GridPane if it's empty.
     * @param x
     * @param y
     */
    public void addNoeud (Node node, int x , int y) {
        GridPane.setConstraints(node, x , y);
        Platform.runLater( () -> {
            gridPane.getChildren().add(node);
            GridPane.setHalignment(node, HPos.CENTER);
            GridPane.setValignment(node, VPos.CENTER);
        });
    }

    /**
     * Allows to change the text of the TextArea.
     * @Param texte
     */
    public void setTexte (String texte){
        javafx.application.Platform.runLater( () -> console.setText(console.getText()+"\n"+texte));
        console.setScrollTop(Double.MAX_VALUE);

        System.out.println(texte);


    }
}
