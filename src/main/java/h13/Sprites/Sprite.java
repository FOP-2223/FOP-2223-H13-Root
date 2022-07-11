package h13.Sprites;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {
    protected double velocity;
    private final double relativeWidth;
    private final double relativeHeight;
    protected final Pane gameBoard;
    private final DoubleProperty velocityX = new SimpleDoubleProperty(0);
    private final DoubleProperty velocityY = new SimpleDoubleProperty(0);
    private final LongProperty lastUpdate = new SimpleLongProperty(0);
    protected int health;
    private boolean dead = false;

    private AnimationTimer movementTimer;

    protected boolean coordinatesInBounds(double x, double y, double padding) {
        return x >= padding && x <= gameBoard.getWidth() - padding && y >= padding && y <= gameBoard.getHeight() - padding;
    }

    protected Point2D getPaddedPosition(double x, double y, double padding) {
        return new Point2D(
            Math.max(padding, Math.min(gameBoard.getWidth() - getWidth() - padding, x)),
            Math.max(padding, Math.min(gameBoard.getHeight() - getHeight() - padding, y))
        );
    }

    public Sprite(double x, double y, double relativeWidth, double relativeHeight, Color color, double velocity, int health, Pane gameBoard) {
        super(x, y, relativeWidth * gameBoard.getWidth(), relativeHeight * gameBoard.getWidth());
        this.velocity = velocity;
        this.setFill(color);
        this.gameBoard = gameBoard;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.widthProperty().bind(gameBoard.widthProperty().multiply(relativeWidth));
        this.heightProperty().bind(gameBoard.widthProperty().multiply(relativeHeight));
        this.health = health;

        // Smooth movement
        movementTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate.get() > 0) {
                    final double elapsedTime = (now - lastUpdate.get()) / 1_000_000_000.0;
                    final double deltaX = velocityX.get() * elapsedTime;
                    final double deltaY = velocityY.get() * elapsedTime;
                    final double oldX = getX();
                    final double oldY = getY();
                    final double newX = oldX + deltaX;
                    final double newY = oldY + deltaY;
                    gameTick(new GameTickParameters(
                        this,
                        now,
                        elapsedTime,
                        deltaX,
                        deltaY,
                        oldX,
                        oldY,
                        newX,
                        newY));
                }
                lastUpdate.set(now);
            }
        };
        movementTimer.start();
    }

    protected record GameTickParameters(
        AnimationTimer movementTimer,
        long now,
        double elapsedTime,
        double deltaX,
        double deltaY,
        double oldX,
        double oldY,
        double newX,
        double newY
    ) {
    }

    protected void gameTick(GameTickParameters tick) {
        var newPos = getPaddedPosition(tick.newX(), tick.newY(), gameBoard.getBorder().getInsets().getLeft());
        setX(newPos.getX());
        setY(newPos.getY());
        if (health <= 0) {
            setVisible(false);
            gameBoard.getChildren().remove(this);
            tick.movementTimer.stop();
        }
    }

    public double getVelocityX() {
        return velocityX.get();
    }

    public double getVelocityY() {
        return velocityY.get();
    }

    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    public void setVelocityY(double velocityY) {
        this.velocityY.set(velocityY);
    }

    public void moveLeft() {
        velocityX.set(velocityX.get() - velocity * gameBoard.getWidth());
    }

    public void moveRight() {
        velocityX.set(velocityX.get() + velocity * gameBoard.getWidth());
    }

    public void moveUp() {
        velocityY.set(velocityY.get() - velocity * gameBoard.getHeight());
    }

    public void moveDown() {
        velocityY.set(velocityY.get() + velocity * gameBoard.getHeight());
    }

    public void stop() {
        velocityX.set(0);
        velocityY.set(0);
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getRelativeWidth() {
        return relativeWidth;
    }

    public double getRelativeHeight() {
        return relativeHeight;
    }

    public Pane getGameBoard() {
        return gameBoard;
    }

    public int getHealth() {
        return health;
    }

    public void damage(int damage) {
        System.out.printf("%s damaged: previous health %d, new health %d\n", this.hashCode(), health, health - damage);
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void damage() {
        damage(1);
    }

    public void die() {
        health = 0;
        stop();
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public AnimationTimer getMovementTimer() {
        return movementTimer;
    }
}
