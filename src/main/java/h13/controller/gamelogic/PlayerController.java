package h13.controller.gamelogic;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.sprites.Player;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

import static h13.controller.GameConstants.*;

public class PlayerController {
    private final GameController gameController;
    private final Player player;

    private final List<KeyCode> keysPressed = new ArrayList<>();


    public PlayerController(final GameController gameController) {
        this.gameController = gameController;
        player = new Player(
            0,
            ORIGINAL_GAME_BOUNDS.getHeight() - ORIGINAL_GAME_BOUNDS.getWidth() * RELATIVE_SHIP_WIDTH,
            PLAYER_VELOCITY,
            getGameController());
        init();
    }

    private void handleKeyboardInputs() {
        getGameController().getGameScene().setOnKeyPressed(e -> {
            if (keysPressed.contains(e.getCode())) return;
            keysPressed.add(e.getCode());
            switch (e.getCode()) {
                case LEFT, A -> player.moveLeft();
                case RIGHT, D -> player.moveRight();
                case UP, W -> player.moveUp();
                case DOWN, S -> player.moveDown();
                case SPACE -> player.shoot();
            }
        });
        gameController.getGameScene().setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode());    // remove the key from the list of pressed keys
            switch (e.getCode()) {
                case LEFT, A -> player.moveRight();
                case RIGHT, D -> player.moveLeft();
                case UP, W -> player.moveDown();
                case DOWN, S -> player.moveUp();
            }
            if (keysPressed.isEmpty()) {
                player.stop();
            }
        });
    }

    private void init() {
        getGameController().addSprite(player);
        handleKeyboardInputs();
    }

    public GameController getGameController() {
        return gameController;
    }

    public Player getPlayer() {
        return player;
    }
}
