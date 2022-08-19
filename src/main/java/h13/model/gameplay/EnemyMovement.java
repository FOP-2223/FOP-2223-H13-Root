package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.model.gameplay.sprites.Enemy;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.jetbrains.annotations.Nullable;

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
     * Whether the bottom was reached
     */
    private boolean bottomWasReached = false;
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
        return bottomWasReached;
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
    private @Nullable Bounds getEnemyBounds() {
        final var enemies = getEnemyController().getAliveEnemies();
        if (enemies.isEmpty()) {
            return null;
        }
        boolean first = true;
        int lowestxIndex, highestxIndex, lowestyIndex, highestyIndex;
        lowestxIndex = highestxIndex = lowestyIndex = highestyIndex = 0;
        Enemy enemyWithLowestXIndex = null, enemyWithLowestYIndex = null, enemyWithHighestXIndex = null, enemyWithHighestYIndex = null;
        for (final var e : enemies) {
            if (!e.isAlive()) {
                continue;
            }
            if (first) {
                lowestxIndex = highestxIndex = e.getxIndex();
                lowestyIndex = highestyIndex = e.getyIndex();
                first = false;
            }
            lowestxIndex = Math.min(lowestxIndex, e.getxIndex());
            highestxIndex = Math.max(highestxIndex, e.getxIndex());
            lowestyIndex = Math.min(lowestyIndex, e.getyIndex());
            highestyIndex = Math.max(highestyIndex, e.getyIndex());
            if (e.getxIndex() == lowestxIndex) {
                enemyWithLowestXIndex = e;
            }
            if (e.getyIndex() == lowestyIndex) {
                enemyWithLowestYIndex = e;
            }
            if (e.getxIndex() == highestxIndex) {
                enemyWithHighestXIndex = e;
            }
            if (e.getyIndex() == highestyIndex) {
                enemyWithHighestYIndex = e;
            }
        }

        if (enemyWithLowestXIndex == null || enemyWithLowestYIndex == null || enemyWithHighestXIndex == null || enemyWithHighestYIndex == null) {
            return null;
        }

        return new BoundingBox(
            enemyWithLowestXIndex.getX(),
            enemyWithLowestYIndex.getY(),
            enemyWithHighestXIndex.getX() + enemyWithHighestXIndex.getWidth() - enemyWithLowestXIndex.getX(),
            enemyWithHighestYIndex.getY() + enemyWithHighestYIndex.getHeight() - enemyWithLowestYIndex.getY()
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
        if (bottomWasReached) {
            return;
        }

        final var enemyBounds = getEnemyBounds();

        if (enemyBounds == null) {
            return;
        }

        final var deltaX = velocity * elapsedTime * direction.getX();
        final var deltaY = velocity * elapsedTime * direction.getY();

        final Bounds newBounds = new BoundingBox(
            enemyBounds.getMinX() + deltaX,
            enemyBounds.getMinY() + deltaY,
            enemyBounds.getWidth(),
            enemyBounds.getHeight());

        if (targetReached(newBounds)) {
            var newDeltaX = deltaX;
            var newDeltaY = deltaY;
            if (newBounds.getMinX() < 0) {
                newDeltaX -= newBounds.getMinX();
            }
            if (newBounds.getMinY() < 0) {
                newDeltaY -= newBounds.getMinY();
            }
            if (newBounds.getMaxX() > ORIGINAL_GAME_BOUNDS.getWidth()) {
                newDeltaX -= newBounds.getMaxX() - ORIGINAL_GAME_BOUNDS.getWidth();
            }
            if (newBounds.getMaxY() > ORIGINAL_GAME_BOUNDS.getHeight()) {
                bottomWasReached = true;
                return;
            }
            updatePositions(newDeltaX, newDeltaY);
            nextMovement(enemyBounds);
        } else {
            updatePositions(deltaX, deltaY);
        }
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
