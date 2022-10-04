package h13.controller.scene.menu;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;


/**
 * A {@link SceneController} that manages the "Settings" scene.
 */
public class SettingsController extends SceneController {

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
    public CheckBox fullscreenCheckBox;

    /**
     * The checkbox for the "enable sound" setting.
     */
    @FXML
    public CheckBox enableSoundCheckBox;

    /**
     * The checkbox for the "enable music" setting.
     */
    @FXML
    public CheckBox enableMusicCheckBox;

    /**
     * The slider for the "music volume" setting.
     */
    @FXML
    public Slider musicVolumeSlider;

    /**
     * The slider for the "gameplay volume" setting.
     */
    @FXML
    public Slider gameplayVolumeSlider;

    @Override
    public String getTitle() {
        return "Space Invaders - Settings";
    }

    @Override
    public void initStage(final Stage stage) {
        super.initStage(stage);

        fullscreenCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.fullscreenProperty());
        loadTexturesCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.loadTexturesProperty());
        loadBackgroundCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.loadBackgroundProperty());
        instantShootingCheckBox.selectedProperty().bindBidirectional(ApplicationSettings.instantShootingProperty());
    }
}
