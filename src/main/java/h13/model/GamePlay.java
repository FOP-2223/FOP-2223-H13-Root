package h13.model;

import h13.controller.EnemyController;
import h13.controller.GameController;
import h13.model.sprites.Player;
import h13.view.gui.GameScene;
import javafx.geometry.HorizontalDirection;

public class GamePlay {

    private GameController gameController;

    public GamePlay(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        // Player
        var player = new Player(100, 100, 1.5, getGameController());
        getGameController().setPlayer(player);
        player.setY(gameController.getGameBoard().getMaxHeight() - player.getHeight());
        getGameController().getGameBoard().getChildren().add(player);

        // Enemies
        getGameController().setEnemyController(new EnemyController(getGameController(), HorizontalDirection.RIGHT));
//        getGameBoard().getChildren().add(enemyController);
    }

    public GameController getGameController() {
        return gameController;
    }
}
