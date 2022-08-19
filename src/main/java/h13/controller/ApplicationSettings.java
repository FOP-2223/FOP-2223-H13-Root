package h13.controller;

import h13.model.HighscoreEntry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

/**
 * A Data class that contains the ApplicationSettings.
 * Also handles Saving and Loading of the settings from the filesystem.
 */
public class ApplicationSettings {
    // --Constructors-- //

    /**
     * Overrides the default constructor.
     */
    private ApplicationSettings() {
        throw new IllegalStateException("Cannot instantiate ApplicationSettings");
    }

    // --Variables, Getters and Setters-- //
    /**
     * Whether the game should be played in fullscreen mode.
     */
    private static final BooleanProperty fullscreen = new SimpleBooleanProperty(false);

    /**
     * Gets the {@link #fullscreen}-property.
     *
     * @return The {@link #fullscreen}-property.
     * @see #fullscreen
     */
    public static BooleanProperty fullscreenProperty() {
        return fullscreen;
    }

    /**
     * Whether to load the textures of sprites from the filesystem.
     */
    private static final BooleanProperty loadTextures = new SimpleBooleanProperty(true);

    /**
     * Gets the {@link #loadTextures}-property.
     *
     * @return The {@link #loadTextures}-property.
     * @see #loadTextures
     */
    public static BooleanProperty loadTexturesProperty() {
        return loadTextures;
    }

    /**
     * Whether to load the background images from the filesystem.
     */
    private static final BooleanProperty loadBackground = new SimpleBooleanProperty(true);

    /**
     * Gets the {@link #loadBackground}-property.
     *
     * @return The {@link #loadBackground}-property.
     * @see #loadBackground
     */
    public static BooleanProperty loadBackgroundProperty() {
        return loadBackground;
    }

    /**
     * Whether to enable instant shooting.
     */
    private static final BooleanProperty instantShooting = new SimpleBooleanProperty(false);

    /**
     * Gets the {@link #instantShooting}-property.
     *
     * @return The {@link #instantShooting}-property.
     * @see #instantShooting
     */
    public static BooleanProperty instantShootingProperty() {
        return instantShooting;
    }

    /**
     * Whether to enable horizontal enemy movement.
     */
    private static final BooleanProperty enemyHorizontalMovement = new SimpleBooleanProperty(true);

    /**
     * Gets the {@link #enemyHorizontalMovement}-property.
     *
     * @return The {@link #enemyHorizontalMovement}-property.
     * @see #enemyHorizontalMovement
     */
    public static BooleanProperty enemyHorizontalMovementProperty() {
        return enemyHorizontalMovement;
    }

    /**
     * Whether to enable vertical enemy movement.
     */
    private static final BooleanProperty enemyVerticalMovement = new SimpleBooleanProperty(true);

    /**
     * Gets the {@link #enemyVerticalMovement}-property.
     *
     * @return The {@link #enemyVerticalMovement}-property.
     * @see #enemyVerticalMovement
     */
    public static BooleanProperty enemyVerticalMovementProperty() {
        return enemyVerticalMovement;
    }

    /**
     * The list of highscore entries.
     */
    private static final ObservableList<HighscoreEntry> highscores = FXCollections.observableArrayList(
        new HighscoreEntry("Player 1", new Date().toString(), 100)
    );

    /**
     * Gets the {@link #highscores} list.
     *
     * @return The {@link #highscores} list.
     */
    public static ObservableList<HighscoreEntry> getHighscores() {
        return highscores;
    }
}
