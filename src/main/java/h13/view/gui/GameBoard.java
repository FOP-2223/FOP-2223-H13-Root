package h13.view.gui;

import h13.model.GameConstants;
import h13.model.Playable;
import h13.model.sprites.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class GameBoard extends Canvas implements Playable {
    private Set<Sprite> sprites = new HashSet<>();

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
