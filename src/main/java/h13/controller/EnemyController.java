package h13.controller;

import h13.model.EnemyMovement;
import h13.model.GameConstants;
import h13.model.sprites.Enemy;
import h13.view.gui.GameBoard;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HorizontalDirection;

import java.util.HashSet;
import java.util.Set;

import static h13.model.GameConstants.*;

public class EnemyController {
    private final GameController gameController;
    private final EnemyMovement enemyMovement;
    private final Set<Enemy> enemies;

    private final DoubleProperty yOffset = new SimpleDoubleProperty(0);

    public EnemyController(
        final GameController gameController,
        final HorizontalDirection initialMovementDirection) {
        enemies = new HashSet<>();
        this.gameController = gameController;
        enemyMovement = new EnemyMovement(this, initialMovementDirection);
        init();
    }

    public GameController getGameController() {
        return gameController;
    }

    private void init() {
        // enemies
        for (int i = 0; i < ENEMY_COLS; i++) {
            for (int j = 0; j < ENEMY_ROWS; j++) {
                final var enemy = new Enemy(
                    i,
                    j,
                    0,
                    (ENEMY_ROWS - j) * 10,
                    getGameController()
                );
//                var insets = getGameBoard().getBorder().getInsets();
//                var horizontalSpace = getGameBoard().getMaxWidth() - insets.getLeft() - insets.getRight();
                final var horizontalSpace = getGameBoard().getWidth();
                final var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
                final var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                final var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                enemy.setX(chunkSize * i + padding);
                enemy.setY(chunkSize * j + padding + getYOffset());

                getGameBoard().addSprite(enemy);
//                getChildren().add(enemy);
                enemies.add(enemy);
            }
        }

        yOffsetProperty().bind(getGameBoard().heightProperty().divide(20));
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

    public DoubleProperty yOffsetProperty() {
        return yOffset;
    }

    public double getYOffset() {
        return yOffset.get();
    }

    public void setYOffset(final double yOffset) {
        this.yOffset.set(yOffset);
    }
}
