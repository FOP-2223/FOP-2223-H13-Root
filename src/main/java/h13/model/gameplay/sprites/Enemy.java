package h13.model.gameplay.sprites;

import h13.controller.gamelogic.EnemyController;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.scene.paint.Color;

import static h13.controller.GameConstants.*;

public class Enemy extends BattleShip {
    private double timeTillNextShot = 2; // 2 seconds
    private final int xIndex;
    private final int yIndex;

    private final int pointsWorth;


    public Enemy(final int xIndex, final int yIndex, final double velocity, final int pointsWorth, final GameController gameController) {
        super(
            0,
            0,
            velocity,
            Color.YELLOW,
            1,
            gameController);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.pointsWorth = pointsWorth;
        // get available textures
        final var random = new java.util.Random().nextInt(1, 3);
        // choose one randomly
        loadTexture("/h13/images/sprites/enemy" + random + ".png");
    }

    @Override
    protected void nextFrame(final GameFrameParameters frame) {
        super.nextFrame(frame);

        // Shoot with a certain probability
        timeTillNextShot -= frame.elapsedTime();
        if (timeTillNextShot <= 0) {
            // Shoot at random intervals
            if (Math.random() < ENEMY_SHOOTING_PROBABILITY) {
                shoot();
                timeTillNextShot = 2;
            }
        }
    }

    public void shoot() {
        shoot(Direction.DOWN);
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

    public int getPointsWorth() {
        return pointsWorth;
    }

    @Override
    public void die() {
        super.die();
        getGameController().getPlayerController().getPlayer().addPoints(getPointsWorth());
    }
}
