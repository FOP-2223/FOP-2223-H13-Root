package h13.controller;

import h13.model.GamePlay;
import h13.model.Playable;
import h13.model.gameplay.GameState;
import h13.view.gui.GameBoard;
import h13.view.gui.GameScene;
import h13.model.sprites.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController implements Playable {
    private final GameScene gameScene;
    private GameState gameState;
    private GamePlay gamePlay;
    private EnemyController enemyController;
    private PlayerController playerController;

    private final AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(final long now) {
            update(now);
        }
    };

    public GameScene getGameScene() {
        return gameScene;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }

    public Player getPlayer() {
        return playerController.getPlayer();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    public GameBoard getGameBoard() {
        return gameScene.getGameBoard();
    }


    public GameController(final GameScene gameScene) {
        this.gameScene = gameScene;
        init();
    }

    private void init() {
        gamePlay = new GamePlay(this);
        gameLoop.start();
    }

    @Override
    public void update(final long now) {
        Platform.runLater(() -> {
            if (enemyController != null && enemyController.getEnemyMovement() != null)
                enemyController.getEnemyMovement().update(now);
        });
        getGameBoard()
            .getSprites()
            .stream()
            .filter(Objects::nonNull)
            .map(Playable.class::cast)
            .forEach(s -> {
                Platform.runLater(() -> s.update(now));
            });
//        getGameBoard().getSprites().forEach(s -> s.update(now));
        getGameBoard().update(now);
        if (getEnemyController() != null && getEnemyController().defeated()) {
            win();
        }
        if (getPlayer().isDead() || (getEnemyController() != null && getEnemyController().getEnemyMovement().bottomWasReached())) {
            lose();
        }
    }

    private void lose() {
        Platform.runLater(() -> {
            gameLoop.stop();
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Loose!!!\nContinue?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                reset();
                gameLoop.start();
                //do stuff
            }
        });
    }

    public void pause() {
        gameLoop.stop();
    }

    public void resume() {
        gameLoop.start();
    }

    private void win() {
        Platform.runLater(() -> {
            gameLoop.stop();
//            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Won!!!\nContinue?", ButtonType.YES, ButtonType.NO);
//            alert.showAndWait();
//
//            if (alert.getResult() == ButtonType.YES) {
                final var score = getPlayer().getScore();
                final var health = getPlayer().getHealth();
                reset();
                getPlayer().setScore(score);
                getPlayer().setHealth(health);
                gameLoop.start();
                //do stuff
//            }
        });
    }

    public void reset() {
        getGameBoard().clearSprites();
        init();
    }

    public void setEnemyController(final EnemyController enemyController) {
        this.enemyController = enemyController;
    }

    public void setPlayerController(final PlayerController playerController) {
        this.playerController = playerController;
    }
}
