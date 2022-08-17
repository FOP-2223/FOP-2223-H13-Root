package h13.controller.gamelogic;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import h13.model.gameplay.Updatable;
import h13.model.gameplay.sprites.Player;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

import static h13.controller.GameConstants.*;

public class PlayerController implements Updatable {
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

    private void playerKeyAction(KeyEvent e) {
        final var gameInputHandler = getGameController().getGameInputHandler();
        Set<Direction> directions = new HashSet<>();
        boolean shouldKeepShooting = false;
        for (var keyCode : gameInputHandler.getKeysPressed()) {
            switch (keyCode) {
                case LEFT, A -> directions.add(Direction.LEFT);
                case RIGHT, D -> directions.add(Direction.RIGHT);
                case UP, W -> directions.add(Direction.UP);
                case DOWN, S -> directions.add(Direction.DOWN);
                case SPACE -> shouldKeepShooting = true;
            }
        }
        player.setDirection(directions.stream().reduce(Direction::combine).orElse(Direction.NONE));
        player.setKeepShooting(shouldKeepShooting);
    }

    private void handleKeyboardInputs() {
        final var gameInputHandler = getGameController().getGameInputHandler();
        gameInputHandler.addOnKeyPressed(this::playerKeyAction);
        gameInputHandler.addOnKeyReleased(this::playerKeyAction);
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

    @Override
    public void update(double elapsedTime) {

    }
}
