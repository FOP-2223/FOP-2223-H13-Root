package h13.model.gameplay;

import h13.controller.gamelogic.EnemyController;
import h13.controller.scene.game.GameController;
import h13.controller.gamelogic.PlayerController;
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
