package h13.model.gameplay.sprites;

import h13.model.gameplay.Direction;
import h13.model.gameplay.GameState;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

import static h13.controller.GameConstants.BULLET_VELOCITY;

/**
 * A Bullet is a Sprite that can be fired by a BattleShip and can hit other BattleShips.
 *
 * <ul>
 *   <li>A Bullet cannot damage itself, other Bullets, its owner or Friends of its owner.</li>
 *   <li>A Bullet can only damage the same BattleShip only once.</li>
 * </ul>
 */
public class Bullet extends Sprite {
    // --Variables-- //

    /**
     * The owner of the Bullet.
     */
    private final BattleShip owner;

    /**
     * The set of BattleShips that have been damaged by the Bullet.
     */
    private final Set<Sprite> hits = new HashSet<>();

    // --Constructors-- //

    /**
     * Creates a new Bullet.
     *
     * @param x         The initial x-coordinate of the Bullet.
     * @param y         The initial y-coordinate of the Bullet.
     * @param gameState The game state.
     * @param owner     The owner of the Bullet.
     * @param direction The direction the Bullet is travelling towards.
     */
    public Bullet(final double x, final double y, final GameState gameState, final BattleShip owner, final Direction direction) {
        super(x, y, 1, 5, Color.WHITE, BULLET_VELOCITY, 1, gameState);
        this.owner = owner;
        setDirection(direction);
    }

    // --Getters and Setters-- //

    /**
     * Gets the owner of the Bullet.
     *
     * @return The owner of the Bullet.
     * @see #owner
     */
    public BattleShip getOwner() {
        return owner;
    }

    /**
     * Gets the set of BattleShips that have been damaged by the Bullet.
     *
     * @return The set of BattleShips that have been damaged by the Bullet.
     * @see #hits
     */
    public Set<Sprite> getHits() {
        return hits;
    }

    @Override
    protected void onOutOfBounds(Bounds newBounds) {
        // If the bullet reaches the edge of the game Board, remove it.
        die();
    }

    @Override
    public void die() {
        super.die();

        owner.setBullet(null);
    }

    /**
     * Checks if the Bullet can damage the given Sprite.
     *
     * @param other The Sprite to check.
     * @return True if the Bullet can damage the given Sprite.
     */
    public boolean hit(BattleShip other) {
        return owner.isEnemy(other) && other.getBounds().intersects(getBounds()) && hits.add(other);
    }
}
