package h13.Sprites;

import h13.Sprites.Bullet;
import h13.Sprites.Sprite;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static h13.GameConstants.RELATIVE_SHIP_WIDTH;

public class BattleShip extends Sprite {
    public BattleShip(double x, double y, double velocity, Color color, int health, Pane gameBoard) {
        super(x, y, RELATIVE_SHIP_WIDTH, RELATIVE_SHIP_WIDTH, color, velocity, health, gameBoard);
    }

    public void shoot() {
        Sprite bullet = new Bullet(getX() + getWidth() / 2, getY(), gameBoard, this);
        gameBoard.getChildren().add(gameBoard.getChildren().indexOf(this), bullet);
    }
}
