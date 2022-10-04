package h13.view.gui;

import h13.controller.GameConstants;
import h13.controller.scene.SceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public abstract class SubMenuScene<SC extends SceneController, CR extends Node> extends BaseScene<SC> {
    /**
     * A Typesafe reference to the root Node of this Scene.
     */
    private final BorderPane root;

    private final CR contentRoot;


    public SubMenuScene(final CR contentRoot, final SC controller, final String title) {
        super(new BorderPane(), controller);
        // Typesafe reference to the root group of the scene.
        root = (BorderPane) getRoot();
        this.contentRoot = contentRoot;
        init(title);
    }

    private void init(final String title) {
        final Label label = new Label(title);
        label.setFont(GameConstants.TITLE_FONT);
        label.setPadding(new Insets(20, 20, 20, 20));
        root.setTop(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        root.setCenter(contentRoot);

        final var button = new Button("Go Back");
        button.setFont(Font.font(27));
        button.setPrefSize(600, 72);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(getController()::loadMainMenuScene);
        button.setPadding(new Insets(20,20,20,20));
        //root.setPadding(new Insets(20,20,20,20));
        root.setBottom(button);
        BorderPane.setAlignment(button, Pos.CENTER);
    }

    public CR getContentRoot() {
        return contentRoot;
    }
}
