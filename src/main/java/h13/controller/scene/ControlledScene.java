package h13.controller.scene;

/**
 * An interface for a controlled scene.
 */
public interface ControlledScene {
    /**
     * Gets the {@link SceneController} which is responsible for controlling the scene.
     *
     * @return The {@link SceneController}.
     */
    SceneController getController();
}
