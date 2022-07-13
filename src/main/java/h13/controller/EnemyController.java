package h13.controller;

import h13.model.GameConstants;
import h13.model.Playable;
import h13.model.sprites.Enemy;
import javafx.beans.property.*;
import javafx.geometry.HorizontalDirection;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Set;

import static h13.model.GameConstants.*;

public class EnemyController implements Playable {
    private GameController gameController;
    private Set<Enemy> enemies;
    private final DoubleProperty movementProgress;

    private final BooleanProperty verticalMovement = new SimpleBooleanProperty(false);

    private final double horizontalMovementDuration = 2;
    private final double verticalMovementDuration = .5;
    private final double verticalMovementDistance = .1;
    private final DoubleProperty verticalMovementIteration = new SimpleDoubleProperty(0);

    private final LongProperty lastUpdate = new SimpleLongProperty(0);

    private final SimpleObjectProperty<HorizontalDirection> horizontalMovementDirection;

    public EnemyController(
        GameController gameController,
        HorizontalDirection initialMovementDirection) {
        super();
        this.enemies = new HashSet<>();
        this.movementProgress = new SimpleDoubleProperty(0);
        this.horizontalMovementDirection = new SimpleObjectProperty<>(initialMovementDirection);
        this.gameController = gameController;
//        this.setPrefRows(5);
//        this.setPrefColumns(11);
//        var widthWithoutMargin = getGameBoard().getMaxWidth() - getGameBoard().getInsets().getLeft() - getGameBoard().getInsets().getRight();
//        setWidth(widthWithoutMargin * (1 - HORIZONTAL_ENEMY_MOVE_SPACE));
//        setVgap(widthWithoutMargin * SHIP_PADING);
//        setHgap(widthWithoutMargin * SHIP_PADING);
//        setLayoutX(getGameBoard().getInsets().getLeft());
//        setLayoutY(getGameBoard().getInsets().getTop());
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
                    getGameController()
                );
                var insets = getGameBoard().getBorder().getInsets();
                var horizontalSpace = getGameBoard().getMaxWidth() - insets.getLeft() - insets.getRight();
                var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_SPACE);
                var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                enemy.setX(chunkSize * i + insets.getLeft() + padding);
                enemy.setY(chunkSize * j + insets.getTop() + padding);

                getGameBoard().getChildren().add(enemy);
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

    public Pane getGameBoard() {
        return gameController.getGameBoard();
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
            final double progress = movementProgress.get() + elapsedTime / (verticalMovement.get() ? verticalMovementDuration : horizontalMovementDuration);
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
                var insets = getGameBoard().getBorder().getInsets();
                var horizontalSpace = getGameBoard().getMaxWidth() - insets.getLeft() - insets.getRight();
                var verticalSpace = getGameBoard().getMaxHeight() - insets.getTop() - insets.getBottom();
                var horizontalEnemySpace = horizontalSpace * (1 - HORIZONTAL_ENEMY_MOVE_SPACE);
                var chunkSize = horizontalEnemySpace / ENEMY_COLS;
                var padding = chunkSize / 2 - GameConstants.RELATIVE_SHIP_WIDTH * horizontalSpace / 2;
                for (Enemy enemy : getAliveEnemies()) {
                    var enemyXPos = chunkSize * enemy.getxIndex() + insets.getLeft() + padding;
                    var enemyYPos = chunkSize * enemy.getyIndex() + insets.getTop() + padding;
                    if (verticalMovement.get()) {
                        enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 0 : chunkSize));
                        enemy.setY(enemyYPos + (verticalMovementDistance * verticalSpace) * (verticalMovementIteration.get() - 1) + (verticalMovementDistance * verticalSpace) * movementProgress.get());
                    } else {
                        enemy.setX(enemyXPos + (horizontalMovementDirection.get().equals(HorizontalDirection.LEFT) ? 1 - progress : progress) * chunkSize);
                        enemy.setY(enemyYPos + (verticalMovementDistance * verticalSpace) * verticalMovementIteration.get());
                    }
                }
            }
        }
        lastUpdate.set(now);
    }
}
