package h13.view.gui;

import h13.model.GameConstants;
import h13.model.Playable;
import h13.model.sprites.Player;
import h13.model.sprites.Sprite;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class GameBoard extends Canvas implements Playable {
    private Set<Sprite> sprites = new HashSet<>();
    private Bounds previousBounds = getBoundsInParent();

    public GameBoard(double width, double height) {
        super(width, height);
    }

    public Set<Sprite> getSprites() {
        return sprites;
    }

    public <T extends Sprite> Set<T> getSprites(Class<T> clazz) {
        return getSprites().stream().filter(clazz::isInstance).map(clazz::cast).collect(HashSet::new, Set::add, Set::addAll);
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
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.PALEGREEN);
        gc.setLineWidth(4);
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.strokeRect(0, 0, getWidth(), getHeight());
        sprites.forEach(sprite -> sprite.render(gc));
    }

    public double getScale() {
        return getWidth() / GameConstants.ORIGINAL_GAME_BOUNDS.getWidth();
    }
}
