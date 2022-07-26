package h13.view.gui;

import h13.controller.ApplicationSettings;
import h13.controller.GameConstants;
import h13.controller.game.GameController;
import h13.model.gameplay.Playable;
import h13.model.gameplay.sprites.Bullet;
import h13.model.gameplay.sprites.Enemy;
import h13.model.gameplay.sprites.Player;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameBoard extends Canvas implements Playable {
    private Bounds previousBounds = getBoundsInParent();

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
        return gameScene.getGameController();
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void update(final long now) {
        if (!getBoundsInParent().equals(previousBounds)) {
            if (previousBounds.getWidth() > 0 && previousBounds.getHeight() > 0 && getBoundsInParent().getWidth() > 0 && getBoundsInParent().getHeight() > 0) {
                final var scale = getWidth() / previousBounds.getWidth();
                System.out.println("scale: " + scale);
                getGameController().getSprites().forEach(sprite -> {
                    sprite.setX(sprite.getX() * scale);
                    sprite.setY(sprite.getY() * scale);
                });
            }
            previousBounds = getBoundsInParent();
        }
        getGameController().getSprites(Player.class).forEach(player -> player.setY(getHeight() - player.getHeight()));
        final var gc = getGraphicsContext2D();

        // Background
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
        } else {
            gc.clearRect(0, 0, getWidth(), getHeight());
        }

        // Draw bullets first (behind the player)
        getGameController().getSprites(Bullet.class).forEach(bullet -> bullet.render(gc));

        // Draw the enemies
        getGameController().getSprites(Enemy.class).forEach(enemy -> enemy.render(gc));

        // Draw the player last (on top of the enemies)
        getGameController().getSprites(Player.class).forEach(player -> player.render(gc));

        // Draw other sprites
        getGameController().getSprites(s -> !(s instanceof Player) && !(s instanceof Bullet) && !(s instanceof Enemy)).forEach(sprite -> sprite.render(gc));

        final Font font = Font.loadFont(GameConstants.class.getResourceAsStream(GameConstants.STATS_FONT_PATH), getWidth() / 30);
        gc.setFont(font);
        // Draw the score
        getGameController().getSprites(Player.class).forEach(player -> {
            final String msg = "Score: " + player.getScore();
            final Text text = new Text(msg);
            text.setFont(font);
            text.setFont(font);
            gc.setFill(Color.WHITE);
            gc.fillText(msg, 0.03 * getWidth(), 0.01 * getWidth() + text.getLayoutBounds().getHeight());
        });

        // Draw the lives
        getGameController().getSprites(Player.class).forEach(player -> {
            final String msg = "Lives: " + player.getHealth();
            final Text text = new Text(msg);
            text.setFont(font);
            text.setFont(font);
            gc.setFill(Color.WHITE);
            gc.fillText(msg, 0.97 * getWidth() - text.getLayoutBounds().getWidth(), 0.01 * getWidth() + text.getLayoutBounds().getHeight());
        });

        // Draw borders
        gc.setStroke(Color.PALEGREEN);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, getWidth(), getHeight());
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void resume(final long now) {
        // Nothing to do
    }

    public double getScale() {
        return getWidth() / GameConstants.ORIGINAL_GAME_BOUNDS.getWidth();
    }
}
