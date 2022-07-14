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


    public EnemyMovement(final EnemyController enemyController, final HorizontalDirection initialMovementDirection) {
        this.enemyController = enemyController;
        movementProgress = new SimpleDoubleProperty(0);
        horizontalMovementDirection = new SimpleObjectProperty<>(initialMovementDirection);
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
    public void update(final long now) {
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
                final var horizontalSpace = getEnemyController().getGameBoard().getWidth();
                final var verticalSpace = getEnemyController().getGameBoard().getHeight();
                final var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
                final var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                final var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                for (final Enemy enemy : getEnemyController().getAliveEnemies()) {
                    final var enemyXPos = chunkSize * enemy.getxIndex() + +padding;
                    final var enemyYPos = chunkSize * enemy.getyIndex() + +padding;
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
//        for (Enemy enemy : getEnemyController().getAliveEnemies()) {
//            if (enemy.getY() + enemy.getHeight() > getEnemyController().getGameBoard().getHeight()) {
//                return true;
//            }
//        }
//        return false;
        return getEnemyController().getAliveEnemies().stream().anyMatch(enemy -> enemy.getY() + enemy.getHeight() > getEnemyController().getGameBoard().getHeight());
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
