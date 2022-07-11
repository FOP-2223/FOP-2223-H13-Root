package h13;


import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public final class GameConstants {
    public static final Bounds ORIGINAL_GAME_BOUNDS = new BoundingBox(
        0,
        0,
        256d,
        224d);
    public static final double ASPECT_RATIO = ORIGINAL_GAME_BOUNDS.getWidth() / ORIGINAL_GAME_BOUNDS.getHeight();
//    public static final Bounds shipSize
    public static final double RELATIVE_SHIP_WIDTH = 0.1;
}
