package h13.model;

import h13.controller.game.EnemyController;
import h13.controller.game.GameController;
import h13.controller.game.PlayerController;
import javafx.geometry.HorizontalDirection;

public class GamePlay {

    private final GameController gameController;

    public GamePlay(final GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        // Player
        getGameController().setPlayerController(new PlayerController(getGameController()));

        // Enemies
        getGameController().setEnemyController(new EnemyController(getGameController(), HorizontalDirection.RIGHT));
    }

    public GameController getGameController() {
        return gameController;
    }
}
