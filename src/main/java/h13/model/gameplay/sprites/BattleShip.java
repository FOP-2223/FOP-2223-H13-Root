package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;
import static h13.controller.GameConstants.RELATIVE_SHIP_WIDTH;

public class BattleShip extends Sprite {
    private @Nullable Bullet bullet;
    public BattleShip(final double x, final double y, final double velocity, final Color color, final int health, final GameController gameController) {
        super(x, y, RELATIVE_SHIP_WIDTH * ORIGINAL_GAME_BOUNDS.getWidth(), RELATIVE_SHIP_WIDTH * ORIGINAL_GAME_BOUNDS.getWidth(), color, velocity, health, gameController);
    }

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

    public boolean isFriend(final BattleShip other) {
        return getClass().isInstance(other);
    }

    public boolean isEnemy(final BattleShip other) {
        return !isFriend(other);
    }

    public boolean hasBullet() {
        return bullet != null;
    }

    public void setBullet(@Nullable final Bullet bullet) {
        this.bullet = bullet;
    }

    public @Nullable Bullet getBullet() {
        return bullet;
    }
}
