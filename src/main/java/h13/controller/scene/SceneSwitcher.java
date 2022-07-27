package h13.controller.scene;

import h13.view.gui.ControlledScene;
import h13.view.gui.GameScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class SceneSwitcher {

    private SceneSwitcher() {
    }

    public static Stage getStage(final ActionEvent e) {
        if (e.getSource() instanceof Node n && n.getScene().getWindow() instanceof Stage s) {
            return s;
        }
        throw new IllegalArgumentException("ActionEvent source is not a Node or does not have a Scene with a Stage");
    }

    public static Scene loadScene(final Scene scene, final Stage stage) {
        stage.setScene(scene);
        if (scene instanceof ControlledScene cs) {
            cs.getController().initStage(stage);
        }
        stage.show();
        return scene;
    }

    public static Scene loadFXMLScene(final String sceneName, final Stage stage) throws IOException {
        final @Nullable var sceneURL = SceneSwitcher.class.getResource(sceneName);
        if (sceneURL == null) {
            throw new IOException("Scene not found: " + sceneName);
        }
        final var loader = new FXMLLoader(sceneURL);
        final var scene = new Scene(loader.load());
        loadScene(scene, stage);
        loader.<SceneController>getController().initStage(stage);
        return scene;
    }

    public static Scene loadMainMenuScene(final Stage stage) throws IOException {
        return loadFXMLScene("/h13/view.gui/mainMenuScene.fxml", stage);
    }

    public static Scene loadAboutScene(final Stage stage) throws IOException {
        return loadFXMLScene("/h13/view.gui/aboutScene.fxml", stage);
    }

    public static Scene loadSettingsScene(final Stage stage) throws IOException {
        return loadFXMLScene("/h13/view.gui/settingsScene.fxml", stage);
    }

    public static Scene loadHighscoreScene(final Stage stage) throws IOException {
        return loadFXMLScene("/h13/view.gui/highscoreScene.fxml", stage);
    }


    public static Scene loadGameScene(final Stage stage) throws IOException {
        return loadScene(new GameScene(), stage);
    }
}
