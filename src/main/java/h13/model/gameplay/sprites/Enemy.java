package h13.model.gameplay.sprites;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.scene.paint.Color;

import static h13.controller.GameConstants.*;

/**
 * An Enemy is a BattleShip that is moved by the EnemyController and shoots downwards.
 */
public class Enemy extends BattleShip {
    // --Variables-- //

    /**
     * The enemy's X-index of the enemy grid.
     */
    private final int xIndex;
    /**
     * The enemy's Y-index of the enemy grid.
     */
    private final int yIndex;
    /**
     * The amount of points the enemy is worth when it is destroyed.
     */
    private final int pointsWorth;
    /**
     * The remaining shot cooldown-time of the enemy.
     */
    private double timeTillNextShot = 2; // 2 seconds

    // --Constructors-- //

    /**
     * Creates a new enemy.
     *
     * @param xIndex         The enemy's X-index of the enemy grid.
     * @param yIndex         The enemy's Y-index of the enemy grid.
     * @param velocity       The enemy's velocity.
     * @param pointsWorth    The amount of points the enemy is worth when it is destroyed.
     * @param gameController The game controller.
     */
    public Enemy(final int xIndex, final int yIndex, final double velocity, final int pointsWorth,
                 final GameController gameController) {
        super(0, 0, velocity, Color.YELLOW, 1, gameController);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.pointsWorth = pointsWorth;
        // get available textures
        final var random = new java.util.Random().nextInt(1, 3);
        // choose one randomly
        loadTexture("/h13/images/sprites/enemy" + random + ".png");
    }

    // --Getters and Setters-- //

    /**
     * Gets the enemy's X-index of the enemy grid.
     *
     * @return The enemy's X-index of the enemy grid.
     */
    public int getxIndex() {
        return xIndex;
    }

    /**
     * Gets the enemy's Y-index of the enemy grid.
     *
     * @return The enemy's Y-index of the enemy grid.
     */
    public int getyIndex() {
        return yIndex;
    }

    /**
     * Gets the amount of points the enemy is worth when it is destroyed.
     *
     * @return The amount of points the enemy is worth when it is destroyed.
     */
    public int getPointsWorth() {
        return pointsWorth;
    }

    // --Utility Methods-- //

    /**
     * Overloaded method from {@link BattleShip#shoot(Direction)} with the {@link Direction#DOWN} parameter.
     *
     * @see BattleShip#shoot(Direction)
     */
    public void shoot() {
        shoot(Direction.DOWN);
    }

    @Override
    public void die() {
        super.die();
        getGameController().getPlayerController().getPlayer().addPoints(getPointsWorth());
    }

    // --update-- //
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
}
