package h13.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

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
}
