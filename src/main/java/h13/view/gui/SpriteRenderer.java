package h13.view.gui;

import h13.model.gameplay.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/**
 * A SpriteRenderer is responsible for rendering a {@link Sprite} on a {@link GraphicsContext}.
 */
public final class SpriteRenderer {
    // --Constructors-- //

    /**
     * Overrides the default constructor.
     */
    private SpriteRenderer() {
        throw new RuntimeException("Cannot instantiate SpriteRenderer");
    }

    // --Methods-- //

    /**
     * Renders a given {@link Sprite} on a {@link GraphicsContext}.
     *
     * @param gc    The {@link GraphicsContext} to render the {@link Sprite} on.
     * @param s     The {@link Sprite} to render.
     * @param scale The scale factor to use when rendering the {@link Sprite}.
     * @see Sprite
     * @see GraphicsContext
     */
    public static void renderSprite(@NotNull final GraphicsContext gc, @NotNull final Sprite s, final double scale) {
        if (s.getTexture() != null) {
            gc.drawImage(s.getTexture(), s.getX() * scale, s.getY() * scale, s.getWidth() * scale, s.getHeight() * scale);
        } else {
            gc.setFill(s.getColor());
            gc.fillRect(s.getX() * scale, s.getY() * scale, s.getWidth() * scale, s.getHeight() * scale);
        }
    }
}
