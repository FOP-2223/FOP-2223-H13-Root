package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.controller.scene.SceneSwitcher;
import h13.shared.JFXUtils;
import h13.shared.ManualGraderConstants;
import javafx.beans.property.Property;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestForSubmission
public class SettingsSceneTest extends ApplicationTest {
    private Stage stage;
    private SettingsScene settingsScene;

    private static List<Object> correctPropertyValues = new ArrayList<>();

    private static boolean graded = false;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        this.stage = stage;
        settingsScene = new SettingsScene();
        stage.setScene(settingsScene);
        Map.ofEntries(
            Map.entry(ApplicationSettings.instantShootingProperty(), false),
            Map.entry(ApplicationSettings.enemyShootingDelayProperty(), 1000),
            Map.entry(ApplicationSettings.enemyShootingProbabilityProperty(), 0.0005),
            Map.entry(ApplicationSettings.fullscreenProperty(), false),
            Map.entry(ApplicationSettings.loadTexturesProperty(), true),
            Map.entry(ApplicationSettings.loadBackgroundProperty(), true)
        ).forEach((property, value) -> ((Property<Object>) (Property<?>) property).setValue((Object) value));
//        stage.show();
    }

    @BeforeEach
    public void settingsSceneManualGrading() throws InterruptedException {
        if (graded || !ManualGraderConstants.testImplementation) return;
        JFXUtils.messageTutor("Change The Settings in the Settings Scene, you will get log messages for each change.");
        JFXUtils.messageTutor("When you are done, check the correctly modifiable settings in the Grading Panel and press OK.");
        Map.ofEntries(
            Map.entry(ApplicationSettings.instantShootingProperty(), "Instant Shooting"),
            Map.entry(ApplicationSettings.enemyShootingDelayProperty(), "Enemy Shooting Delay"),
            Map.entry(ApplicationSettings.enemyShootingProbabilityProperty(), "Enemy Shooting Probability"),
            Map.entry(ApplicationSettings.fullscreenProperty(), "Fullscreen"),
            Map.entry(ApplicationSettings.loadTexturesProperty(), "Load Textures"),
            Map.entry(ApplicationSettings.loadBackgroundProperty(), "Load Background")
        ).forEach((property, name) -> property.addListener((observable, oldValue, newValue) -> {
            JFXUtils.messageTutor(String.format("The %s Property was set to %s", name, newValue));
        }));
        JFXUtils.onJFXThread(() -> {
            // show Grading Panel for tutor to evaluate correctly modifiable settings
            var gradingScene = new Scene(new BorderPane());
            gradingScene.getStylesheets().add("/h13/controller/scene/game/darkMode.css");
            var root = (BorderPane) gradingScene.getRoot();

            // checkboxes for each setting
            var optionsVbox = new VBox();
            optionsVbox.setAlignment(javafx.geometry.Pos.CENTER);
            optionsVbox.setSpacing(10);
            var instantShootingCheckBox = new CheckBox("Instant Shooting");
            var enemyShootingDelayCheckBox = new CheckBox("Enemy Shooting Delay");
            var enemyShootingProbabilityCheckBox = new CheckBox("Enemy Shooting Probability");
            var fullscreenCheckBox = new CheckBox("Fullscreen");
            var loadTexturesCheckBox = new CheckBox("Load Textures");
            var loadBackgroundCheckBox = new CheckBox("Load Background");
            optionsVbox.getChildren().addAll(
                instantShootingCheckBox,
                enemyShootingDelayCheckBox,
                enemyShootingProbabilityCheckBox,
                fullscreenCheckBox,
                loadTexturesCheckBox,
                loadBackgroundCheckBox
            );

            // buttons to confirm the settings
            var buttonsHbox = new HBox();
            buttonsHbox.setAlignment(javafx.geometry.Pos.CENTER);
            buttonsHbox.setSpacing(10);
            var confirmButton = new javafx.scene.control.Button("Confirm");
            var cancelButton = new javafx.scene.control.Button("Cancel");
            buttonsHbox.getChildren().addAll(confirmButton, cancelButton);

            // add the checkboxes and buttons to the root
            root.setCenter(optionsVbox);
            root.setBottom(buttonsHbox);

            // show the grading scene
            var gradingStage = new Stage();
            gradingStage.setScene(gradingScene);


            // show the settings scene
            var settingsStage = new Stage();

            // add the settings to the list of correct settings
            confirmButton.setOnAction(event -> {
                if (instantShootingCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.instantShootingProperty());
                }
                if (enemyShootingDelayCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.enemyShootingDelayProperty());
                }
                if (enemyShootingProbabilityCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.enemyShootingProbabilityProperty());
                }
                if (fullscreenCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.fullscreenProperty());
                }
                if (loadTexturesCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.loadTexturesProperty());
                }
                if (loadBackgroundCheckBox.isSelected()) {
                    correctPropertyValues.add(ApplicationSettings.loadBackgroundProperty());
                }
                JFXUtils.messageTutor("Settings confirmed");
                // close settings stage
                settingsStage.close();
                ((Stage) confirmButton.getScene().getWindow()).close();
            });

            // close the stage if the cancel button is pressed
            cancelButton.setOnAction(event -> {
                JFXUtils.messageTutor("Settings cancelled");
                settingsStage.close();
                ((Stage) cancelButton.getScene().getWindow()).close();
            });

            // when the grading scene is closed, the settings scene is closed as well
            gradingStage.setOnCloseRequest(event -> {
                settingsStage.close();
            });


            SceneSwitcher.loadScene(SceneSwitcher.SceneType.SETTINGS, settingsStage);
            gradingStage.showAndWait();
        });
        graded = true;
    }

    public <T> void testSettingsSceneProperty(Property<T> property, String propertyName) {
        if (!ManualGraderConstants.testImplementation) return;
        Assertions2.assertTrue(
            correctPropertyValues.contains(property),
            Assertions2.emptyContext(),
            r -> String.format("The %s Property could not be modified.", propertyName)
        );
    }

    @Test
    public void testSettingsSceneInstantShooting() {
        testSettingsSceneProperty(ApplicationSettings.instantShootingProperty(), "Instant Shooting");
    }

    @Test
    public void testSettingsSceneEnemyShootingDelay() {
        testSettingsSceneProperty(ApplicationSettings.enemyShootingDelayProperty(), "Enemy Shooting Delay");
    }

    @Test
    public void testSettingsSceneEnemyShootingProbability() {
        testSettingsSceneProperty(ApplicationSettings.enemyShootingProbabilityProperty(), "Enemy Shooting Probability");
    }

    @Test
    public void testSettingsSceneFullscreen() {
        testSettingsSceneProperty(ApplicationSettings.fullscreenProperty(), "Fullscreen");
    }

    @Test
    public void testSettingsSceneLoadTextures() {
        testSettingsSceneProperty(ApplicationSettings.loadTexturesProperty(), "Load Textures");
    }

    @Test
    public void testSettingsSceneLoadBackground() {
        testSettingsSceneProperty(ApplicationSettings.loadBackgroundProperty(), "Load Background");
    }
}
