package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label label;

    @FXML
    private Button buttonQuit;

    @FXML
    private Button buttonPlayAgain;

    private Game game;

    public EndGameController() {
        Main.setEndGameController(this);
    }

    public void setGame(Game game) {
        this.game = game;
        appendTextLabel((game.getPlayerWinner().equals(PlayerCategory.USER) ? "You win !!" : "You lose !!"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchorPane.setStyle("-fx-background-image : url('images/background_endGame.jpg')");

        buttonQuit.setOnAction((event) -> quitter());

        buttonPlayAgain.setOnAction((event) -> rejouer());
    }

    @FXML
    public void quitter () {
        getStage().close();
    }

    @FXML
    public void rejouer() {
       //TODO
    }

    /**
     * append text to the label
     * @param text
     */
    public void appendTextLabel (String text) {
        Platform.runLater(() -> {
            label.setText(label.getText() + text);
        });
    }

    /**
     * Get the stage containing this scene
     * @return
     */
    public Stage getStage() {
        return (Stage) label.getScene().getWindow();
    }
}
