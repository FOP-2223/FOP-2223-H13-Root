package h13.model;

import h13.controller.EnemyController;
import h13.controller.GameController;
import h13.controller.PlayerController;
import h13.model.sprites.Player;
import h13.view.gui.GameScene;
import javafx.geometry.HorizontalDirection;
import javafx.scene.CacheHint;

import static h13.model.GameConstants.ORIGINAL_GAME_BOUNDS;
import static h13.model.GameConstants.RELATIVE_SHIP_WIDTH;

public class GamePlay {

    private GameController gameController;

    public GamePlay(GameController gameController) {
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
