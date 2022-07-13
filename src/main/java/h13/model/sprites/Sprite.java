package h13.model.sprites;

import h13.model.Playable;
import h13.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Sprite extends Rectangle implements Playable {

    // --Variables--//
    protected final GameController gameController;
    private final Color color;
    private final LongProperty lastUpdate = new SimpleLongProperty(0);
    private final double relativeHeight;
    private final double relativeWidth;
    private final DoubleProperty velocityX = new SimpleDoubleProperty(0);
    private final DoubleProperty velocityY = new SimpleDoubleProperty(0);
    protected int health;
    protected double velocity;
    private boolean dead = false;

    private AnimationTimer movementTimer;

    public Sprite(double x, double y, double relativeWidth, double relativeHeight, Color color, double velocity, int health, GameController gameController) {
        super(x, y, relativeWidth * gameController.getGameBoard().getMaxWidth(), relativeHeight * gameController.getGameBoard().getMaxWidth());
        this.velocity = velocity;
        this.color = color;
        this.gameController = gameController;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.health = health;
        init();
    }

    protected boolean coordinatesInBounds(double x, double y, double padding) {
        return x >= padding && x <= getGameBoard().getMaxWidth() - getWidth() - padding
            && y >= padding && y <= getGameBoard().getMaxHeight() - getHeight() - padding;
    }

    // --Getters and Setters--//

    public void damage() {
        damage(1);
    }

    public void damage(int damage) {
//        System.out.printf("%s damaged: previous health %d, new health %d\n", this.hashCode(), health, health - damage);
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        health = 0;
        dead = true;
    }

    protected void gameTick(GameTickParameters tick) {
        var newPos = getPaddedPosition(tick.newX(), tick.newY(), getGameBoard().getBorder().getInsets().getLeft());
        setX(newPos.getX());
        setY(newPos.getY());
        if (health <= 0) {
            setVisible(false);
            getGameBoard().getChildren().remove(this);
//            tick.movementTimer.stop();
        }
    }

    public Pane getGameBoard() {
        return gameController.getGameBoard();
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getHealth() {
        return health;
    }

    public AnimationTimer getMovementTimer() {
        return movementTimer;
    }

    protected Point2D getPaddedPosition(double x, double y, double padding) {
        return new Point2D(
            Math.max(padding, Math.min(getGameBoard().getWidth() - getWidth() - padding, x)),
            Math.max(padding, Math.min(getGameBoard().getHeight() - getHeight() - padding, y))
        );
    }

    public double getRelativeHeight() {
        return relativeHeight;
    }

    public double getRelativeWidth() {
        return relativeWidth;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public double getVelocityX() {
        return velocityX.get();
    }

    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    private void init() {
        this.setFill(color);
        this.widthProperty().bind(getGameBoard().widthProperty().multiply(relativeWidth));
        this.heightProperty().bind(getGameBoard().widthProperty().multiply(relativeHeight));
//        setHeight(5);
    }

    protected double getGameboardWidth() {
        return getGameBoard().getMaxWidth() - getGameBoard().getInsets().getLeft() - getGameBoard().getInsets().getRight();
    }

    protected double getGameboardHeight() {
        return getGameBoard().getMaxHeight() - getGameBoard().getInsets().getTop() - getGameBoard().getInsets().getBottom();
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isAlive() {
        return !isDead();
    }

    public void moveDown() {
        velocityY.set(velocityY.get() + velocity * getGameboardHeight());
    }

    public void moveLeft() {
        velocityX.set(velocityX.get() - velocity * getGameboardWidth());
    }

    public void moveRight() {
        velocityX.set(velocityX.get() + velocity * getGameboardWidth());
    }

    public void moveUp() {
        velocityY.set(velocityY.get() - velocity * getGameboardHeight());
    }

    public void pause() {
        movementTimer.stop();
    }

    public void resume() {
        movementTimer.start();
    }

    public void stop() {
        velocityX.set(0);
        velocityY.set(0);
    }

    @Override
    public void update(long now) {
        // Smooth movement
        if (lastUpdate.get() > 0) {
            final double elapsedTime = (now - lastUpdate.get()) / 1_000_000_000.0;
            final double deltaX = velocityX.get() * elapsedTime;
            final double deltaY = velocityY.get() * elapsedTime;
            final double oldX = getX();
            final double oldY = getY();
            final double newX = oldX + deltaX;
            final double newY = oldY + deltaY;
            gameTick(
                new GameTickParameters(
                    getMovementTimer(),
                    now,
                    elapsedTime,
                    deltaX,
                    deltaY,
                    oldX,
                    oldY,
                    newX,
                    newY
                )
            );
        }
        lastUpdate.set(now);
    }

    public DoubleProperty velocityXProperty() {
        return velocityX;
    }

    public DoubleProperty velocityYProperty() {
        return velocityY;
    }

    protected record GameTickParameters(
        AnimationTimer movementTimer,
        long now,
        /**
         * The time elapsed since the last update in seconds.
         */
        double elapsedTime,
        double deltaX,
        double deltaY,
        double oldX,
        double oldY,
        double newX,
        double newY
    ) {
    }
}
