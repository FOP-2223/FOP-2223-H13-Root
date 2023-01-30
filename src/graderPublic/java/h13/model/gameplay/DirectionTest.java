package h13.model.gameplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Junit-Tests for the {@link Direction} class.
 */
public class DirectionTest {
    @Test
    public void testOpposite() {
        for (final Direction d : Direction.values()) {
            final var opposite = d.getOpposite();
            assertEquals(-d.x, opposite.x);
            assertEquals(-d.y, opposite.y);
        }
    }

    @Test
    public void testFromUnitVector() {
        for (final Direction d : Direction.values()) {
            final var fromUnitVector = d.fromVector(d.getX(), d.getY());
            assertEquals(d, fromUnitVector);
        }
    }

    @Test
    public void testCombine() {
        for (final Direction d : Direction.values()) {
            assertEquals(d, d.combine(d));
            assertEquals(Direction.NONE, d.combine(d.getOpposite()));
        }
    }

    @Test
    public void testRotate() {
        for (final Direction d : Direction.values()) {
            assertEquals(d, d.rotate(0));
            assertEquals(d, d.rotate(360));
            assertEquals(d, d.rotate(-360));
            assertEquals(d, d.rotate(720));
            assertEquals(d.getOpposite(), d.rotate(180));
        }
    }
}
