package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Windows {

    public static void openNewWindow(String title, String fxmlName, String iconName) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/" + iconName));
        FXMLLoader loader = new FXMLLoader(Windows.class.getResource(fxmlName));
        stage.setScene(new Scene(loader.load()));
        stage.show();

    }

    public static void changeScene(Stage stage, String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Windows.class.getResource("/application/" + fxmlName));
        stage.setScene(new Scene(loader.load()));
    }

}
