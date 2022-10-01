package h13.model.gameplay.sprites;

import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import javafx.scene.paint.Color;

/**
 * A Player is a BattleShip that can only move horizontally and shoots upwards.
 */
public class Player extends BattleShip {

    // --Variables-- //

    /**
     * The player's score.
     */
    private int score;

    /**
     * Whether the player is continuously shooting.
     */
    private boolean keepShooting = false;

    // --Constructors-- //

    /**
     * Creates a new player.
     *
     * @param x              The x-coordinate of the player.
     * @param y              The y-coordinate of the player.
     * @param gameState The game state.
     */
    public Player(final double x, final double y, final double velocity, final GameState gameState) {
        super(x, y, velocity, Color.BLUE, 5, gameState);
        loadTexture("/h13/images/sprites/player.png");
    }

    // --Getters and Setters-- //

    /**
     * Gets the player's current {@link #score}.
     *
     * @return The player's current {@link #score}.
     * @see #score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's {@link #score} to the given value.
     *
     * @param score The player's new {@link #score}.
     * @see #score
     */
    public void setScore(final int score) {
        this.score = score;
    }

    /**
     * Increments the player's {@link #score} by the given value.
     *
     * @param points The amount to increment the player's {@link #score} by.
     */
    public void addPoints(final int points) {
        score += points;
    }

    /**
     * Checks whether the player is continuously shooting.
     *
     * @return {@code true} if the player is continuously shooting.
     * @see #keepShooting
     */
    public boolean isKeepShooting() {
        return keepShooting;
    }

    /**
     * Sets the {@link #keepShooting} field to the given value.
     *
     * @param keepShooting whether the player should be continuously shooting.
     * @see #keepShooting
     */
    public void setKeepShooting(final boolean keepShooting) {
        this.keepShooting = keepShooting;
    }

    // --movement-- //

    @Override
    public void moveDown() {
        // Do nothing
    }

    @Override
    public void moveUp() {
        // Do nothing
    }

    // --Shooting-- //

    /**
     * Overloaded method from {@link BattleShip#shoot(Direction)} with the {@link Direction#UP} parameter.
     *
     * @see BattleShip#shoot(Direction)
     */
    public void shoot() {
        shoot(Direction.UP);
    }

    @Override
    public void update(double elapsedTime) {
        super.update(elapsedTime);

        if (keepShooting) {
            shoot();
        }
    }
}
