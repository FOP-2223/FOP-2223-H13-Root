package h13.model;

import h13.controller.EnemyController;
import h13.model.sprites.Enemy;
import javafx.beans.property.*;
import javafx.geometry.HorizontalDirection;

import static h13.model.GameConstants.*;

public class EnemyMovement implements Playable {
    private final EnemyController enemyController;
    private final DoubleProperty movementProgress;

    private final BooleanProperty verticalMovement = new SimpleBooleanProperty(false);
    private final DoubleProperty verticalMovementIteration = new SimpleDoubleProperty(0);

    private final LongProperty lastUpdate = new SimpleLongProperty(0);

    private final SimpleObjectProperty<HorizontalDirection> horizontalMovementDirection;


    public EnemyMovement(EnemyController enemyController, HorizontalDirection initialMovementDirection) {
        this.enemyController = enemyController;
        this.movementProgress = new SimpleDoubleProperty(0);
        this.horizontalMovementDirection = new SimpleObjectProperty<>(initialMovementDirection);
    }

    public double getMovementProgress() {
        return movementProgress.get();
    }

    public HorizontalDirection getHorizontalMovementDirection() {
        return horizontalMovementDirection.get();
    }

    public SimpleObjectProperty<HorizontalDirection> horizontalMovementDirectionProperty() {
        return horizontalMovementDirection;
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void update(long now) {
        if (lastUpdate.get() > 0) {
            final double elapsedTime = (now - lastUpdate.get()) / 1_000_000_000.0;
            final double progress = movementProgress.get() + elapsedTime / (verticalMovement.get() ? VERTICAL_ENEMY_MOVEMENT_DURATION : HORIZONTAL_ENEMY_MOVEMENT_DURATION);
            movementProgress.set(progress);
            if (movementProgress.get() >= 1) {
                movementProgress.set(0);
                if (verticalMovement.get()) {
                    horizontalMovementDirection.set(horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? HorizontalDirection.RIGHT : HorizontalDirection.LEFT);
                } else {
                    verticalMovementIteration.set(verticalMovementIteration.get() + 1);
                }
                verticalMovement.set(!verticalMovement.get());
            } else {
//                var insets = getEnemyController().getGameBoard().getBorder().getInsets();
//                var horizontalSpace = getEnemyController().getGameBoard().getMaxWidth() - insets.getLeft() - insets.getRight();
//                var verticalSpace = getEnemyController().getGameBoard().getMaxHeight() - insets.getTop() - insets.getBottom();
                var horizontalSpace = getEnemyController().getGameBoard().getWidth();
                var verticalSpace = getEnemyController().getGameBoard().getHeight();
                var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
                var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                for (Enemy enemy : getEnemyController().getAliveEnemies()) {
                    var enemyXPos = chunkSize * enemy.getxIndex() + +padding;
                    var enemyYPos = chunkSize * enemy.getyIndex() + +padding;
                    if (verticalMovement.get()) {
                        enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 0 : (HORIZONTAL_ENEMY_MOVE_DISTANCE * horizontalSpace)));
                        enemy.setY(enemyYPos + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * (verticalMovementIteration.get() - 1) + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * movementProgress.get());
                    } else {
                        enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 1 - progress : progress) * (HORIZONTAL_ENEMY_MOVE_DISTANCE * horizontalSpace));
                        enemy.setY(enemyYPos + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * verticalMovementIteration.get());
                    }
                }
            }
        }
        lastUpdate.set(now);
    }

    public boolean bottomWasReached() {
        if (verticalMovement.get()) {
            return (verticalMovementIteration.get() - 1) * VERTICAL_ENEMY_MOVE_DISTANCE + VERTICAL_ENEMY_MOVE_DISTANCE * movementProgress.get() >= 1;
        } else {
            return verticalMovementIteration.get() * VERTICAL_ENEMY_MOVE_DISTANCE >= 1;
        }
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
