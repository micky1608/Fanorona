package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Controller controller;

    private static EndGameController endGameController;


    public static Controller getController () {
        return controller;
    }

    public static void setController(Controller controller) {
        Main.controller = controller;
    }

    public static EndGameController getEndGameController() {
        return endGameController;
    }

    public static void setEndGameController(EndGameController endGameController) {
        Main.endGameController = endGameController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Fanorona");
        primaryStage.getIcons().add(new Image("/images/icone.png"));
        primaryStage.setResizable(false);
        Windows.changeScene(primaryStage , "sample.fxml");
        primaryStage.show();
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.start();
        launch(args);
    }


}
