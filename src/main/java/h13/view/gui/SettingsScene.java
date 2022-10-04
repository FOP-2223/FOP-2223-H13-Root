package h13.view.gui;

import h13.controller.scene.menu.SettingsController;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsScene extends SubMenuScene<SettingsController, TabPane> {

    /**
     * Creates a Scene for a specific root Node.
     */
    public SettingsScene() {
        super(new TabPane(), new SettingsController(), "Settings");
        init();
    }

    private void init() {
        final var tabPane = getContentRoot();
        tabPane.setPrefSize(200, 200);
        final var gameplayTab = new Tab("Gameplay");
        final var graphicsTab = new Tab("Graphics");
        final var audioTab = new Tab("Audio");

        tabPane.getTabs().addAll(gameplayTab, graphicsTab, audioTab);

        final var gameplayVBox = new VBox();
        gameplayVBox.setPrefSize(200, 180);
        gameplayVBox.setSpacing(10);
        gameplayVBox.setAlignment(javafx.geometry.Pos.CENTER);
        gameplayVBox.getChildren().add(getController().instantShootingCheckBox = new CheckBox("Instant Shooting"));
        gameplayTab.setContent(gameplayVBox);

        final var graphicsVBox = new VBox();
        graphicsVBox.setPrefSize(200, 180);
        graphicsVBox.setSpacing(10);
        graphicsVBox.setAlignment(javafx.geometry.Pos.CENTER);
        graphicsVBox.getChildren().add(getController().loadTexturesCheckBox = new CheckBox("Load Textures"));
        graphicsVBox.getChildren().add(getController().loadBackgroundCheckBox = new CheckBox("Load Background"));
        graphicsVBox.getChildren().add(getController().fullscreenCheckBox = new CheckBox("FullScreen"));
        graphicsTab.setContent(graphicsVBox);

        final var audioVBox = new VBox();
        audioVBox.setPrefSize(200, 180);
        audioVBox.setSpacing(10);
        audioVBox.setAlignment(javafx.geometry.Pos.CENTER);
        audioVBox.getChildren().add(getController().enableSoundCheckBox = new CheckBox("Enable Sound"));
        final var musicVolumeHBox = new HBox();
        musicVolumeHBox.setAlignment(javafx.geometry.Pos.CENTER);
        musicVolumeHBox.getChildren().add(getController().musicVolumeSlider = new Slider());
        musicVolumeHBox.getChildren().add(new Label("Music Volume"));
        audioVBox.getChildren().add(musicVolumeHBox);
        final var gameplayVolumeHBox = new HBox();
        gameplayVolumeHBox.setAlignment(javafx.geometry.Pos.CENTER);
        gameplayVolumeHBox.getChildren().add(getController().gameplayVolumeSlider = new Slider());
        gameplayVolumeHBox.getChildren().add(new Label("Gameplay Volume"));
        audioVBox.getChildren().add(gameplayVolumeHBox);
        audioTab.setContent(audioVBox);
    }
}
