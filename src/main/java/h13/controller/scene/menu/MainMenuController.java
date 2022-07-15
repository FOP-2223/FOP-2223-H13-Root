package h13.controller.scene.menu;

import h13.controller.scene.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MainMenuController extends SceneController {
    public void quit(final ActionEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
