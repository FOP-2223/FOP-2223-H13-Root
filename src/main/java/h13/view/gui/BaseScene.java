package h13.view.gui;

import h13.controller.scene.ControlledScene;
import h13.controller.scene.SceneController;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

public abstract class BaseScene<SC extends SceneController> extends Scene implements ControlledScene<SC> {

    private final SC controller;

    public BaseScene(final Region root, final SC controller) {
        super(root);
        this.controller = controller;
        root.setPrefSize(600, 460);
        root.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        root.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
        // apply styles
        root.getStylesheets().add("h13/view.gui/menuStyles.css");
        root.getStylesheets().add("h13/view.gui/darkMode.css");
    }

    @Override
    public SC getController() {
        return controller;
    }
}
