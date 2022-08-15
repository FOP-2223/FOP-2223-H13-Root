package h13.controller.scene;

import h13.view.gui.GameScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.Callable;

public final class SceneSwitcher {

    private SceneSwitcher() {
        throw new RuntimeException("Cannot instantiate SceneSwitcher");
    }

    public enum SceneType {
        MAIN_MENU(() -> getFXMLScene("/h13/view.gui/mainMenuScene.fxml"), "Space Invaders - Main Menu"),
        ABOUT(() -> getFXMLScene("/h13/view.gui/aboutScene.fxml"), "Space Invaders - About"),
        SETTINGS(() -> getFXMLScene("/h13/view.gui/settingsScene.fxml"), "Space Invaders - Settings"),
        HIGHSCORE(() -> getFXMLScene("/h13/view.gui/highscoreScene.fxml"), "Space Invaders - Highscore"),
        GAME(() -> SceneAndController.fromScene(new GameScene()), "Space Invaders - Game");
        private final Callable<SceneAndController> sacGenerator;
        private final String title;
        SceneType(final Callable<SceneAndController> sacGenerator, final String title) {
            this.sacGenerator = sacGenerator;
            this.title = title;
        }

        public Callable<SceneAndController> getSacGenerator() {
            return sacGenerator;
        }

        public String getTitle() {
            return title;
        }
    }



    public static Stage getStage(final ActionEvent e) {
        if (e.getSource() instanceof Node n && n.getScene().getWindow() instanceof Stage s) {
            return s;
        }
        throw new IllegalArgumentException("ActionEvent source is not a Node or does not have a Scene with a Stage");
    }

    private static Scene loadScene(final Scene scene, final Stage stage) {
        stage.setScene(scene);
        if (scene instanceof ControlledScene cs) {
            cs.getController().initStage(stage);
        }
        stage.show();
        return scene;
    }

    private static Scene loadScene(final SceneAndController sac, final Stage stage) {
        final var scene = sac.getScene();
        final var controller = sac.getController();
        stage.setScene(scene);
        if (controller != null) {
            controller.initStage(stage);
        }
        stage.show();
        return scene;
    }

    public static Scene loadScene(final SceneType sceneType, final Stage stage) throws Exception {
        final var sac = sceneType.getSacGenerator().call();
        return loadScene(sac, stage);
    }

    private static SceneAndController getFXMLScene(final String sceneName) throws IOException {
        final @Nullable var sceneURL = SceneSwitcher.class.getResource(sceneName);
        if (sceneURL == null) {
            throw new IOException("Scene not found: " + sceneName);
        }
        final var loader = new FXMLLoader(sceneURL);
        return new SceneAndController(new Scene(loader.load()), loader.getController());
    }
}
