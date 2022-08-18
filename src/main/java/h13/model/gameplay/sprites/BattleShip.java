package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;
import static h13.controller.GameConstants.RELATIVE_SHIP_WIDTH;

/**
 * A BattleShip is a Sprite that can shoot and be friends or enemies with other BattleShips.
 */
public class BattleShip extends Sprite {
    // --Variables-- //

    /**
     * The Ship's current {@link Bullet}.
     */
    private @Nullable Bullet bullet;

    // --Constructors-- //

    /**
     * Creates a new BattleShip.
     *
     * @param x              The x-coordinate of the BattleShip.
     * @param y              The y-coordinate of the BattleShip.
     * @param velocity       The velocity of the BattleShip.
     * @param color          The color of the BattleShip.
     * @param health         The health of the BattleShip.
     * @param gameController The game controller.
     */
    public BattleShip(final double x, final double y, final double velocity, final Color color, final int health, final GameController gameController) {
        super(
            x,
            y,
            RELATIVE_SHIP_WIDTH * ORIGINAL_GAME_BOUNDS.getWidth(),
            RELATIVE_SHIP_WIDTH * ORIGINAL_GAME_BOUNDS.getWidth(),
            color,
            velocity,
            health,
            gameController
        );
    }

    // --Getters and Setters--//

    /**
     * Gets the Ship's current {@link #bullet}.
     *
     * @return The Ship's current {@link #bullet}.
     * @see #bullet
     */
    public @Nullable Bullet getBullet() {
        return bullet;
    }

    /**
     * Checks whether the Ship current has a {@link #bullet}.
     *
     * @return {@code true} if the Ship has a {@link #bullet}, {@code false} otherwise.
     */
    public boolean hasBullet() {
        return bullet != null;
    }

    /**
     * Sets the Ship's {@link #bullet} to the given value.
     *
     * @param bullet The Ship's new {@link #bullet}.
     * @see #bullet
     */
    public void setBullet(@Nullable final Bullet bullet) {
        this.bullet = bullet;
    }

    /**
     * Checks whether the given {@link BattleShip} is befriended with this Ship.
     *
     * @param other The other {@link BattleShip} to check.
     * @return {@code true} if the given {@link BattleShip} is befriended with this Ship, {@code false} otherwise.
     */
    public boolean isFriend(final BattleShip other) {
        return getClass().isInstance(other);
    }

    /**
     * Checks whether the given {@link BattleShip} is an enemy of this Ship.
     *
     * @param other The other {@link BattleShip} to check.
     * @return {@code true} if the given {@link BattleShip} is an enemy uf this Ship, {@code false} otherwise.
     * @see #isFriend(BattleShip)
     */
    public boolean isEnemy(final BattleShip other) {
        return !isFriend(other);
    }

    /**
     * Shoots a {@link Bullet} from the Center of the Ship in the given {@link Direction}.
     *
     * @param direction The {@link Direction} to shoot the {@link Bullet} towards.
     */
    protected void shoot(final Direction direction) {
        if (hasBullet() && !ApplicationSettings.instantShootingProperty().get()) {
            return;
        }
        setBullet(new Bullet(
            getX() + getWidth() / 2,
            getY(),
            getGameController(),
            this,
            direction));
        getGameController().addSprite(getBullet());
    }
}
