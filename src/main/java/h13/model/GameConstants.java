package h13.model;


import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public final class GameConstants {
    public static final Bounds ORIGINAL_GAME_BOUNDS = new BoundingBox(
        0,
        0,
        256,
        224);
    public static final double ASPECT_RATIO = ORIGINAL_GAME_BOUNDS.getWidth() / ORIGINAL_GAME_BOUNDS.getHeight();
//    public static final Bounds shipSize

    public static final int ENEMY_ROWS = 5;
    public static final int ENEMY_COLS = 11;

    public static final double SHIP_PADING = 0.01;
    public static final double HORIZONTAL_ENEMY_MOVE_DISTANCE = 0.1;
    public static final double VERTICAL_ENEMY_MOVE_DISTANCE = .01;
    public static final double RELATIVE_SHIP_WIDTH = (1d - HORIZONTAL_ENEMY_MOVE_DISTANCE) / ENEMY_COLS - 2 * SHIP_PADING;
    public static final double ENEMY_SHOOTING_PROBABILITY = 0.0005;
    public static final double HORIZONTAL_ENEMY_MOVEMENT_DURATION = 2;
    public static final double VERTICAL_ENEMY_MOVEMENT_DURATION = .5;
}
