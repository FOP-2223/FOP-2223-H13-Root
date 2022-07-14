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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController implements Playable {
    private GameScene gameScene;
    private GameState gameState;
    private GamePlay gamePlay;
    private EnemyController enemyController;
    private Player player;
    private List<KeyCode> keysPressed = new ArrayList<>();

    private AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
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
        return player;
    }

    public List<KeyCode> getKeysPressed() {
        return keysPressed;
    }

    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    public GameBoard getGameBoard() {
        return gameScene.getGameBoard();
    }


    public GameController(GameScene gameScene) {
        this.gameScene = gameScene;
        init();
    }

    private void init() {
        this.gamePlay = new GamePlay(this);

        handleKeyboardInputs();

        gameLoop.start();
    }

    private void handleKeyboardInputs() {
        gameScene.setOnKeyPressed(e -> {
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
        gameScene.setOnKeyReleased(e -> {
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

    @Override
    public void update(long now) {
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Loose!!!\nContinue?", ButtonType.YES, ButtonType.NO);
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Won!!!\nContinue?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                reset();
                gameLoop.start();
                //do stuff
            }
        });
    }

    public void reset() {
        getGameBoard().clearSprites();
        init();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setEnemyController(EnemyController enemyController) {
        this.enemyController = enemyController;
    }
}
