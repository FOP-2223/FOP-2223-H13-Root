package h13.controller.gamelogic;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class GameInputHandler {
    private final List<EventHandler<KeyEvent>> onKeyPressed = new ArrayList<>();
    private final List<EventHandler<KeyEvent>> onKeyReleased = new ArrayList<>();
    private final List<EventHandler<KeyEvent>> onKeyTyped = new ArrayList<>();

    /**
     * The keys that were pressed during the last frame.
     * When a key is pressed, it is added to this list after the onKeyPressed event is handled.
     */
    private final List<KeyCode> keysPressed = new ArrayList<>();

    public GameInputHandler(Scene scene) {
        handleKeyboardInputs(scene);
    }

    public void addOnKeyPressed(EventHandler<KeyEvent> eventHandler) {
        onKeyPressed.add(eventHandler);
    }

    public void addOnKeyReleased(EventHandler<KeyEvent> eventHandler) {
        onKeyReleased.add(eventHandler);
    }

    public void addOnKeyTyped(EventHandler<KeyEvent> eventHandler) {
        onKeyTyped.add(eventHandler);
    }

    private void handleKeyboardInputs(Scene scene) {
        scene.setOnKeyPressed(e -> {
            onKeyPressed.forEach(eventHandler -> eventHandler.handle(e));
            keysPressed.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode()); // remove the key from the list of pressed keys
            onKeyReleased.forEach(eventHandler -> eventHandler.handle(e));
        });
        scene.setOnKeyTyped(e -> onKeyTyped.forEach(eventHandler -> eventHandler.handle(e)));
    }


    /**
     *
     * @return the value of the {@link #keysPressed} field.
     * @see #keysPressed
     */
    public List<KeyCode> getKeysPressed() {
        return keysPressed;
    }
}
