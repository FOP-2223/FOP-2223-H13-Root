package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.game.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

import static h13.controller.GameConstants.RELATIVE_SHIP_WIDTH;

public class BattleShip extends Sprite {
    public BattleShip(final double x, final double y, final double velocity, final Color color, final int health, final GameController gameController) {
        super(x, y, RELATIVE_SHIP_WIDTH, RELATIVE_SHIP_WIDTH, color, velocity, health, gameController);
    }

    protected void shoot(final VerticalDirection direction) {
        if (hasBullet() && !ApplicationSettings.instantShootingProperty().get()) {
            return;
        }
        final Sprite bullet = new Bullet(
            getX() + getWidth() / 2,
            getY(),
            getGameController(),
            this,
            direction);
        getGameController().addSprite(bullet);
    }

    public boolean isFriend(final BattleShip other) {
        return getClass().isInstance(other);
    }

    public boolean isEnemy(final BattleShip other) {
        return !isFriend(other);
    }

    public boolean hasBullet() {
        return getGameController().getSprites(Bullet.class).stream().anyMatch(bullet -> bullet.getOwner() == this);
    }
}
