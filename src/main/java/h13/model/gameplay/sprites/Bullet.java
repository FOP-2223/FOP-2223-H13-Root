package h13.model.gameplay.sprites;

import h13.controller.scene.game.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

import java.util.HashSet;

import static h13.controller.GameConstants.BULLET_VELOCITY;

public class Bullet extends Sprite {
    private final BattleShip owner;
    private final HashSet<Sprite> hits = new HashSet<>();

    private final VerticalDirection direction;


    public Bullet(final double x, final double y, final GameController gameController, final BattleShip owner, final VerticalDirection direction) {
        super(x, y, 0.003, 0.02, Color.WHITE, BULLET_VELOCITY, 1, gameController);
        this.owner = owner;
        this.direction = direction;
        velocityYProperty().bind(getGameBoard().heightProperty().multiply(direction.equals(VerticalDirection.UP) ? -getVelocity() : getVelocity()));
    }

    @Override
    protected void gameTick(final GameTickParameters tick) {
        super.gameTick(tick);

        // If the bullet reaches the edge of the game Board, remove it.
        if (!coordinatesInBounds(tick.newX(), tick.newY(), 0)) {
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
    }

    public BattleShip getOwner() {
        return owner;
    }

    public VerticalDirection getDirection() {
        return direction;
    }
}
