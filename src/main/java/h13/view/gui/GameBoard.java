package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.scene.game.GameController;
import h13.model.gameplay.Updatable;
import h13.model.gameplay.sprites.Bullet;
import h13.model.gameplay.sprites.Enemy;
import h13.model.gameplay.sprites.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static h13.controller.GameConstants.*;

public class GameBoard extends Canvas implements Updatable {

//    private final GameController gameController;

    private final GameScene gameScene;

    private Image backgroundImage;

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

    public GameController getGameController() {
        return gameScene.getController();
    }

    /**
     * @param elapsedTime The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void update(final double elapsedTime) {
        final var gc = getGraphicsContext2D();
        final double scale = getWidth() / ORIGINAL_GAME_BOUNDS.getWidth();

        // Background
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
        } else {
            gc.clearRect(0, 0, getWidth(), getHeight());
        }

        // Draw bullets first (behind the player)
        getGameController().getSprites(Bullet.class).forEach(bullet -> SpriteRenderer.renderSprite(gc, bullet, scale));

        // Draw the enemies
        getGameController().getSprites(Enemy.class).forEach(enemy -> SpriteRenderer.renderSprite(gc, enemy, scale));

        // Draw the player last (on top of the enemies)
        SpriteRenderer.renderSprite(gc, getGameController().getPlayer(), scale);

        // Draw other sprites
        getGameController().getSprites(s -> !(s instanceof Player) && !(s instanceof Bullet) && !(s instanceof Enemy)).forEach(sprite -> SpriteRenderer.renderSprite(gc, sprite,scale));

        final Font font = Font.loadFont(GameConstants.class.getResourceAsStream(GameConstants.STATS_FONT_PATH), getWidth() / 30);
        gc.setFont(font);
        getGameController().getSprites(Player.class).forEach(player -> {
            // Draw the score
            final String score = "Score: " + player.getScore();
            final Text scoreLabel = new Text(score);
            scoreLabel.setFont(font);
            scoreLabel.setFont(font);
            gc.setFill(Color.WHITE);
            gc.fillText(score, 0.03 * getWidth(), 0.01 * getWidth() + scoreLabel.getLayoutBounds().getHeight());

            // Draw the lives
            final String lives = "Lives: " + player.getHealth();
            final Text livesLabel = new Text(lives);
            livesLabel.setFont(font);
            livesLabel.setFont(font);
            gc.setFill(Color.WHITE);
            gc.fillText(lives, 0.97 * getWidth() - livesLabel.getLayoutBounds().getWidth(), 0.01 * getWidth() + livesLabel.getLayoutBounds().getHeight());
        });

        // Draw borders
        gc.setStroke(Color.PALEGREEN);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, getWidth(), getHeight());
    }

    public double getScale() {
        return getWidth() / ORIGINAL_GAME_BOUNDS.getWidth();
    }
}
