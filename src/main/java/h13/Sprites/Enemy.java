package h13.Sprites;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Enemy extends BattleShip {
    public Enemy(double x, double y, double velocity, Pane gameBoard) {
        super(x, y, velocity, Color.YELLOW, 3, gameBoard);
    }
}
