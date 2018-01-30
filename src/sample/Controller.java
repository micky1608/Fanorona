package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane gridPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setGridLinesVisible(true);
        ColumnConstraints columnConstraint = new ColumnConstraints(100);
        RowConstraints rowConstraints = new RowConstraints(100);
        for(int i=0 ; i<9 ; i++)
            gridPane.getColumnConstraints().add(columnConstraint);
        for(int i=0 ; i<5 ; i++)
            gridPane.getRowConstraints().add(rowConstraints);

    }
}
