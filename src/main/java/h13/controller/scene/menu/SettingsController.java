package h13.controller.scene.menu;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A {@link SceneController} that manages the "Settings" scene.
 */
public class SettingsController extends SceneController implements Initializable {

    // --Variables-- //

    /**
     * The checkbox for the "load textures" setting.
     */
    @FXML
    public CheckBox loadTexturesCheckBox;

    /**
     * The checkbox for the "load background" setting.
     */
    @FXML
    public CheckBox loadBackgroundCheckBox;

    /**
     * The checkbox for the "instant shooting" setting.
     */
    @FXML
    public CheckBox instantShootingCheckBox;

    /**
     * The checkbox for the "enable Horizontal enemy Movement" setting.
     */
    @FXML
    public CheckBox enemyHorizontalMovementCheckBox;

    /**
     * The checkbox for the "enable Vertical enemy Movement" setting.
     */
    @FXML
    public CheckBox enemyVerticalMovementCheckBox;

    /**
     * The checkbox for the "full screen" setting.
     */
    @FXML
    private CheckBox fullscreenCheckBox;

    @Override
    public String getTitle() {
        return "Space Invaders - Settings";
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        fullscreenCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.fullscreenProperty());
        loadTexturesCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.loadTexturesProperty());
        loadBackgroundCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.loadBackgroundProperty());
        instantShootingCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.instantShootingProperty());
        enemyHorizontalMovementCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.enemyHorizontalMovementProperty());
        enemyVerticalMovementCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.enemyVerticalMovementProperty());
    }
}
