package h13.model.sprites;

import h13.controller.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

import static h13.model.GameConstants.RELATIVE_SHIP_WIDTH;

public class BattleShip extends Sprite {
    public BattleShip(double x, double y, double velocity, Color color, int health, GameController gameController) {
        super(x, y, RELATIVE_SHIP_WIDTH, RELATIVE_SHIP_WIDTH, color, velocity, health, gameController);
    }

    protected void shoot(VerticalDirection direction) {
        Sprite bullet = new Bullet(
            getBoundsInParent().getMinX() + getWidth() / 2,
            getBoundsInParent().getMinY(),
            getGameController(),
            this,
            direction);
        getGameBoard().getChildren().add(getGameBoard().getChildren().indexOf(this), bullet);
    }

    public boolean isFriend(BattleShip other) {
        return this.getClass().isInstance(other);
    }

    public boolean isEnemy(BattleShip other) {
        return !this.isFriend(other);
    }
}
