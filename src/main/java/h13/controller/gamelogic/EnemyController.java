package h13.controller.gamelogic;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import h13.model.gameplay.EnemyMovement;
import h13.controller.GameConstants;
import h13.model.gameplay.sprites.Enemy;
import h13.view.gui.GameBoard;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;
import java.util.Set;

import static h13.controller.GameConstants.*;

/**
 * An EnemyController is responsible for instantiating and updating the enemies.
 */
public class EnemyController {

    // --Variables-- //

    /**
     * The enemies.
     */
    private final Set<Enemy> enemies;
    /**
     * The enemy movement controller.
     */
    private final EnemyMovement enemyMovement;
    /**
     * The yOffset.
     */
    private final GameController gameController;

    // --Constructors-- //

    /**
     * Creates a new EnemyController.
     *
     * @param gameController           The game controller.
     * @param initialMovementDirection The initial movement direction.
     */
    public EnemyController(
        final GameController gameController,
        final Direction initialMovementDirection) {
        enemies = new HashSet<>();
        this.gameController = gameController;
        enemyMovement = new EnemyMovement(this, initialMovementDirection);
        init();
    }

    // --Getters and Setters-- //

    /**
     * Gets the value of {@link #enemies} field.
     *
     * @return The value of {@link #enemies} field.
     * @see #enemies
     */
    public Set<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Gets the value of {@link #enemyMovement} field.
     *
     * @return The value of {@link #enemyMovement} field.
     * @see #enemyMovement
     */
    public EnemyMovement getEnemyMovement() {
        return enemyMovement;
    }

    /**
     * Gets the value of {@link #gameController} field.
     *
     * @return The value of {@link #gameController} field.
     * @see #gameController
     */
    public GameController getGameController() {
        return gameController;
    }

    // --Utility Methods-- //

    /**
     * Gets all the {@link Enemy}s where {@link Enemy#isAlive()} returns true.
     *
     * @return The {@link Enemy}s where {@link Enemy#isAlive()} returns true.
     * @see Enemy#isAlive()
     */
    public Set<Enemy> getAliveEnemies() {
        return enemies.stream().filter(Enemy::isAlive).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    /**
     * Checks whether all the {@link Enemy}s are dead.
     *
     * @return {@code true} if all the {@link Enemy}s are dead, {@code false} otherwise.
     * @see Enemy#isDead()
     */
    public boolean defeated() {
        return getAliveEnemies().isEmpty();
    }

    // --Other Methods-- //

    /**
     * Initializes the enemies.
     */
    private void init() {
        // enemies
        final var horizontalSpace = ORIGINAL_GAME_BOUNDS.getWidth();
        final var padding = CHUNK_SIZE / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
        for (int i = 0; i < ENEMY_COLS; i++) {
            for (int j = 0; j < ENEMY_ROWS; j++) {
                final var enemy = new Enemy(
                    i,
                    j,
                    0,
                    (ENEMY_ROWS - j) * 10,
                    getGameController()
                );

                enemy.setX(CHUNK_SIZE * i + padding);
                enemy.setY(CHUNK_SIZE * j + padding + ENEMY_Y_OFFSET);

                getGameController().addSprite(enemy);
                enemies.add(enemy);
            }
        }

    }
}
