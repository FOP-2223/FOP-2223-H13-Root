package h13.controller.scene;

import javafx.scene.Scene;
import org.jetbrains.annotations.Nullable;

public record SceneAndController(Scene scene, @Nullable SceneController controller) {
    public Scene getScene() {
        return scene;
    }
    public @Nullable SceneController getController() {
        return controller;
    }

    public static SceneAndController fromScene(Scene scene) {
        return new SceneAndController(scene, scene instanceof ControlledScene cs ? cs.getController() : null);
    }
}
