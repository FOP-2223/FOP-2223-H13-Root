package h13.model.gameplay.sprites;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

import static h13.controller.GameConstants.BULLET_VELOCITY;
import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;

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
     * @param x              The initial x-coordinate of the Bullet.
     * @param y              The initial y-coordinate of the Bullet.
     * @param gameController The game controller.
     * @param owner          The owner of the Bullet.
     * @param direction      The direction the Bullet is travelling towards.
     */
    public Bullet(final double x, final double y, final GameController gameController, final BattleShip owner, final Direction direction) {
        super(x, y, 1, 5, Color.WHITE, BULLET_VELOCITY, 1, gameController);
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
    public void die() {
        super.die();
        owner.setBullet(null);
        if (owner instanceof Player p && p.isKeepShooting()) {
            p.shoot();
        }
    }

    @Override
    protected void onOutOfBounds() {
        // If the bullet reaches the edge of the game Board, remove it.
        die();
    }

    @Override
    public void update(final double elapsedTime) {
        super.update(elapsedTime);

        // Hit Detection
        final var damaged = getGameController().getSprites().stream()
            .filter(BattleShip.class::isInstance)
            .map(BattleShip.class::cast)
            .filter(sprite -> sprite != owner)
            .filter(owner::isEnemy)
            .filter(sprite -> sprite.getBounds().intersects(getBounds()))
            .filter(sprite -> !hits.contains(sprite))
            .findFirst().orElse(null);
        if (damaged != null) {
            damaged.damage(1);
            damage();
            hits.add(damaged);
        }
    }
}
