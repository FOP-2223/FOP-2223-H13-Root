package h13.model;

import h13.controller.EnemyController;
import h13.controller.GameController;
import h13.model.sprites.Player;
import h13.view.gui.GameScene;
import javafx.geometry.HorizontalDirection;
import javafx.scene.CacheHint;

public class GamePlay {

    private GameController gameController;

    public GamePlay(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        // Player
        var player = new Player(0, 0, 1.5, getGameController());
        getGameController().setPlayer(player);
        player.setY(gameController.getGameBoard().getHeight() - player.getHeight());

        getGameController().getGameBoard().addSprite(player);

        // Enemies
        getGameController().setEnemyController(new EnemyController(getGameController(), HorizontalDirection.RIGHT));
    }

    public GameController getGameController() {
        return gameController;
    }
}
