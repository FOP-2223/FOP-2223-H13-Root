package h13.model.sprites;

import h13.controller.EnemyController;
import h13.controller.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static h13.model.GameConstants.*;

public class Enemy extends BattleShip {
    private double timeTillNextShot = 2; // 2 seconds
    private final int xIndex;
    private final int yIndex;


    public Enemy(int xIndex, int yIndex, double velocity, GameController gameController) {
        super(
            0,
            0,
            velocity,
            Color.YELLOW,
            1,
            gameController);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        // get available textures
        var random = new java.util.Random().nextInt(1, 3);
        // choose one randomly
        loadTexture("/h13/images/sprites/enemy" + random + ".png");
    }

    @Override
    protected void gameTick(GameTickParameters tick) {
        super.gameTick(tick);

        // Shoot with a certain probability
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

    public EnemyController getEnemyController() {
        return getGameController().getEnemyController();
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }
}
