package h13.model.gameplay.sprites;

import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

public class Player extends BattleShip {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private boolean keepShooting = false;

    public Player(final double x, final double y, final double velocity, final GameController gameController) {
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
        shoot(Direction.UP);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(final int score) {
        this.score.set(score);
    }

    public void addPoints(final int points) {
        score.set(score.get() + points);
    }

    public void setKeepShooting(final boolean keepShooting) {
        this.keepShooting = keepShooting;
        if(this.keepShooting) {
            shoot();
        }
    }

    public boolean isKeepShooting() {
        return keepShooting;
    }
}
