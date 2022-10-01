package h13.model.gameplay;

import h13.model.gameplay.sprites.Sprite;

import java.util.HashSet;
import java.util.Set;

public class GameState {

    // --Variables-- //
    /**
     * The {@link Sprite}s that are present in the game.
     * This is a set because there is no guarantee that the sprites are not duplicated.
     * This is not a problem because the set is used to remove duplicates.
     *
     * @see #getSprites()
     */
    private final Set<Sprite> sprites = new HashSet<>();

    /**
     * Gets the value of {@link #sprites} field.
     *
     * @return The value of {@link #sprites} field.
     * @see #sprites
     */
    public Set<Sprite> getSprites() {
        return sprites;
    }
}
