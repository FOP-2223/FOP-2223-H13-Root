package h13.Sprites;

import javafx.geometry.VerticalDirection;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class Bullet extends Sprite {
    private final BattleShip owner;
    private final HashSet<Sprite> hits = new HashSet<>();

    VerticalDirection direction;


    public Bullet(double x, double y, Pane gameBoard, BattleShip owner, VerticalDirection direction) {
        super(x, y, 0.003, 0.02, Color.RED, 1, 1, gameBoard);
        this.owner = owner;
        this.direction = direction;
        velocityYProperty().bind(gameBoard.heightProperty().multiply(direction.equals(VerticalDirection.UP) ? -getVelocity() : getVelocity()));
    }

    @Override
    protected void gameTick(GameTickParameters tick) {
        super.gameTick(tick);
        if (!coordinatesInBounds(tick.newX(), tick.newY(), gameBoard.getBorder().getInsets().getLeft())) {
            die();
            System.out.println("Bullet out of bounds");
        }

        // Hit Detection
        var damaged = gameBoard.getChildren().stream()
            .filter(BattleShip.class::isInstance)
            .map(BattleShip.class::cast)
            .filter(sprite -> sprite != owner)
            .filter(owner::isEnemy)
            .filter(sprite -> sprite.getBoundsInParent().intersects(getBoundsInParent()))
            .filter(sprite -> !hits.contains(sprite))
            .findFirst().orElse(null);
        if (damaged != null) {
            damaged.damage(1);
            damage();
            hits.add(damaged);
        }
    }

    public BattleShip getOwner() {
        return owner;
    }
}
