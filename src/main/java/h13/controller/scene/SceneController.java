package h13.controller.scene;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
