package h13.controller;

import h13.model.Playable;
import h13.view.gui.GameScene;
import h13.model.sprites.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.HorizontalDirection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameScene gameScene;
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

    public Pane getGameBoard() {
        return gameScene.getGameBoard();
    }


    public GameController(GameScene gameScene) {
        this.gameScene = gameScene;
        init();
    }

    private void init() {
        // Player
        player = new Player(100, 100, 1.5, this);
        player.setY(getGameBoard().getMaxHeight() - player.getHeight());
        getGameBoard().getChildren().add(player);

        // Enemies
        enemyController = new EnemyController(this, HorizontalDirection.RIGHT);
//        getGameBoard().getChildren().add(enemyController);

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


    public void update(long now) {
        Platform.runLater(() -> {
            if (enemyController != null) enemyController.update(now);
        });
        getGameBoard()
            .getChildren()
            .stream()
            .filter(Playable.class::isInstance)
            .map(Playable.class::cast)
            .forEach(s -> {
                Platform.runLater(() -> s.update(now));
            });

        if (getEnemyController().getAliveEnemyCount() == 0) {
            win();
        } else {
//            System.out.println("remaining enemies: " + getEnemyController().getAliveEnemyCount());
        }
    }

    public void pause() {
        gameLoop.stop();
    }

    public void resume() {
        gameLoop.start();
    }

    private void win() {
        System.out.println("You win!");
        reset();
    }

    public void reset() {
        getGameBoard().getChildren().clear();
        init();
    }
}
