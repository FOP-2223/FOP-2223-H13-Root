package h13.model.sprites;

import h13.controller.GameController;
import javafx.geometry.VerticalDirection;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player extends BattleShip {
    public Player(double x, double y, double velocity, GameController gameController) {
        super(x, y, velocity, Color.BLUE, 5, gameController);
        loadTexture("/h13/images/sprites/player.png");
    }

    @Override
    public void moveDown() {
        // Do nothing
    }

    @Override
    public void moveUp() {
        // Do nothing
    }

    public void shoot() {
        shoot(VerticalDirection.UP);
    }
}
