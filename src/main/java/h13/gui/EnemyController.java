package h13.gui;

import h13.Playable;
import h13.Sprites.Enemy;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HorizontalDirection;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static h13.GameConstants.*;

public class EnemyController implements Playable {
    private Pane gameBoard;
    private Set<Enemy> enemies;
    private final DoubleProperty horizontalMovementProgress;
    private final SimpleObjectProperty<HorizontalDirection> horizontalMovementDirection;

    public EnemyController(
        Pane gameBoard,
        HorizontalDirection initialMovementDirection) {
        this.gameBoard = gameBoard;
        this.enemies = new HashSet<>();
        this.horizontalMovementProgress = new SimpleDoubleProperty(0);
        this.horizontalMovementDirection = new SimpleObjectProperty<>(initialMovementDirection);
        init();
    }

    private void init() {
        // enemies
        for (int i = 0; i < ENEMY_COLS; i++) {
            for (int j = 0; j < ENEMY_ROWS; j++) {
                var enemy = new Enemy(
                    i,
                    j,
                    1.5,
                    this
                );
                gameBoard.getChildren().add(enemy);
                enemies.add(enemy);

                var insetsProperty = gameBoard.insetsProperty();
                var horizontalSpace = gameBoard.widthProperty().subtract(insetsProperty.get().getLeft()).subtract(insetsProperty.get().getRight());
                var horizontalEnemySpace = horizontalSpace.multiply(1 - HORIZONTAL_ENEMY_MOVE_SPACE);
                var chunkSize = horizontalEnemySpace.divide(ENEMY_COLS);
                var padding = chunkSize.divide(2).subtract(enemy.widthProperty().divide(2));

                // act
                TranslateTransition movement = new TranslateTransition();
                movement.setNode(enemy);
                movement.setDuration(Duration.seconds(1));
                movement.setByX(HORIZONTAL_ENEMY_MOVE_SPACE * gameBoard.getMaxWidth());
                movement.setCycleCount(TranslateTransition.INDEFINITE);
                movement.setAutoReverse(true);
                System.out.println("movement.byXProperty().get(): " + movement.byXProperty().get());
                System.out.println(gameBoard.widthProperty().get());
                movement.play();
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

    public Pane getGameBoard() {
        return gameBoard;
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void update(long now) {

    }
}
