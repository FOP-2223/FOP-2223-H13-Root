package h13.view.gui;

import h13.model.gameplay.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

public final class SpriteRenderer {
    private SpriteRenderer() {
        throw new RuntimeException("Cannot instantiate SpriteRenderer");
    }

    public static void renderSprite(@NotNull final GraphicsContext gc, @NotNull final Sprite s, final double scale) {
        if (s.getTexture() != null) {
            gc.drawImage(s.getTexture(), s.getX() * scale, s.getY() * scale, s.getWidth() * scale, s.getHeight() * scale);
        } else {
            gc.setFill(s.getColor());
            gc.fillRect(s.getX() * scale, s.getY() * scale, s.getWidth() * scale, s.getHeight() * scale);
        }
    }
}
