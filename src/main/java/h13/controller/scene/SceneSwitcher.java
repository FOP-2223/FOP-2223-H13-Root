package h13.controller.scene;

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
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }

    public static Scene loadScene(final Scene scene, final Stage stage) {
        stage.setScene(scene);
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
        return scene;
    }

    public static Scene loadMainMenuScene(final Stage stage) throws IOException {
        stage.setTitle("Space Invaders - Main Menu");
        return loadFXMLScene("/h13/view.gui/mainMenuScene.fxml", stage);
    }

    public static Scene loadAboutScene(final Stage stage) throws IOException {
        stage.setTitle("Space Invaders - About");
        return loadFXMLScene("/h13/view.gui/aboutScene.fxml", stage);
    }

    public static Scene loadSettingsScene(final Stage stage) throws IOException {
        stage.setTitle("Space Invaders - Settings");
        return loadFXMLScene("/h13/view.gui/settingsScene.fxml", stage);
    }

    public static Scene loadHighscoreScene(final Stage stage) throws IOException {
        stage.setTitle("Space Invaders - Highscore");
        return loadFXMLScene("/h13/view.gui/highscoreScene.fxml", stage);
    }
}
