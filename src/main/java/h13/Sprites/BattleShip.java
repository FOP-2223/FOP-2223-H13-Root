package h13.Sprites;

import javafx.geometry.VerticalDirection;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static h13.GameConstants.RELATIVE_SHIP_WIDTH;

public class BattleShip extends Sprite {
    public BattleShip(double x, double y, double velocity, Color color, int health, Pane gameBoard) {
        super(x, y, RELATIVE_SHIP_WIDTH, RELATIVE_SHIP_WIDTH, color, velocity, health, gameBoard);
    }

    protected void shoot(VerticalDirection direction) {
        Sprite bullet = new Bullet(
            getX() + getWidth() / 2,
            getY(),
            gameBoard,
            this,
            direction);
        gameBoard.getChildren().add(gameBoard.getChildren().indexOf(this), bullet);
    }

    public boolean isFriend(BattleShip other) {
        return this.getClass().isInstance(other);
    }

    public boolean isEnemy(BattleShip other) {
        return !this.isFriend(other);
    }
}
