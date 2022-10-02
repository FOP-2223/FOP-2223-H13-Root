package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.gamelogic.EnemyController;
import h13.model.gameplay.sprites.Enemy;
import h13.shared.Utils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static h13.controller.GameConstants.*;

/**
 * The EnemyMovement class is responsible for moving the enemies in a grid.
 */
public class EnemyMovement implements Updatable {

    // --Variables-- //

    /**
     * The current movement direction
     */
    private Direction direction;
    /**
     * the previous movement direction
     */
    private @Nullable Direction previousDirection;
    /**
     * The current movement speed
     */
    private double velocity;
    /**
     * The Next y-coordinate to reach
     */
    private double yTarget = 0;

    /**
     * The enemy controller
     */
    private final EnemyController enemyController;

    // --Constructors-- //

    /**
     * Creates a new EnemyMovement.
     *
     * @param enemyController The enemy controller.
     */
    public EnemyMovement(final EnemyController enemyController) {
        this.enemyController = enemyController;
        nextRound();
    }

    // --Getters and Setters-- //

    /**
     * Gets the current {@link #velocity}.
     *
     * @return The current {@link #velocity}.
     * @see #velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Sets the current {@link #velocity} to the given value.
     *
     * @param velocity The new {@link #velocity}.
     * @see #velocity
     */
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the current {@link #direction}.
     *
     * @return The current {@link #direction}.
     * @see #direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the current {@link #direction} to the given value.
     *
     * @param direction The new {@link #direction}.
     * @see #direction
     */
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    /**
     * Checks whether the bottom was reached.
     *
     * @return {@code true} if the bottom was reached, {@code false} otherwise.
     */
    public boolean bottomWasReached() {
        return getEnemyBounds().getMaxY() >= ORIGINAL_GAME_BOUNDS.getHeight();
    }

    /**
     * Gets the enemy controller.
     *
     * @return The enemy controller.
     */
    public EnemyController getEnemyController() {
        return enemyController;
    }

    // --Utility Methods-- //

    /**
     * Creates a BoundingBox around all alive enemies.
     *
     * @return The BoundingBox.
     */
    public Bounds getEnemyBounds() {
        return new BoundingBox(
            enemyController.getAliveEnemies().stream().mapToDouble(Enemy::getX).min().orElse(0),
            enemyController.getAliveEnemies().stream().mapToDouble(Enemy::getY).min().orElse(0),
            enemyController.getAliveEnemies().stream().mapToDouble(e -> e.getX() + e.getWidth()).max().orElse(0)
                - enemyController.getAliveEnemies().stream().mapToDouble(Enemy::getX).min().orElse(0),
            enemyController.getAliveEnemies().stream().mapToDouble(e -> e.getY() + e.getHeight()).max().orElse(0)
                - enemyController.getAliveEnemies().stream().mapToDouble(Enemy::getY).min().orElse(0)
        );
    }

    /**
     * Checks whether the target Position of the current movement iteration is reached.
     *
     * @param enemyBounds The BoundingBox of all alive enemies.
     * @return {@code true} if the target Position of the current movement iteration is reached, {@code false} otherwise.
     */
    private boolean targetReached(final Bounds enemyBounds) {
        return direction == Direction.UP && enemyBounds.getMinY() <= yTarget
            || direction == Direction.DOWN && enemyBounds.getMaxY() >= yTarget
            || direction == Direction.LEFT && enemyBounds.getMinX() <= 0
            || direction == Direction.RIGHT && enemyBounds.getMaxX() >= ORIGINAL_GAME_BOUNDS.getWidth();
    }

    // --Movement-- //

    @Override
    public void update(final double elapsedTime) {
        if (bottomWasReached()) {
            return;
        }

        final var enemyBounds = getEnemyBounds();
        Bounds newBounds = Utils.getNextPosition(enemyBounds, getVelocity(), direction, elapsedTime);

        if (targetReached(newBounds)) {
            newBounds = Utils.clamp(newBounds);
            nextMovement(enemyBounds);
        }

        updatePositions(
            newBounds.getMinX() - enemyBounds.getMinX(),
            newBounds.getMinY() - enemyBounds.getMinY()
        );
    }

    /**
     * Updates the positions of all alive enemies.
     *
     * @param deltaX The deltaX.
     * @param deltaY The deltaY.
     */
    private void updatePositions(final double deltaX, final double deltaY) {
        final var enemies = getEnemyController().getAliveEnemies();
        for (final var e : enemies) {
            e.setX(e.getX() + deltaX);
            e.setY(e.getY() + deltaY);
        }
    }

    private void nextMovement(final Bounds enemyBounds) {
        if (ApplicationSettings.enemyHorizontalMovementProperty().get() && ApplicationSettings.enemyVerticalMovementProperty().get()) {
            final var oldDirection = direction;
            direction = switch (direction) {
                case LEFT, RIGHT -> Direction.DOWN;
                case DOWN -> previousDirection == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
                default -> Direction.NONE;
            };
            previousDirection = oldDirection;
        } else if (ApplicationSettings.enemyHorizontalMovementProperty().get() || ApplicationSettings.enemyVerticalMovementProperty().get()) {
            direction = direction.getOpposite();
        } else {
            direction = Direction.NONE;
        }
        if (direction.isVertical()) {
            yTarget = enemyBounds.getMaxY() + VERTICAL_ENEMY_MOVE_DISTANCE;
        }
        velocity += .3;
    }

    /**
     * Prepares the next round of enemies.
     * Uses {@link h13.controller.GameConstants#INITIAL_ENEMY_MOVEMENT_DIRECTION} and {@link h13.controller.GameConstants#INITIAL_ENEMY_MOVEMENT_VELOCITY} to set the initial values.
     */
    public void nextRound() {
        direction = INITIAL_ENEMY_MOVEMENT_DIRECTION;
        velocity = INITIAL_ENEMY_MOVEMENT_VELOCITY;
        previousDirection = null;
        yTarget = 0;
    }
}
