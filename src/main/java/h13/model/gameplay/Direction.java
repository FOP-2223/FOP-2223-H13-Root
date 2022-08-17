package h13.model.gameplay;

import java.util.Arrays;

/**
 * A Direction is a type of {@linkplain Enum} that represents an orthogonal direction on a 2D plane.
 */
public enum Direction {
    /**
     * The direction of no movement.
     */
    NONE(0, 0),
    /**
     * The direction upwards or direction of view "north".
     */
    UP(0, -1),
    /**
     * The direction to the right or direction of view "east".
     */
    RIGHT(1, 0),
    /**
     * The direction downwards or direction of view "south".
     */
    DOWN(0, 1),
    /**
     * The direction to the left or direction of view "west".
     */
    LEFT(-1, 0),
    /**
     * The direction upwards and to the right or direction of view "north-east".
     */
    UP_RIGHT(1, -1),
    /**
     * The direction downwards and to the right or direction of view "south-east".
     */
    DOWN_RIGHT(1, 1),
    /**
     * The direction downwards and to the left or direction of view "south-west".
     */
    DOWN_LEFT(-1, 1),
    /**
     * The direction upwards and to the left or direction of view "north-west".
     */
    UP_LEFT(-1, -1);

    final int x;
    final int y;
    Direction(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHorizontal() {
        return this == Direction.LEFT || this == Direction.RIGHT;
    }

    public boolean isVertical() {
        return this == Direction.UP || this == Direction.DOWN;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction fromUnitVector(final double x, final double y) {
        return Arrays.stream(Direction.values()).min((d1, d2) -> {
            final double d1Length = Math.sqrt(d1.x * d1.x + d1.y * d1.y);
            final double d2Length = Math.sqrt(d2.x * d2.x + d2.y * d2.y);
            return Double.compare(d1Length, d2Length);
        }).orElse(Direction.NONE);
    }

    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case UP_RIGHT -> DOWN_LEFT;
            case DOWN_RIGHT -> UP_LEFT;
            case DOWN_LEFT -> UP_RIGHT;
            case UP_LEFT -> DOWN_RIGHT;
            default -> NONE;
        };
    }

    public boolean isOpposite(final Direction direction) {
        return this == direction.getOpposite();
    }

    public Direction combine(final Direction other) {
        return equals(NONE) ? other : isOpposite(other) ? NONE : other;
    }
}
