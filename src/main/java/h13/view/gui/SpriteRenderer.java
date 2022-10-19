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
     * @param gc The {@link GraphicsContext} to render the {@link Sprite} on.
     * @param s  The {@link Sprite} to render.
     * @see Sprite
     * @see GraphicsContext
     */
    public static void renderSprite(@NotNull final GraphicsContext gc, @NotNull final Sprite s) {
        // save original configuration
        gc.save();

        if (s.getTexture() != null) {
            // draw texture image
            gc.drawImage(s.getTexture(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
        } else {
            // set color and draw rectangle
            gc.setFill(s.getColor());
            gc.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
        }

        // restore original configuration
        gc.restore();
    }
}
