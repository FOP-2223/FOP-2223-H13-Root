package h13.controller.gamelogic;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.sprites.Player;

import static h13.controller.GameConstants.*;

public class PlayerController {
    private final GameController gameController;
    private final Player player;


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
        final var gameInputHandler = getGameController().getGameInputHandler();
        gameInputHandler.addOnKeyPressed(e -> {
            if (gameInputHandler.getKeysPressed().contains(e.getCode())) return;
            switch (e.getCode()) {
                case LEFT, A -> player.moveLeft();
                case RIGHT, D -> player.moveRight();
                case UP, W -> player.moveUp();
                case DOWN, S -> player.moveDown();
                case SPACE -> player.shoot();
            }
        });
        gameInputHandler.addOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT, A -> player.moveRight();
                case RIGHT, D -> player.moveLeft();
                case UP, W -> player.moveDown();
                case DOWN, S -> player.moveUp();
            }
            if (gameInputHandler.getKeysPressed().isEmpty()) {
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
