package h13.view.gui;

import h13.model.gameplay.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

public final class SpriteRenderer {
    private SpriteRenderer() {
        throw new RuntimeException("Cannot instantiate SpriteRenderer");
    }

    public static void renderSprite(@NotNull final GraphicsContext gc, @NotNull final Sprite s) {
        if (s.getTexture() != null) {
            gc.drawImage(s.getTexture(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
        } else {
            gc.setFill(s.getColor());
            gc.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
        }
    }
}
