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

import static h13.controller.scene.SceneSwitcher.loadFXMLScene;
import static h13.controller.scene.SceneSwitcher.loadScene;

public abstract class SceneController {

    public Scene loadMainMenuScene(final ActionEvent e) throws IOException {
        return SceneSwitcher.loadMainMenuScene(SceneSwitcher.getStage(e));
    }

    public Scene loadAboutScene(final ActionEvent e) throws IOException {
        return SceneSwitcher.loadAboutScene(SceneSwitcher.getStage(e));
    }

    public Scene loadSettingsScene(final ActionEvent e) throws IOException {
        return SceneSwitcher.loadSettingsScene(SceneSwitcher.getStage(e));
    }

    public Scene loadHighscoreScene(final ActionEvent e) throws IOException {
        return SceneSwitcher.loadHighscoreScene(SceneSwitcher.getStage(e));
    }

    public Scene loadGameScene(final ActionEvent e) throws IOException {
        return SceneSwitcher.loadGameScene(SceneSwitcher.getStage(e));
    }
}
