package h13.controller.scene;

import h13.view.gui.GameScene;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static h13.controller.scene.SceneSwitcher.loadScene;

public abstract class SceneController {
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void initStage(final Stage stage) {
        this.stage = stage;
    }
    public Scene loadMainMenuScene(final ActionEvent e) throws Exception {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getStage());
    }

    public Scene loadAboutScene(final ActionEvent e) throws Exception {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.ABOUT, getStage());
    }

    public Scene loadSettingsScene(final ActionEvent e) throws Exception {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.SETTINGS, getStage());
    }

    public Scene loadHighscoreScene(final ActionEvent e) throws Exception {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.HIGHSCORE, getStage());
    }

    public Scene loadGameScene(final ActionEvent e) throws Exception {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.GAME, getStage());
    }
}
