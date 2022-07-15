package h13.controller;

import h13.model.HighscoreEntry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.Date;
import java.util.HashMap;

public class ApplicationSettings {
    private ApplicationSettings() {
    }

    private static final BooleanProperty fullscreen = new SimpleBooleanProperty(false);

    public static BooleanProperty fullscreenProperty() {
        return fullscreen;
    }

    private static final BooleanProperty loadTextures = new SimpleBooleanProperty(true);

    public static BooleanProperty loadTexturesProperty() {
        return loadTextures;
    }

    private static final BooleanProperty loadBackground = new SimpleBooleanProperty(true);

    public static BooleanProperty loadBackgroundProperty() {
        return loadBackground;
    }

    private static final BooleanProperty instantShooting = new SimpleBooleanProperty(false);

    public static BooleanProperty instantShootingProperty() {
        return instantShooting;
    }

    private static final BooleanProperty enemyHorizontalMovement = new SimpleBooleanProperty(true);

    public static BooleanProperty enemyHorizontalMovementProperty() {
        return enemyHorizontalMovement;
    }

    private static final BooleanProperty enemyVerticalMovement = new SimpleBooleanProperty(true);

    public static BooleanProperty enemyVerticalMovementProperty() {
        return enemyVerticalMovement;
    }

    // highscores
    public static final ObservableList<HighscoreEntry> highscores = FXCollections.observableArrayList(
        new HighscoreEntry("Player 1", new Date().toString(), 100)
    );
}
