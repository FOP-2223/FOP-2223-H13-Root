package h13.controller.gamelogic;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.EnemyMovement;
import h13.controller.GameConstants;
import h13.model.gameplay.sprites.Bullet;
import h13.model.gameplay.sprites.Enemy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static h13.controller.GameConstants.*;

/**
 * An EnemyController is responsible for instantiating and updating the enemies.
 */
public class EnemyController {

    // --Variables-- //
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
     */
    public EnemyController(
        final GameController gameController) {
        this.gameController = gameController;
        enemyMovement = new EnemyMovement(this);
        nextLevel();
    }

    // --Getters and Setters-- //

    /**
     * Gets a {@link Set} of all {@linkplain Enemy enemies}.
     *
     * @return A {@link Set} of all {@linkplain Enemy enemies}.
     */
    public Set<Enemy> getEnemies() {
        return getGameController().getGameState().getSprites().stream().filter(Enemy.class::isInstance).map(Enemy.class::cast).collect(Collectors.toSet());
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
        return getGameController().getGameState().getSprites().stream().filter(s -> s instanceof Enemy e && e.isAlive()).map(Enemy.class::cast).collect(Collectors.toSet());
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
     * Initialises the enemies for the next level, clearing the current enemies and adding new ones.
     * Also resets the {@link #enemyMovement} using {@link EnemyMovement#nextRound()}.
     */
    public void nextLevel() {
        // cleanup previous level
        getEnemies().clear();
        getGameController().getGameState().getSprites().removeIf(Enemy.class::isInstance);
        getGameController().getGameState().getSprites().removeIf(s -> s instanceof Bullet b && b.getOwner() instanceof Enemy);

        // add new enemies
        final var horizontalSpace = ORIGINAL_GAME_BOUNDS.getWidth();
        final var padding = CHUNK_SIZE / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
        for (int i = 0; i < ENEMY_COLS; i++) {
            for (int j = 0; j < ENEMY_ROWS; j++) {
                final var enemy = new Enemy(
                    i,
                    j,
                    0,
                    (ENEMY_ROWS - j) * 10,
                    getGameController().getGameState()
                );

                enemy.setX(CHUNK_SIZE * i + padding);
                enemy.setY(CHUNK_SIZE * j + padding + HUD_HEIGHT);

                getGameController().getGameState().getSprites().add(enemy);
            }
        }
        // reset enemy movement
        enemyMovement.nextRound();
    }
}
