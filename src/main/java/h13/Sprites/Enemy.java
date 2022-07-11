package h13.Sprites;

import javafx.geometry.VerticalDirection;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static h13.GameConstants.ENEMY_COLS;
import static h13.GameConstants.ENEMY_SHOOTING_PROBABILITY;

public class Enemy extends BattleShip {
    private double timeTillNextShot = 2; // 2 seconds
    private final int xIndex;
    private final int yIndex;

    public Enemy(int xIndex, int yIndex, double velocity, Pane gameBoard) {
        super(
            0,
            0,
            velocity,
            Color.YELLOW,
            1,
            gameBoard);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    @Override
    protected void gameTick(GameTickParameters tick) {
        super.gameTick(tick);
        var insets = gameBoard.getBorder().getInsets();
        var chunkSize = (gameBoard.getWidth() - insets.getLeft() - insets.getRight()) / ENEMY_COLS;
        var padding = chunkSize / 2 - getWidth() / 2;
        setX(chunkSize * xIndex + insets.getLeft() + padding);
        setY(chunkSize * yIndex + insets.getTop() + padding);
        timeTillNextShot -= tick.elapsedTime();
        if (timeTillNextShot <= 0) {
            // Shoot at random intervals
            if (Math.random() < ENEMY_SHOOTING_PROBABILITY) {
                shoot();
                timeTillNextShot = 2;
            }
        }
    }

    public void shoot() {
        shoot(VerticalDirection.DOWN);
    }
}
