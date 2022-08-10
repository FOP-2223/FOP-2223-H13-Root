package h13;

import h13.controller.scene.SceneSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SpaceInvaders extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, stage);
    }

    public static void main(final String[] args) {
        launch(args);
    }

}
