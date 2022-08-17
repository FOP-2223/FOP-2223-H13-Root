package h13.model.gameplay.sprites;

import h13.controller.ApplicationSettings;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Direction;
import h13.model.gameplay.Updatable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static h13.controller.GameConstants.ORIGINAL_GAME_BOUNDS;

public abstract class Sprite implements Updatable {

    // --Variables--//
    private final GameController gameController;
    private final Color color;
    private final double height;
    private final double width;

    private Image texture;

    private double x;
    private double y;
    private @NotNull Direction direction = Direction.NONE;
    private int health;
    private final double velocity;
    private boolean dead = false;

    public Sprite(final double x, final double y, final double width, final double height, final Color color, final double velocity, final int health, final GameController gameController) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.color = color;
        this.gameController = gameController;
        this.width = width;
        this.height = height;
        this.health = health;
    }

    protected boolean coordinatesInBounds(final double x, final double y, final double padding) {
        return x >= padding && x <= ORIGINAL_GAME_BOUNDS.getWidth() - getWidth() - padding
            && y >= padding && y <= ORIGINAL_GAME_BOUNDS.getHeight() - getHeight() - padding;
    }

    // --Getters and Setters--//

    public void damage() {
        damage(1);
    }

    public void damage(final int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }

    public void die() {
        health = 0;
        dead = true;
        getGameController().removeSprite(this);
    }

    protected void nextFrame(final GameFrameParameters frame) {
        final var newPos = getBoundedPosition(frame.newX(), frame.newY(), 0);
        setX(newPos.getX());
        setY(newPos.getY());
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getHealth() {
        return health;
    }

    protected Point2D getBoundedPosition(final double x, final double y, final double padding) {
        return new Point2D(
            Math.max(padding, Math.min(ORIGINAL_GAME_BOUNDS.getWidth() - getWidth() - padding, x)),
            Math.max(padding, Math.min(ORIGINAL_GAME_BOUNDS.getHeight() - getHeight() - padding, y))
        );
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getVelocity() {
        return velocity;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isAlive() {
        return !isDead();
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull final Direction direction) {
        this.direction = direction;
    }

    public void moveDown() {
        setDirection(Direction.DOWN);
    }

    public void moveLeft() {
        setDirection(Direction.LEFT);
    }

    public void moveRight() {
        setDirection(Direction.RIGHT);
    }

    public void moveUp() {
        setDirection(Direction.UP);
    }

    public void stop() {
        setDirection(Direction.NONE);
    }

    @Override
    public void update(final double elapsedTime) {
        // Smooth movement
            final double deltaX = getDirection().getX() * velocity * elapsedTime;
            final double deltaY = getDirection().getY() * velocity * elapsedTime;
            final double oldX = getX();
            final double oldY = getY();
            final double newX = oldX + deltaX;
            final double newY = oldY + deltaY;
            nextFrame(
                new GameFrameParameters(
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

    protected record GameFrameParameters(
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
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    // getBounds
    public Bounds getBounds() {
        return new BoundingBox(getX(), getY(), getWidth(), getHeight());
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(final Image texture) {
        this.texture = texture;
    }

    protected void loadTexture(final String path) {
        if (!ApplicationSettings.loadTexturesProperty().get()) {
            return;
        }
        try {
            texture = new Image(path);
        } catch (final Exception e) {
            System.out.println("Failed to load texture: " + path);
            e.printStackTrace();
        }
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public Color getColor() {
        return color;
    }
}
