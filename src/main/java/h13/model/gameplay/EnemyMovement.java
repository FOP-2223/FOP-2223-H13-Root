package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.gamelogic.EnemyController;
import h13.model.gameplay.sprites.Enemy;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import org.jetbrains.annotations.Nullable;

import static h13.controller.GameConstants.*;

public class EnemyMovement implements Updatable {
    private final EnemyController enemyController;
    private double velocity = 10;

    private double yTarget = 0;
    private Direction direction;

    private @Nullable Direction previousDirection;

    private boolean bottomWasReached = false;


    public EnemyMovement(final EnemyController enemyController, final Direction direction) {
        this.enemyController = enemyController;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private boolean targetReached(final Bounds enemyBounds) {
        return direction == Direction.UP && enemyBounds.getMinY() <= yTarget
            || direction == Direction.DOWN && enemyBounds.getMaxY() >= yTarget
            || direction == Direction.LEFT && enemyBounds.getMinX() <= 0
            || direction == Direction.RIGHT && enemyBounds.getMaxX() >= ORIGINAL_GAME_BOUNDS.getWidth();
    }

    private @Nullable Bounds getEnemyBounds() {
        final var enemies = getEnemyController().getAliveEnemies();
        if (enemies.isEmpty()) {
            return null;
        }
        boolean first = true;
        int lowestxIndex, highestxIndex, lowestyIndex, highestyIndex;
        lowestxIndex = highestxIndex = lowestyIndex = highestyIndex = 0;
        Enemy enemyWithLowestXIndex = null, enemyWithLowestYIndex = null, enemyWithHighestXIndex = null, enemyWithHighestYIndex = null;
        for (var e : enemies) {
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
     * @param elapsedTime The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
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

    private void updatePositions(final double deltaX, final double deltaY) {
        final var enemies = getEnemyController().getAliveEnemies();
        for (var e : enemies) {
            e.setX(e.getX() + deltaX);
            e.setY(e.getY() + deltaY);
        }
    }

    private void nextMovement(Bounds enemyBounds) {
        if (ApplicationSettings.enemyHorizontalMovementProperty().get() && ApplicationSettings.enemyVerticalMovementProperty().get()) {
            var oldDirection = direction;
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

    public boolean bottomWasReached() {
        return bottomWasReached;
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
