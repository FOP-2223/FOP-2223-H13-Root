package h13.controller.scene.menu;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends SceneController implements Initializable {

    @FXML
    public CheckBox loadTexturesCheckBox;
    @FXML
    public CheckBox loadBackgroundCheckBox;
    @FXML
    public CheckBox instantShootingCheckBox;
    @FXML
    public CheckBox enemyHorizontalMovementCheckBox;
    @FXML
    public CheckBox enemyVerticalMovementCheckBox;
    @FXML
    private CheckBox fullscreenCheckBox;

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

    @Override
    public void initStage(final Stage stage) {
        super.initStage(stage);
        stage.setTitle("Space Invaders - Settings");
    }
}
