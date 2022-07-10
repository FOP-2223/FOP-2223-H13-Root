package h13;

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
    private final SpriteType type;
    private double velocity;
    private final double relativeWidth;
    private final double relativeHeight;
    private final Pane gameBoard;
    private final DoubleProperty velocityX = new SimpleDoubleProperty(0);
    private final DoubleProperty velocityY = new SimpleDoubleProperty(0);
    private final LongProperty lastUpdate = new SimpleLongProperty(0);

    private boolean coordinatesInBounds(double x, double y, double padding) {
        return x >= padding && x <= gameBoard.getWidth() - padding && y >= padding && y <= gameBoard.getHeight() - padding;
    }

    private Point2D getPaddedPosition(double x, double y, double padding) {
        return new Point2D(
            Math.max(padding, Math.min(gameBoard.getWidth() - getWidth() - padding, x)),
            Math.max(padding, Math.min(gameBoard.getHeight() - getHeight() - padding, y))
        );
    }

    public Sprite(double x, double y, double relativeWidth, double relativeHeight, Color color, SpriteType type, double velocity, Pane gameBoard) {
        super(x, y, relativeWidth * gameBoard.getWidth(), relativeHeight * gameBoard.getWidth());
        this.type = type;
        this.velocity = velocity;
        this.setFill(color);
        this.gameBoard = gameBoard;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.widthProperty().bind(gameBoard.widthProperty().multiply(relativeWidth));
        this.heightProperty().bind(gameBoard.widthProperty().multiply(relativeHeight));

        // Smooth movement
        AnimationTimer movementTimer = new AnimationTimer() {
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
                    if (type == SpriteType.BULLET && !coordinatesInBounds(newX, newY, gameBoard.getBorder().getInsets().getLeft())) {
                        setVisible(false);
                        gameBoard.getChildren().remove(this);
                        this.stop();
                    }
                    var newPos = getPaddedPosition(newX, newY, gameBoard.getBorder().getInsets().getLeft());
                    setX(newPos.getX());
                    setY(newPos.getY());
                }
                lastUpdate.set(now);
            }
        };
        movementTimer.start();
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

    void moveLeft() {
        velocityX.set(velocityX.get() - velocity * gameBoard.getWidth());
    }

    void moveRight() {
        velocityX.set(velocityX.get() + velocity * gameBoard.getWidth());
    }

    void moveUp() {
        velocityY.set(velocityY.get() - velocity * gameBoard.getHeight());
    }

    void moveDown() {
        velocityY.set(velocityY.get() + velocity * gameBoard.getHeight());
    }

    void stop() {
        velocityX.set(0);
        velocityY.set(0);
    }

    void shoot() {
        Sprite bullet = new Sprite(getX() + getWidth() / 2, getY(), 0.007, 0.05, Color.RED, SpriteType.BULLET, 2, gameBoard);
        gameBoard.getChildren().add(gameBoard.getChildren().indexOf(this), bullet);
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

    public SpriteType getType() {
        return type;
    }
}
