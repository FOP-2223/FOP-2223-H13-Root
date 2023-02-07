package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Updatable;
import h13.model.gameplay.sprites.Bullet;
import h13.model.gameplay.sprites.Enemy;
import h13.model.gameplay.sprites.Player;
import h13.model.gameplay.sprites.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static h13.controller.GameConstants.*;

/**
 * A GameBoard is a {@link Canvas} on which the {@link h13.model.gameplay.sprites.Sprite}s as well as the HUD are drawn.
 * It is part of the {@link GameScene} and is controlled by a {@link GameController}.
 */
public class GameBoard extends Canvas implements Updatable {

    // --Variables-- //

    /**
     * The {@link GameScene} that contains this {@link GameBoard}.
     *
     * @see GameScene
     */
    private final GameScene gameScene;

    /**
     * The Background of this {@link GameBoard}.
     *
     * @see Image
     */
    private @Nullable Image backgroundImage;

    // --Constructors-- //

    /**
     * Creates a new GameBoard with the given parameters.
     *
     * @param width     The width of the {@link GameBoard}.
     * @param height    The height of the {@link GameBoard}.
     * @param gameScene The {@link GameScene} that contains this {@link GameBoard}.
     */
    public GameBoard(final double width, final double height, final GameScene gameScene) {
        super(width, height);
        this.gameScene = gameScene;
        if (ApplicationSettings.loadBackgroundProperty().get()) {
            try {
                backgroundImage = new Image("/h13/images/wallpapers/Galaxy3.jpg");
//            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/h13/images/wallpapers/Galaxy1.jpg"));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --Utility Methods-- //

    /**
     * Calculates the Scale factor of the {@link GameBoard} based on the {@link GameScene}'s width and height.
     *
     * @return The calculated Scale factor.
     */
    private double getScale() {
        return getWidth() / ORIGINAL_GAME_BOUNDS.getWidth();
    }

    /**
     * Gets the {@link GameController} that controls this {@link GameBoard}.
     *
     * @return The {@link GameController} that controls this {@link GameBoard}.
     * @see GameController
     */
    public GameController getGameController() {
        return gameScene.getController();
    }

    // --Methods-- //

    @Override
    public void update(final double elapsedTime) {
        final double scale = getScale();
        final var gc = getGraphicsContext2D();
        gc.setTransform(new Affine(new Scale(scale, scale)));

        drawBackground(gc);
        drawSprites(gc);

        // Draw hitboxes
//        final var eb = getGameController().getGameState().getEnemyMovement().getEnemyBounds();
//        gc.setStroke(Color.RED);
//        gc.strokeRect(eb.getMinX(), eb.getMinY(), eb.getWidth(), eb.getHeight());

        drawHUD(gc);
        drawBorder(gc);
    }

    /**
     * Draws the background of this {@link GameBoard} to the given {@link GraphicsContext} based on the {@link GameConstants#ORIGINAL_GAME_BOUNDS}.
     * <br>
     * If the {@link #backgroundImage} is not set, the background is cleared with the {@link GraphicsContext#clearRect(double, double, double, double)} method.
     *
     * @param gc The {@link GraphicsContext} to draw the background to.
     */
    private void drawBackground(final GraphicsContext gc) {
        // Background
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, ORIGINAL_GAME_BOUNDS.getWidth(), ORIGINAL_GAME_BOUNDS.getHeight());
        } else {
            gc.clearRect(0, 0, ORIGINAL_GAME_BOUNDS.getWidth(), ORIGINAL_GAME_BOUNDS.getHeight());
        }
    }

    /**
     * Draws the sprites of this {@link GameBoard} to the given {@link GraphicsContext} using the {@link SpriteRenderer} class.
     * <br>
     * The sprites are drawn in the following order (from bottom to top):
     * <ol>
     *     <li>Bullets</li>
     *     <li>Enemies</li>
     *     <li>Player</li>
     *     <li>Others (currently none)</li>
     * </ol>
     *
     * @param gc The {@link GraphicsContext} to draw the sprites to.
     */
    private void drawSprites(final GraphicsContext gc) {
        // define the order in which the sprites are drawn
        final List<Class<? extends Sprite>> spriteOrder = List.of(Bullet.class, Enemy.class, Player.class);
        final var sprites = getGameController().getGameState().getSprites();

        // draw privileged sprites
        spriteOrder.stream()
            .flatMap(clazz -> sprites.stream().filter(s -> clazz.isAssignableFrom(s.getClass())))
            .forEachOrdered(sprite -> SpriteRenderer.renderSprite(gc, sprite));

        // draw other sprites
        sprites.stream()
            .filter(sprite -> spriteOrder.stream().noneMatch(clazz -> clazz.isAssignableFrom(sprite.getClass())))
            .forEachOrdered(sprite -> SpriteRenderer.renderSprite(gc, sprite));
    }

    /**
     * Draws the Heads-Up-Display (HUD) of this {@link GameBoard} to the given {@link GraphicsContext}.
     * <br>
     * The HUD contains the following information:
     * <ul>
     *     <li>Player Score (top left)</li>
     *     <li>Remaining Lives (top right)</li>
     * </ul>
     * <br>
     *
     * @param gc The {@link GraphicsContext} to draw the HUD to.
     */
    private void drawHUD(final GraphicsContext gc) {
        // save the current configuration
        gc.save();

        // draw the score
        gc.setFill(HUD_TEXT_COLOR);
        gc.setFont(HUD_FONT);

        // draw the score
        final var score = getGameController().getPlayer().getScore();
        final var scoreText = String.format("Score: %d", score);
        final var scoreLabel = new Text(scoreText);
        scoreLabel.setFont(HUD_FONT);
        gc.fillText(
            scoreText,
            HUD_PADDING,
            HUD_PADDING + scoreLabel.getBoundsInLocal().getHeight()
        );

        // draw the lives
        final var lives = getGameController().getPlayer().getHealth();
        final var livesText = String.format("Lives: %d", lives);
        final var livesLabel = new Text(livesText);
        livesLabel.setFont(HUD_FONT);
        gc.fillText(
            livesText,
            ORIGINAL_GAME_BOUNDS.getWidth() - HUD_PADDING - livesLabel.getBoundsInLocal().getWidth(),
            HUD_PADDING + livesLabel.getBoundsInLocal().getHeight()
        );

        // restore the previous configuration
        gc.restore();
    }

    /**
     * Draws the border of this {@link GameBoard} to the given {@link GraphicsContext}.
     * <br>
     * The border is drawn using the {@link GraphicsContext#strokeRect(double, double, double, double)} method.
     * <br>
     * The border has the color {@link GameConstants#BORDER_COLOR} and the width {@link GameConstants#BORDER_WIDTH}.
     *
     * @param gc The {@link GraphicsContext} to draw the border to.
     */
    private static void drawBorder(final GraphicsContext gc) {
        // save original settings
        gc.save();

        // Draw borders
        gc.setStroke(BORDER_COLOR);
        gc.setLineWidth(BORDER_WIDTH);
        gc.strokeRect(0, 0, ORIGINAL_GAME_BOUNDS.getWidth(), ORIGINAL_GAME_BOUNDS.getHeight());

        // restore original settings
        gc.restore();
    }
}
