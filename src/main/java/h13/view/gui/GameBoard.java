package h13.view.gui;

import h13.model.GameConstants;
import h13.model.Playable;
import h13.model.sprites.Bullet;
import h13.model.sprites.Enemy;
import h13.model.sprites.Player;
import h13.model.sprites.Sprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class GameBoard extends Canvas implements Playable {
    private Set<Sprite> sprites = new HashSet<>();
    private Bounds previousBounds = getBoundsInParent();

    private Image backgroundImage;

    public GameBoard(double width, double height) {
        super(width, height);
        if (GameConstants.LOAD_TEXTURES) {
            try {
                backgroundImage = new Image("/h13/images/wallpapers/Galaxy3.jpg");
//            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/h13/images/wallpapers/Galaxy1.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Set<Sprite> getSprites() {
        return sprites;
    }

    public <T extends Sprite> Set<T> getSprites(Class<T> clazz) {
        return getSprites().stream().filter(clazz::isInstance).map(clazz::cast).collect(HashSet::new, Set::add, Set::addAll);
    }

    public Set<Sprite> getSprites(Predicate<Sprite> predicate) {
        return getSprites().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    public void clearSprites() {
        sprites.clear();
    }

    public void clearSprites(Class<? extends Sprite> clazz) {
        sprites.stream().filter(clazz::isInstance).forEach(sprite -> sprites.remove(sprite));
    }

    public void clearSprites(Predicate<Sprite> predicate) {
        sprites.stream().filter(predicate).forEach(sprites::remove);
    }

    /**
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void update(long now) {
        if (!getBoundsInParent().equals(previousBounds)) {
            if (previousBounds.getWidth() > 0 && previousBounds.getHeight() > 0 && getBoundsInParent().getWidth() > 0 && getBoundsInParent().getHeight() > 0) {
                var scale = getWidth() / previousBounds.getWidth();
                System.out.println("scale: " + scale);
                sprites.forEach(sprite -> {
                    sprite.setX(sprite.getX() * scale);
                    sprite.setY(sprite.getY() * scale);
                });
            }
            previousBounds = getBoundsInParent();
        }
        getSprites(Player.class).forEach(player -> player.setY(getHeight() - player.getHeight()));
        var gc = getGraphicsContext2D();
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, getWidth(), getHeight());
        } else {
            gc.clearRect(0, 0, getWidth(), getHeight());
        }
        // Draw bullets first (behind the player)
        getSprites(Bullet.class).forEach(bullet -> bullet.render(gc));

        // Draw the enemies
        getSprites(Enemy.class).forEach(enemy -> enemy.render(gc));

        // Draw the player last (on top of the enemies)
        getSprites(Player.class).forEach(player -> player.render(gc));

        // Draw everything else
        getSprites(s -> !(s instanceof Player) && !(s instanceof Bullet) && !(s instanceof Enemy)).forEach(sprite -> sprite.render(gc));

        // Draw borders
        gc.setStroke(Color.PALEGREEN);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, getWidth(), getHeight());
    }

    public double getScale() {
        return getWidth() / GameConstants.ORIGINAL_GAME_BOUNDS.getWidth();
    }
}
