package h13.model.gameplay.sprites;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.scene.paint.Color;

import java.util.HashSet;

import static h13.controller.GameConstants.BULLET_VELOCITY;
import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;

public class Bullet extends Sprite {
    private final BattleShip owner;
    private final HashSet<Sprite> hits = new HashSet<>();


    public Bullet(final double x, final double y, final GameController gameController, final BattleShip owner, final Direction direction) {
        super(x, y, 1, 5, Color.WHITE, BULLET_VELOCITY, 1, gameController);
        this.owner = owner;
        setDirection(direction);
    }

    @Override
    protected void nextFrame(final GameFrameParameters frame) {
        super.nextFrame(frame);

        // If the bullet reaches the edge of the game Board, remove it.
        if (!ORIGINAL_GAME_BOUNDS.contains(frame.newX(), frame.newY())) {
            die();
        }

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

    @Override
    public void die() {
        super.die();
        owner.setBullet(null);
        if (owner instanceof Player p && p.isKeepShooting()) {
            p.shoot();
        }
    }

    public BattleShip getOwner() {
        return owner;
    }
}
