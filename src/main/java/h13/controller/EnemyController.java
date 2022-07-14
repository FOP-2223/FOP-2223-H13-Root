package h13.controller;

import h13.model.EnemyMovement;
import h13.model.GameConstants;
import h13.model.sprites.Enemy;
import h13.view.gui.GameBoard;
import javafx.geometry.HorizontalDirection;
import javafx.scene.CacheHint;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Set;

import static h13.model.GameConstants.*;

public class EnemyController {
    private GameController gameController;
    private EnemyMovement enemyMovement;
    private Set<Enemy> enemies;

    public EnemyController(
        GameController gameController,
        HorizontalDirection initialMovementDirection) {
        super();
        this.enemies = new HashSet<>();
        this.gameController = gameController;
        this.enemyMovement = new EnemyMovement(this, initialMovementDirection);
        init();
    }

    public GameController getGameController() {
        return gameController;
    }

    private void init() {
        // enemies
        for (int i = 0; i < ENEMY_COLS; i++) {
            for (int j = 0; j < ENEMY_ROWS; j++) {
                var enemy = new Enemy(
                    i,
                    j,
                    0,
                    ENEMY_ROWS - j * 10,
                    getGameController()
                );
//                var insets = getGameBoard().getBorder().getInsets();
//                var horizontalSpace = getGameBoard().getMaxWidth() - insets.getLeft() - insets.getRight();
                var horizontalSpace = getGameBoard().getWidth();
                var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
                var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                enemy.setX(chunkSize * i + padding);
                enemy.setY(chunkSize * j + padding);

                getGameBoard().addSprite(enemy);
//                getChildren().add(enemy);
                enemies.add(enemy);
            }
        }


    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    public Set<Enemy> getAliveEnemies() {
        return enemies.stream().filter(Enemy::isAlive).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public int getAliveEnemyCount() {
        return getAliveEnemies().size();
    }

    public boolean defeated() {
        return getAliveEnemies().isEmpty();
    }

    public GameBoard getGameBoard() {
        return gameController.getGameBoard();
    }

    public EnemyMovement getEnemyMovement() {
        return enemyMovement;
    }
}
