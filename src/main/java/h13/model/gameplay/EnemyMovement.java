package h13.model.gameplay;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.gamelogic.EnemyController;
import h13.model.gameplay.sprites.Enemy;
import javafx.beans.property.*;
import javafx.geometry.HorizontalDirection;

import static h13.controller.GameConstants.*;

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

        if (!ApplicationSettings.enemyHorizontalMovementProperty().get()) {
            horizontalMovementDirection.set(HorizontalDirection.LEFT);
            verticalMovement.set(true);
        }
        if (lastUpdate.get() > 0) {
            if (ApplicationSettings.enemyVerticalMovementProperty().get() || ApplicationSettings.enemyHorizontalMovementProperty().get()) {
                final double elapsedTime = (now - lastUpdate.get()) / 1_000_000_000.0;
                final double progress = movementProgress.get() + elapsedTime / (verticalMovement.get() ? VERTICAL_ENEMY_MOVEMENT_DURATION : HORIZONTAL_ENEMY_MOVEMENT_DURATION);
                movementProgress.set(progress);
            }
            if (movementProgress.get() >= 1) {
                nextMovement();
            } else {
                updatePositions();
            }
        }
        lastUpdate.set(now);
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void resume(final long now) {
        lastUpdate.set(now);
    }

    private void updatePositions() {
        final var horizontalSpace = getEnemyController().getGameBoard().getWidth();
        final var verticalSpace = getEnemyController().getGameBoard().getHeight();
        final var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_DISTANCE);
        final var chunkSize = horizontalEnemySpace / ENEMY_COLS;
        final var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
        for (final Enemy enemy : getEnemyController().getAliveEnemies()) {
            final var enemyXPos = chunkSize * enemy.getxIndex() + padding;
            final var enemyYPos = chunkSize * enemy.getyIndex() + padding + enemyController.getYOffset();
            if (verticalMovement.get()) {
                enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 0 : (HORIZONTAL_ENEMY_MOVE_DISTANCE * horizontalSpace)));
                enemy.setY(enemyYPos + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * (verticalMovementIteration.get() - 1) + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * movementProgress.get());
            } else {
                enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 1 - getMovementProgress() : getMovementProgress()) * (HORIZONTAL_ENEMY_MOVE_DISTANCE * horizontalSpace));
                enemy.setY(enemyYPos + (VERTICAL_ENEMY_MOVE_DISTANCE * verticalSpace) * verticalMovementIteration.get());
            }
        }
    }

    private void nextMovement() {
        movementProgress.set(0);
        if (ApplicationSettings.enemyHorizontalMovementProperty().get() && (verticalMovement.get() || !ApplicationSettings.enemyVerticalMovementProperty().get())) {
            horizontalMovementDirection.set(horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? HorizontalDirection.RIGHT : HorizontalDirection.LEFT);
        } else {
            verticalMovementIteration.set(verticalMovementIteration.get() + 1);
        }
        if (ApplicationSettings.enemyHorizontalMovementProperty().get() && ApplicationSettings.enemyVerticalMovementProperty().get()) {
            verticalMovement.set(!verticalMovement.get());
        }
    }

    public boolean bottomWasReached() {
        return getEnemyController()
            .getAliveEnemies()
            .stream()
            .anyMatch(enemy -> enemy.getY() + enemy.getHeight() > getEnemyController().getGameBoard().getHeight());
    }

    public EnemyController getEnemyController() {
        return enemyController;
    }
}
