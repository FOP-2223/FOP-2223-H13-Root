package h13.controller.scene.menu;

import h13.controller.scene.SceneController;
import javafx.stage.Stage;

public class AboutController extends SceneController {
    @Override
    public void initStage(final Stage stage) {
        super.initStage(stage);
        stage.setTitle("Space Invaders - About");
    }
}
