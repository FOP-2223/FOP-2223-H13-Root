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

    // --Variables--//
    /**
     * The GameController that controls the game.
     */
    private final GameController gameController;
    /**
     * The color of the sprite. (fallback for when the sprite has no texture)
     */
    private final Color color;
    /**
     * the height of the sprite.
     */
    private final double height;
    /**
     * The width of the sprite.
     */
    private final double width;
    /**
     * The texture of the sprite.
     */

    private Image texture;
    /**
     * The x-coordinate of the sprite.
     */

    private double x;
    /**
     * The y-coordinate of the sprite.
     */
    private double y;
    /**
     * The current Movement-{@link Direction} of the sprite.
     */
    private @NotNull Direction direction = Direction.NONE;
    /**
     * The remaining life of the sprite.
     */
    private int health;
    /**
     * The movement velocity of the sprite.
     */
    private final double velocity;
    /**
     * whether the sprite is no longer alive.
     */
    private boolean dead = false;

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
        final var newPos = clamp(frame.newX(), frame.newY());
        setX(newPos.getX());
        setY(newPos.getY());
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getHealth() {
        return health;
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
