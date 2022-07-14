package h13.model.sprites;

import h13.model.Playable;
import h13.controller.GameController;
import h13.view.gui.GameBoard;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Sprite implements Playable {

    // --Variables--//
    protected final GameController gameController;
    private final Color color;
    private final LongProperty lastUpdate = new SimpleLongProperty(0);
    private final double relativeHeight;
    private final double relativeWidth;

    private final DoubleProperty x;
    private final DoubleProperty y;
    private final DoubleProperty velocityX = new SimpleDoubleProperty(0);
    private final DoubleProperty velocityY = new SimpleDoubleProperty(0);
    protected int health;
    protected double velocity;
    private boolean dead = false;

    private AnimationTimer movementTimer;

    public Sprite(double x, double y, double relativeWidth, double relativeHeight, Color color, double velocity, int health, GameController gameController) {
//        super(x, y, relativeWidth * gameController.getGameBoard().getMaxWidth(), relativeHeight * gameController.getGameBoard().getMaxWidth());
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.velocity = velocity;
        this.color = color;
        this.gameController = gameController;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.health = health;
//        setCache(true);
//        setCacheHint(CacheHint.SPEED);
        init();
    }

    protected boolean coordinatesInBounds(double x, double y, double padding) {
        return x >= padding && x <= getGameBoard().getWidth() - getWidth() - padding
            && y >= padding && y <= getGameBoard().getHeight() - getHeight() - padding;
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
        getGameBoard().removeSprite(this);
    }

    protected void gameTick(GameTickParameters tick) {
        var newPos = getPaddedPosition(tick.newX(), tick.newY(), 0);
        setX(newPos.getX());
        setY(newPos.getY());
        if (health <= 0) {
            die();
        }
    }

    public GameBoard getGameBoard() {
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

    public double getHeight() {
        return relativeHeight * getGameboardWidth();
    }

    public double getWidth() {
        return relativeWidth * getGameboardWidth();
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
//        this.setFill(color);
//        this.widthProperty().bind(getGameBoard().widthProperty().multiply(width));
//        this.heightProperty().bind(getGameBoard().widthProperty().multiply(height));
//        setHeight(5);
    }

    protected double getGameboardWidth() {
        return getGameBoard().getWidth();
    }

    protected double getGameboardHeight() {
        return getGameBoard().getHeight();
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

    public void render(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
//        gc.fillRect(getX(), getY(), getWidth() * getGameboardWidth(), getHeight() * getGameboardHeight());
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

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public DoubleProperty yProperty() {
        return y;
    }

    // getBounds
    public Bounds getBounds() {
        return new BoundingBox(getX(), getY(), getWidth(), getHeight());
    }
}
