package h13.model;

import h13.controller.EnemyController;
import h13.controller.GameController;
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
        var player = new Player(0, ORIGINAL_GAME_BOUNDS.getHeight() - ORIGINAL_GAME_BOUNDS.getWidth() * RELATIVE_SHIP_WIDTH, 1.5, getGameController());
        getGameController().setPlayer(player);
//        player.setY(gameController.getGameBoard().getHeight() - player.getHeight());
//        player.yProperty().bind(gameController.getGameBoard().heightProperty().subtract(gameController.getGameBoard().widthProperty().multiply(player.getRelativeHeight())));
        getGameController().getGameBoard().addSprite(player);

        // Enemies
        getGameController().setEnemyController(new EnemyController(getGameController(), HorizontalDirection.RIGHT));
    }

    public GameController getGameController() {
        return gameController;
    }
}
