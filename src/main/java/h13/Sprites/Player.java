package h13.Sprites;

import javafx.geometry.VerticalDirection;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Player extends BattleShip {
    public Player(double x, double y, double velocity, Pane gameBoard) {
        super(x, y, velocity, Color.BLUE, 5, gameBoard);
    }

    @Override
    public void moveDown() {

    }

    @Override
    public void moveUp() {

    }

    public void shoot() {
        shoot(VerticalDirection.UP);
    }
}
