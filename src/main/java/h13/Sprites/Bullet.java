package h13.Sprites;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class Bullet extends Sprite {
    private final BattleShip owner;
    private final HashSet<Sprite> hits = new HashSet<>();


    public Bullet(double x, double y, Pane gameBoard, BattleShip owner) {
        super(x, y, 0.007, 0.05, Color.RED, 2, 1, gameBoard);
        this.owner = owner;
    }

    @Override
    protected void gameTick(GameTickParameters tick) {
        super.gameTick(tick);
        if (!coordinatesInBounds(tick.newX(), tick.newY(), gameBoard.getBorder().getInsets().getLeft())) {
            die();
        }

        // Hit Detection
        var damaged = gameBoard.getChildren().stream()
            .filter(BattleShip.class::isInstance)
            .map(BattleShip.class::cast)
            .filter(sprite -> sprite != owner)
            .filter(sprite -> sprite.getBoundsInParent().intersects(getBoundsInParent()))
            .filter(sprite -> !hits.contains(sprite))
            .findFirst().orElse(null);
        if (damaged != null) {
            damaged.damage(1);
            hits.add(damaged);
            damage();
        }
    }

    public BattleShip getOwner() {
        return owner;
    }
}
