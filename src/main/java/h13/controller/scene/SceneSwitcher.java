package h13.controller.scene;

import h13.view.gui.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A SceneSwitcher is responsible for switching between the different {@link Scene}s.
 */
public final class SceneSwitcher {

    // --Variables-- //

    /**
     * An enum that represents the different scenes that can be switched to.
     */
    public enum SceneType {
        // --Enum Constants-- //

        /**
         * The main menu scene.
         */
        //MAIN_MENU(() -> getFXMLScene("/h13/view.gui/mainMenuScene.fxml")),
        MAIN_MENU(() -> SceneAndController.fromScene(new MainMenuScene())),
        /**
         * The about scene.
         */
        //ABOUT(() -> getFXMLScene("/h13/view.gui/aboutScene.fxml")),
        ABOUT(() -> SceneAndController.fromScene(new AboutScene())),
        /**
         * The settings scene.
         */
        //SETTINGS(() -> getFXMLScene("/h13/view.gui/settingsScene.fxml")),
        SETTINGS(() -> SceneAndController.fromScene(new SettingsScene())),
        /**
         * The highscore scene.
         */
        //HIGHSCORE(() -> getFXMLScene("/h13/view.gui/highscoreScene.fxml")),
        HIGHSCORE(() -> SceneAndController.fromScene(new HighscoreScene())),
        /**
         * The game scene.
         */
        GAME(() -> SceneAndController.fromScene(new GameScene()));

        // --Variables-- //
        /**
         * A Callable that creates a {@link SceneAndController} for this {@link SceneType}.
         * The Controller is used to initialize the {@link Scene}.
         */
        private final Callable<SceneAndController> sacGenerator;

        // --Constructors-- //

        /**
         * Creates a new SceneType.
         *
         * @param sacGenerator A Callable that creates a {@link SceneAndController} for this {@link SceneType}.
         */
        SceneType(final Callable<SceneAndController> sacGenerator) {
            this.sacGenerator = sacGenerator;
        }

        // --Getters and Setters-- //

        /**
         * Gets the value of {@link #sacGenerator} field.
         *
         * @return The value of {@link #sacGenerator} field.
         * @see #sacGenerator
         */
        public Callable<SceneAndController> getSacGenerator() {
            return sacGenerator;
        }
    }

    // --Constructors-- //

    /**
     * Overrides the default constructor.
     */
    private SceneSwitcher() {
        throw new RuntimeException("Cannot instantiate SceneSwitcher");
    }

    // --Methods-- //

    /**
     * Loads an FXML file and creates a {@link SceneAndController} from it.
     *
     * @param sceneName The path to the FXML file.
     * @return The {@link SceneAndController} from the FXML file.
     * @throws IOException If the FXML file could not be loaded.
     * @see FXMLLoader#load(java.io.InputStream)
     */
    private static SceneAndController getFXMLScene(final String sceneName) throws IOException {
        final @Nullable var sceneURL = SceneSwitcher.class.getResource(sceneName);
        if (sceneURL == null) {
            throw new IOException("Scene not found: " + sceneName);
        }
        final var loader = new FXMLLoader(sceneURL);
        return new SceneAndController(new Scene(loader.load()), loader.getController());
    }

    /**
     * Switches to the given Scene and initializes its Controller.
     *
     * @param sac   The {@link SceneAndController} to switch to and initialize.
     * @param stage The {@link Stage} to show the {@link Scene} on.
     * @return The {@link Scene} that was switched to.
     * @see Stage#setScene(javafx.scene.Scene)
     * @see SceneController#initStage(Stage)
     */
    private static Scene loadScene(final SceneAndController sac, final Stage stage) {
        final var scene = sac.getScene();
        final var controller = sac.getController();
        stage.setScene(scene);
        if (controller != null) {
            controller.initStage(stage);
        }
        stage.show();
        return scene;
    }

    /**
     * Loads the given {@link SceneType} and initializes its Controller.
     *
     * @param sceneType The {@link SceneType} to load.
     * @param stage     The {@link Stage} to show the {@link Scene} on.
     * @return The {@link Scene} that was switched to.
     * @see #loadScene(SceneAndController, Stage)
     */
    public static Scene loadScene(final SceneType sceneType, final Stage stage) {
        final SceneAndController sac;
        try {
            sac = sceneType.getSacGenerator().call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return loadScene(sac, stage);
    }
}
