package h13.view.gui;

import h13.controller.scene.menu.SettingsController;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The {@link SettingsScene} is a {@link SubMenuScene} that displays the settings menu.
 */
public class SettingsScene extends SubMenuScene<SettingsController, TabPane> {

    /**
     * Creates a new {@link SettingsScene}.
     */
    public SettingsScene() {
        super(new TabPane(), new SettingsController(), "Settings");
        init();
    }

    /**
     * Initialize the content of the scene.
     */
    private void init() {
        final var tabPane = getContentRoot();
        tabPane.setPrefSize(200, 200);
        final var gameplayTab = new Tab("Gameplay");
        final var graphicsTab = new Tab("Graphics");
//        final var audioTab = new Tab("Audio");

        tabPane.getTabs().addAll(gameplayTab, graphicsTab);

        final var gameplayVBox = new VBox();
        gameplayVBox.setPrefSize(200, 180);
        gameplayVBox.setSpacing(10);
        gameplayVBox.setAlignment(javafx.geometry.Pos.CENTER);
        gameplayVBox.getChildren().add(getController().instantShootingCheckBox = new CheckBox("Instant Shooting"));
        final var enemyShootingDelayHBox = new HBox();
        enemyShootingDelayHBox.setAlignment(javafx.geometry.Pos.CENTER);
        final var enemyShootingDelaySlider = new Slider(0,10000,2000);
        enemyShootingDelayHBox.getChildren().add(enemyShootingDelaySlider);
//        EnemyShootingDelaySlider.setShowTickLabels(true);
//        EnemyShootingDelaySlider.setShowTickMarks(true);
//        EnemyShootingDelaySlider.setMajorTickUnit(1000);
//        EnemyShootingDelaySlider.setMinorTickCount(100);
        enemyShootingDelaySlider.setBlockIncrement(100);
        final var enemyShootingDelayLabel = new Label("Enemy Shooting Delay: " + enemyShootingDelaySlider.getValue());
        enemyShootingDelayHBox.getChildren().add(enemyShootingDelayLabel);
        enemyShootingDelayLabel.textProperty().bind(enemyShootingDelaySlider.valueProperty().asString("Enemy Shooting Delay: %.0fms"));
        getController().enemyShootingDelaySlider = enemyShootingDelaySlider;
        gameplayVBox.getChildren().add(enemyShootingDelayHBox);
        final var enemyShootingProbabilityHBox = new HBox();
        enemyShootingProbabilityHBox.setAlignment(javafx.geometry.Pos.CENTER);
        final var enemyShootingProbabilitySlider = new Slider(0,1, 0.0005);
        enemyShootingProbabilityHBox.getChildren().add(enemyShootingProbabilitySlider);
        enemyShootingProbabilitySlider.setBlockIncrement(0.01);
        final var enemyShootingProbabilityLabel = new Label("Enemy Shooting Probability: " + enemyShootingProbabilitySlider.getValue());
        enemyShootingProbabilityHBox.getChildren().add(enemyShootingProbabilityLabel);
        enemyShootingProbabilityLabel.textProperty().bind(enemyShootingProbabilitySlider.valueProperty().asString("Enemy Shooting Probability: %.4f"));
        getController().enemyShootingProbabilitySlider = enemyShootingProbabilitySlider;
        gameplayVBox.getChildren().add(enemyShootingProbabilityHBox);
        final var autoPlayCheckBox = new CheckBox("Enable Auto Play");
        getController().autoPlayCheckBox = autoPlayCheckBox;
        gameplayVBox.getChildren().add(autoPlayCheckBox);
        gameplayTab.setContent(gameplayVBox);

        final var graphicsVBox = new VBox();
        graphicsVBox.setPrefSize(200, 180);
        graphicsVBox.setSpacing(10);
        graphicsVBox.setAlignment(javafx.geometry.Pos.CENTER);
        graphicsVBox.getChildren().add(getController().loadTexturesCheckBox = new CheckBox("Load Textures"));
        graphicsVBox.getChildren().add(getController().loadBackgroundCheckBox = new CheckBox("Load Background"));
        graphicsVBox.getChildren().add(getController().fullscreenCheckBox = new CheckBox("FullScreen"));
        graphicsTab.setContent(graphicsVBox);

//        final var audioVBox = new VBox();
//        audioVBox.setPrefSize(200, 180);
//        audioVBox.setSpacing(10);
//        audioVBox.setAlignment(javafx.geometry.Pos.CENTER);
//        audioVBox.getChildren().add(getController().enableSoundCheckBox = new CheckBox("Enable Sound"));
//        final var musicVolumeHBox = new HBox();
//        musicVolumeHBox.setAlignment(javafx.geometry.Pos.CENTER);
//        musicVolumeHBox.getChildren().add(getController().musicVolumeSlider = new Slider());
//        musicVolumeHBox.getChildren().add(new Label("Music Volume"));
//        audioVBox.getChildren().add(musicVolumeHBox);
//        final var gameplayVolumeHBox = new HBox();
//        gameplayVolumeHBox.setAlignment(javafx.geometry.Pos.CENTER);
//        gameplayVolumeHBox.getChildren().add(getController().gameplayVolumeSlider = new Slider());
//        gameplayVolumeHBox.getChildren().add(new Label("Gameplay Volume"));
//        audioVBox.getChildren().add(gameplayVolumeHBox);
//        audioTab.setContent(audioVBox);
    }
}
