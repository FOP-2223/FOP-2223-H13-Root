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

/**
 * A sprite is a game object that can be placed on the game board.
 */
public abstract class Sprite implements Updatable {

    // --Variables-- //

    /**
     * The x-coordinate of the sprite.
     */
    private double x;
    /**
     * The y-coordinate of the sprite.
     */
    private double y;
    /**
     * The width of the sprite.
     */
    private final double width;
    /**
     * the height of the sprite.
     */
    private final double height;
    /**
     * The movement velocity of the sprite.
     */
    private final double velocity;
    /**
     * The remaining life of the sprite.
     */
    private int health;
    /**
     * The current Movement-{@link Direction} of the sprite.
     */
    private @NotNull Direction direction = Direction.NONE;
    /**
     * The color of the sprite. (fallback for when the sprite has no texture)
     */
    private final Color color;
    /**
     * The texture of the sprite.
     */
    private Image texture;
    /**
     * The GameController that controls the game.
     */
    private final GameController gameController;

    // --Constructors-- //

    /**
     * Constructs a new Sprite with the given parameters.
     *
     * @param x              the x-coordinate of the sprite.
     * @param y              the y-coordinate of the sprite.
     * @param width          the width of the sprite.
     * @param height         the height of the sprite.
     * @param color          the color of the sprite.
     * @param velocity       the movement velocity of the sprite.
     * @param health         the amount of health the sprite should have.
     * @param gameController the GameController that controls the game.
     */
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

    // --Getters and Setters-- //

    /**
     * Gets the value of the {@link #x} field.
     *
     * @return the value of the {@link #x} field.
     * @see #x
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the value of the {@link #x} field to the given value.
     *
     * @param x the new value of the {@link #x} field.
     * @see #x
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Gets the value of the {@link #y} field.
     *
     * @return the value of the {@link #y} field.
     * @see #y
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the value of the {@link #y} field to the given value.
     *
     * @param y the new value of the {@link #y} field.
     * @see #y
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Gets the value of the {@link #width} field.
     *
     * @return the value of the {@link #width} field.
     * @see #width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the value of the {@link #height} field.
     *
     * @return the value of the {@link #height} field.
     * @see #height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the value of the {@link #velocity} field.
     *
     * @return the value of the {@link #velocity} field.
     * @see #velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Gets the value of the {@link #health} field.
     *
     * @return the value of the {@link #health} field.
     * @see #health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the value of the {@link #health} field to the given value.
     *
     * @param health the new value of the {@link #health} field.
     * @see #health
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Gets the value of the {@link #direction} field.
     *
     * @return the value of the {@link #direction} field.
     * @see #direction
     */
    public @NotNull Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the {@link #direction} field to the given value.
     *
     * @param direction the new value of the {@link #direction} field.
     * @see #direction
     */
    public void setDirection(@NotNull final Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns whether the sprite is no longer alive.
     *
     * @return whether the sprite is no longer alive.
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Checks whether the sprite is alive.
     *
     * @return whether the sprite is alive.
     * @see #isDead()
     */
    public boolean isAlive() {
        return !isDead();
    }

    /**
     * Gets the value of the {@link #color} field.
     *
     * @return the value of the {@link #color} field.
     * @see #color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the value of the {@link #texture} field.
     *
     * @return the value of the {@link #texture} field.
     * @see #texture
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Sets the value of the {@link #texture} field to the given value.
     *
     * @param texture the new value of the {@link #texture} field.
     * @see #texture
     */
    public void setTexture(final Image texture) {
        this.texture = texture;
    }

    /**
     * Loads the texture of the sprite from the given path and sets it to the {@link #texture} field.
     *
     * @param path the path to the texture.
     */
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

    /**
     * Gets the value of the {@link #gameController} field.
     *
     * @return the value of the {@link #gameController} field.
     * @see #gameController
     */
    public GameController getGameController() {
        return gameController;
    }

    //--Utility Methods--//

    /**
     * Gets the {@link Bounds} of the sprite.
     *
     * @return the {@link Bounds} of the sprite.
     */
    public Bounds getBounds() {
        return new BoundingBox(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Returns the closest coordinate to the given coordinate that is inside the game bounds.
     *
     * @param x the x-coordinate to be clamped.
     * @param y the y-coordinate to be clamped.
     * @return the clamped coordinate.
     * @see <a href="https://en.wikipedia.org/wiki/Clamping_(graphics)">Clamping_(graphics)</a>
     */
    protected Point2D clamp(final double x, final double y) {
        return new Point2D(
            Math.max(0, Math.min(ORIGINAL_GAME_BOUNDS.getWidth() - getWidth(), x)),
            Math.max(0, Math.min(ORIGINAL_GAME_BOUNDS.getHeight() - getHeight(), y))
        );
    }

    // --movement-- //

    /**
     * Sets the {@linkplain #direction movement direction} of the sprite to {@link Direction#UP}.
     */
    public void moveUp() {
        setDirection(Direction.UP);
    }

    /**
     * Sets the {@linkplain #direction movement direction} of the sprite to {@link Direction#DOWN}.
     */
    public void moveDown() {
        setDirection(Direction.DOWN);
    }

    /**
     * Sets the {@linkplain #direction movement direction} of the sprite to {@link Direction#LEFT}.
     */
    public void moveLeft() {
        setDirection(Direction.LEFT);
    }

    /**
     * Sets the {@linkplain #direction movement direction} of the sprite to {@link Direction#RIGHT}.
     */
    public void moveRight() {
        setDirection(Direction.RIGHT);
    }

    /**
     * Sets the {@linkplain #direction movement direction} of the sprite to {@link Direction#NONE}.
     */
    public void stop() {
        setDirection(Direction.NONE);
    }

    // --health-- //

    /**
     * Damages the sprite by the given amount.
     * <br>
     * If the sprite's health is less than or equal to 0, the method {@link #die()} is called.
     *
     * @param amount the amount to damage the sprite by.
     */
    public void damage(final int amount) {
        health -= amount;
        if (health <= 0) {
            die();
        }
    }

    /**
     * Damages the sprite by 1.
     *
     * @see #damage(int)
     */
    public void damage() {
        damage(1);
    }

    /**
     * Kills the sprite.
     */
    public void die() {
        getGameController().removeSprite(this);
    }
    // --update-- //

    @Override
    public void update(final double elapsedTime) {
        // Smooth movement
        final double deltaX = getDirection().getX() * velocity * elapsedTime;
        final double deltaY = getDirection().getY() * velocity * elapsedTime;
        var newPos = new Point2D(getX() + deltaX, getY() + deltaY);
        setX(newPos.getX());
        setY(newPos.getY());

        // Check if the sprite is outside the game bounds
        if (!ORIGINAL_GAME_BOUNDS.contains(getBounds())) {
            onOutOfBounds();
        }
    }

    /**
     * Called when the sprite is outside the game bounds.
     */
    protected void onOutOfBounds() {
        // clamp position
        final var newPos = clamp(getX(), getY());
        setX(newPos.getX());
        setY(newPos.getY());
    }
}
